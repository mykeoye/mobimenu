package io.mobimenu.core.service.payment;

import io.mobimenu.payment.PaymentProcessor;
import io.mobimenu.core.port.in.payment.ReQueryPaymentUseCase;
import io.mobimenu.core.port.in.payment.VerifyPaymentUseCase;
import io.mobimenu.core.port.out.order.OrderSaveOperationsPort;
import io.mobimenu.domain.Order;
import io.mobimenu.domain.enums.PaymentType;
import io.smallrye.mutiny.Uni;
import io.mobimenu.common.api.Code;
import io.mobimenu.common.api.Failure;
import io.mobimenu.common.util.IdentifierProvider;
import io.mobimenu.core.port.in.payment.InitiatePaymentUseCase;
import io.mobimenu.core.port.out.order.OrderQueryOperationsPort;
import io.mobimenu.core.port.out.payment.PaymentQueryOperationsPort;
import io.mobimenu.core.port.out.payment.PaymentSaveOperationsPort;
import io.mobimenu.core.port.out.restaurant.RestaurantQueryOperationsPort;
import io.mobimenu.domain.Payment;
import io.smallrye.mutiny.tuples.Tuple2;
import lombok.RequiredArgsConstructor;
import java.util.Map;

@RequiredArgsConstructor
public class PaymentService implements InitiatePaymentUseCase, ReQueryPaymentUseCase, VerifyPaymentUseCase {

    private final RestaurantQueryOperationsPort restaurantQueryOperationsPort;
    private final PaymentQueryOperationsPort paymentQueryOperationsPort;
    private final PaymentSaveOperationsPort paymentSaveOperationsPort;
    private final OrderQueryOperationsPort orderQueryOperationsPort;
    private final PaymentProcessor paymentProcessor;
    private final OrderSaveOperationsPort orderSaveOperationsPort;

    @Override
    public Uni<String> generateTransactionRef(GeneratePaymentRefCommand command) {
        // TODO - Check for the edge case where the payment has already reached the terminal state (SUCCESSFUL or FAILED)
        return restaurantQueryOperationsPort.getById(command.getRestaurantId())
                .onItem().ifNull().failWith(Failure.of(Code.RESTAURANT_NOT_FOUND))
                .flatMap(ignored -> orderQueryOperationsPort.getById(command.getOrderId()))
                .onItem().ifNull().failWith(Failure.of(Code.ORDER_NOT_FOUND))
                .flatMap(ignored -> paymentQueryOperationsPort.getByOrderAndRestaurant(command.getOrderId(), command.getRestaurantId()))
                .onItem().ifNull().switchTo(() -> paymentSaveOperationsPort.savePayment(
                        Payment.withRequiredFields(IdentifierProvider.uuid(command.getPaymentChannel().name()),
                                command.getRestaurantId(),
                                command.getOrderId(),
                                command.getPaymentChannel(),
                                PaymentType.BILL,
                                Payment.Status.PENDING)
                ))
                .map(Payment::getPaymentRef);
    }

    @Override
    public Uni<Payment.Status> requeryPayment(Payment.PaymentId paymentId) {
        return paymentQueryOperationsPort.getById(paymentId)
                .onItem().ifNull().failWith(Failure.of(Code.PAYMENT_NOT_FOUND))
                .flatMap(payment -> {
                    // If the payment is at the terminal state, just return the current status else we want to make
                    // a call to the payment processor and update the payment entity with the new details
                    if (Payment.Status.PENDING != payment.getStatus()) {
                        return Uni.createFrom().item(payment::getStatus);
                    }
                    return paymentProcessor.queryTransaction(payment.getPaymentRef())
                            .flatMap(response -> paymentSaveOperationsPort.updatePayment(paymentId,
                                    response.isTransactionSuccessful()
                                        ? Payment.Status.SUCCESSFUL
                                        : Payment.Status.FAILED,
                                    Map.of()))
                            .map(Payment::getStatus);
                });
    }

    @Override
    public Uni<Payment.Status> verifyPayment(Payment.PaymentId paymentId) {
        return paymentQueryOperationsPort.getById(paymentId)
                .onItem().ifNull().failWith(Failure.of(Code.PAYMENT_NOT_FOUND))
                .flatMap(payment -> paymentProcessor.queryTransaction(payment.getPaymentRef())
                        .map(processorResponse -> Tuple2.of(payment,
                                processorResponse.isTransactionSuccessful()
                                    ? Payment.Status.SUCCESSFUL
                                    : Payment.Status.FAILED)))
                .map(tuple -> {
                    Payment payment = tuple.getItem1();
                    Payment.Status status = tuple.getItem2();
                    return Payment.withAllFields(
                            payment.getId(),
                            payment.getPaymentRef(),
                            payment.getRestaurantId(),
                            payment.getOrderId(),
                            status,
                            payment.getPaymentChannel(),
                            PaymentType.BILL);
                })
                .flatMap(paymentSaveOperationsPort::updatePayment)
                .flatMap(payment -> orderSaveOperationsPort.updatePaymentStatus(payment.getOrderId(),
                        Payment.Status.SUCCESSFUL.equals(payment.getStatus())
                            ? Order.PaymentStatus.PAID
                            : Order.PaymentStatus.PENDING)
                        .map(s -> payment))
                .map(Payment::getStatus);
    }
}
