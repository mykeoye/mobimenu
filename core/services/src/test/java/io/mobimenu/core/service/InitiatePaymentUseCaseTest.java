package io.mobimenu.core.service;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.mobimenu.common.api.Code;
import io.mobimenu.common.api.Failure;
import io.mobimenu.core.port.in.payment.InitiatePaymentUseCase;
import io.mobimenu.core.port.out.order.OrderSaveOperationsPort;
import io.mobimenu.core.port.out.restaurant.RestaurantSaveOperationsPort;
import io.mobimenu.core.service.context.AbstractUseCaseTest;
import io.mobimenu.domain.Order;
import io.mobimenu.domain.OrderItem;
import io.mobimenu.domain.Restaurant;
import io.mobimenu.domain.User;
import io.mobimenu.domain.enums.PaymentChannel;
import io.mobimenu.domain.enums.SalesChannel;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class InitiatePaymentUseCaseTest extends AbstractUseCaseTest {

    @Inject
    RestaurantSaveOperationsPort restaurantSaveOperationsPort;

    @Inject
    OrderSaveOperationsPort orderSaveOperationsPort;

    @Inject
    InitiatePaymentUseCase initiatePaymentUseCase;

    @Test
    @DisplayName("A payment cannot be generated for a non-existent restaurant")
    public void nonExistentRestaurant() {
        var throwable = initiatePaymentUseCase.generateTransactionRef(generatePaymentRefCommand(
                    ObjectId.get().toString(),
                    ObjectId.get().toString()))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitFailure().assertFailed().getFailure();
        var error = Failure.from(throwable).getError();
        assertEquals(Code.RESTAURANT_NOT_FOUND, error);
    }

    @Test
    @DisplayName("A payment cannot be generated for a non-existent order")
    public void noPaymentForNonExistentOrder() {
        var restaurant = restaurantSaveOperationsPort.saveRestaurant(
                        Restaurant.withRequiredFields("Lulu's ChickWizz Restaurant", Restaurant.Type.SINGLE),
                        ObjectId.get().toString())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        var throwable = initiatePaymentUseCase.generateTransactionRef(generatePaymentRefCommand(
                        restaurant.getRestaurantId(),
                        ObjectId.get().toString()
                ))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitFailure().assertFailed().getFailure();
        var error = Failure.from(throwable).getError();
        assertEquals(Code.ORDER_NOT_FOUND, error);
    }

    @Test
    @DisplayName("A payment reference is successfully generated if all invariants checkout")
    public void generatingAPaymentRefIsSuccessful() {
        var restaurant = restaurantSaveOperationsPort.saveRestaurant(
                        Restaurant.withRequiredFields("Lulu's ChickWizz Restaurant", Restaurant.Type.SINGLE),
                        ObjectId.get().toString())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        var order = createOrder(restaurant.getRestaurantId());
        assertNotNull(order);

        var paymentRef = initiatePaymentUseCase.generateTransactionRef(generatePaymentRefCommand(
                        restaurant.getRestaurantId(),
                        order.getOrderId()
                ))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();
        assertNotNull(paymentRef);
    }

    @Test
    @DisplayName("Ensure that the same payment ref, is returned for the same parameters (idempotent)")
    public void ensureThePaymentRefGenerationIsIdempotent() {
        var restaurant = restaurantSaveOperationsPort.saveRestaurant(
                        Restaurant.withRequiredFields("Lulu's ChickWizz Restaurant", Restaurant.Type.SINGLE),
                        ObjectId.get().toString())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        var order = createOrder(restaurant.getRestaurantId());
        assertNotNull(order);

        var paymentRefOne = initiatePaymentUseCase.generateTransactionRef(generatePaymentRefCommand(
                        restaurant.getRestaurantId(),
                        order.getOrderId()
                ))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();
        assertNotNull(paymentRefOne);

        var paymentRefTwo = initiatePaymentUseCase.generateTransactionRef(generatePaymentRefCommand(
                        restaurant.getRestaurantId(),
                        order.getOrderId()
                ))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();
        assertNotNull(paymentRefTwo);
        assertEquals(paymentRefOne, paymentRefTwo);
    }

    private InitiatePaymentUseCase.GeneratePaymentRefCommand generatePaymentRefCommand(String restaurantId, String orderId) {
        return InitiatePaymentUseCase.GeneratePaymentRefCommand.builder()
                .paymentChannel(PaymentChannel.CASH)
                .restaurantId(restaurantId)
                .orderId(orderId)
                .build();
    }

    private Order createOrder(String restaurantId) {
        return orderSaveOperationsPort.saveOrder(Order.withRequiredFields(
                "2",
                SalesChannel.DINE_IN,
                Order.Status.CONFIRMED,
                "Table 4",
                Order.PaymentStatus.PENDING,
                restaurantId,
                ObjectId.get().toString(),
                "Amaka",
                Set.of(OrderItem.withRequiredFields("Fufu", new BigDecimal("200.00"), 5, 5))))
                .subscribe().withSubscriber(UniAssertSubscriber.create()).awaitItem().getItem();
    }

    @AfterEach
    void cleanup() {
        cleanupRestaurant();
        cleanupOrders();
    }

}
