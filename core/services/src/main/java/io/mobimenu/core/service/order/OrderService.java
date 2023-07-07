package io.mobimenu.core.service.order;

import io.mobimenu.common.util.IdentifierProvider;
import io.mobimenu.core.port.out.events.DomainEvent;
import io.mobimenu.core.port.out.events.DomainEventPublisher;
import io.mobimenu.core.port.out.events.EventType;
import io.mobimenu.core.port.out.payment.PaymentSaveOperationsPort;
import io.mobimenu.domain.Payment;
import io.mobimenu.domain.enums.PaymentChannel;
import io.mobimenu.domain.enums.PaymentType;
import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.Meal;
import io.mobimenu.domain.Order;
import java.util.Map;
import java.util.stream.Collectors;
import io.mobimenu.common.api.Code;
import io.mobimenu.domain.OrderItem;
import io.smallrye.mutiny.tuples.Tuple2;
import lombok.RequiredArgsConstructor;
import io.mobimenu.common.api.Failure;
import io.mobimenu.core.port.in.order.PlaceOrderUseCase;
import io.mobimenu.core.port.in.order.ProcessOrderUseCase;
import io.mobimenu.core.port.in.order.UpdateOrderStatusUseCase;
import io.mobimenu.core.port.out.meal.MealQueryOperationsPort;
import io.mobimenu.core.port.out.order.OrderSaveOperationsPort;
import io.mobimenu.core.port.out.order.OrderQueryOperationsPort;
import io.mobimenu.core.port.out.restaurant.RestaurantQueryOperationsPort;

@RequiredArgsConstructor
public class OrderService implements PlaceOrderUseCase, ProcessOrderUseCase, UpdateOrderStatusUseCase {

    private final RestaurantQueryOperationsPort restaurantQueryOperationsPort;
    private final MealQueryOperationsPort mealQueryOperationsPort;
    private final OrderSaveOperationsPort orderSaveOperationsPort;
    private final OrderQueryOperationsPort orderQueryOperationsPort;
    private final PaymentSaveOperationsPort paymentSaveOperationsPort;
    private final DomainEventPublisher<Order> domainEventPublisher;

    @Override
    public Uni<Order> placeOrder(PlaceOrderCommand command) {
        var restaurantId = command.getRestaurantId();
        return restaurantQueryOperationsPort.getById(restaurantId)
                .onItem().ifNull().failWith(Failure.of(Code.RESTAURANT_NOT_FOUND))
                .chain(() -> mealQueryOperationsPort.getMealsByIdsAndStatus(command.getMealIdFromItems(), Meal.Status.AVAILABLE))
                .flatMap(meals -> meals.size() != command.getItems().size()
                        ? Uni.createFrom().failure(Failure.of(Code.MEALS_NOT_FOUND))
                        : Uni.createFrom().item(meals))
                .map(meals -> {
                    Map<String, PlaceOrderCommand.Item> foodItems = command.getMapOfItems();
                    return meals.stream().map(meal -> {
                        PlaceOrderCommand.Item item = foodItems.get(meal.getId().mealId());
                        return OrderItem.withRequiredFields(
                                meal.getName(),
                                meal.getNormalPrice(),
                                item.quantity(),
                                meal.getCookingDuration());
                    }).collect(Collectors.toSet());
                })
                .flatMap(orderItems -> orderQueryOperationsPort.getCount().map(count -> Tuple2.of(count, orderItems)))
                .flatMap(tuple -> orderSaveOperationsPort.saveOrder(Order.withRequiredFields(
                        "#%d".formatted(tuple.getItem1() + 1),
                        command.getSalesChannel(),
                        Order.Status.PENDING,
                        command.getTableNumber(),
                        Order.PaymentStatus.PENDING,
                        restaurantId,
                        command.getCustomerId(),
                        command.getCustomerName(),
                        tuple.getItem2()
                ))).call(order -> {
                    domainEventPublisher.publish(DomainEvent.of(EventType.NEW_ORDER, order));
                    return Uni.createFrom().voidItem();
                });
    }

    @Override
    public Uni<Order> processOrder(ProcessOrderCommand command) {
        return orderQueryOperationsPort.getByOrderIdAndRestaurantId(command.getOrderId(), command.getRestaurantId())
                .onItem().ifNull().failWith(Failure.of(Code.ORDER_NOT_FOUND))
                .map(order -> Order.withAllFields(order.getOrderId(),
                        order.getOrderNum(),
                        order.getSalesChannel(),
                        command.getStatus(),
                        command.getWaitTimeInMinutes(),
                        order.getPaymentStatus(),
                        order.getRestaurantId(),
                        order.getItems()))
                .flatMap(orderSaveOperationsPort::updateOrder);
    }

    @Override
    public Uni<Order.Status> updateOrderStatus(UpdateOrderStatusCommand command) {
        return orderQueryOperationsPort.getByOrderIdAndRestaurantId(command.getOrderId(), command.getRestaurantId())
                .onItem().ifNull().failWith(Failure.of(Code.ORDER_NOT_FOUND))
                .flatMap(o -> orderSaveOperationsPort.updateStatus(command.getOrderId(), command.getStatus()));
    }

    @Override
    public Uni<Order.PaymentStatus> updatePaymentStatus(UpdatePaymentStatusCommand command) {
        PaymentChannel channel = command.getPaymentChannel();
        return orderQueryOperationsPort.getByOrderIdAndRestaurantId(command.getOrderId(), command.getRestaurantId())
                .onItem().ifNull().failWith(Failure.of(Code.ORDER_NOT_FOUND))
                .flatMap(o -> {
                    // If the payment status is already in the terminal state. There's no need to update it
                    if (Order.PaymentStatus.PAID.equals(o.getPaymentStatus())) {
                        return Uni.createFrom().failure(Failure.of(Code.ORDER_PAYMENT_STATUS_UPDATED));
                    }
                    return orderSaveOperationsPort.updatePaymentStatus(command.getOrderId(), command.getPaymentStatus());
                })
                .flatMap(paymentStatus -> {
                    // Payments made via card will have passed through the payment processor 'generate
                    // transaction reference flow' and as such would have already had an entry in the payment log.
                    // For CASH, POS and BANK channels this entry will have to be created, since it requires manual
                    // checking to confirm the payment made. Such payments do not flow through the payment processor
                    if (PaymentChannel.CARD.equals(channel)) {
                        return Uni.createFrom().item(() -> paymentStatus);
                    }
                    return paymentSaveOperationsPort.savePayment(Payment.withRequiredFields(
                                IdentifierProvider.uuid(channel.name()),
                                command.getRestaurantId(),
                                command.getOrderId(),
                                channel,
                                PaymentType.BILL,
                                Order.PaymentStatus.PAID.equals(paymentStatus)
                                    ? Payment.Status.SUCCESSFUL
                                    : Payment.Status.PENDING))
                        .map(p -> paymentStatus);
                });
    }

}
