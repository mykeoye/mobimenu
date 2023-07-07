package io.mobimenu.core.service;

import io.mobimenu.core.port.in.order.PlaceOrderUseCase;
import io.mobimenu.core.port.out.meal.MealSaveOperationsPort;
import io.mobimenu.core.port.out.restaurant.RestaurantSaveOperationsPort;
import io.mobimenu.core.service.context.AbstractUseCaseTest;
import io.mobimenu.domain.Category;
import io.mobimenu.domain.Meal;
import io.mobimenu.domain.Order;
import io.mobimenu.domain.Restaurant;
import io.mobimenu.domain.enums.PaymentChannel;
import io.mobimenu.domain.enums.SalesChannel;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class PlaceOrderUseCaseTest extends AbstractUseCaseTest {

    @Inject
    PlaceOrderUseCase placeOrderUseCase;

    @Inject
    MealSaveOperationsPort mealSaveOperationsPort;

    @Inject
    RestaurantSaveOperationsPort restaurantSaveOperationsPort;

    @Test
    @DisplayName("As a customer i should be able to place an order successfully")
    void shouldBeAbleToPlaceAnOrder() {
        var restaurant = restaurantSaveOperationsPort.saveRestaurant(createRestaurant(), ObjectId.get().toString())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        var meal = mealSaveOperationsPort.saveMeal(Meal.withRequiredFields("Fufu",
                "This is nigerian fufu",
                new Category.CategoryId(ObjectId.get().toString()),
                Set.of(), Meal.Status.AVAILABLE,
                Set.of(),
                null,
                BigDecimal.TEN,
                BigDecimal.ONE,
                15, restaurant.getRestaurantId()))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        var order = placeOrderUseCase.placeOrder(createPlaceOrderCommand(restaurant.getRestaurantId(), meal.mealId()))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        assertNotNull(order);
        assertNotNull(order.getCustomerName());
        assertEquals(Order.Status.PENDING, order.getStatus());
        assertEquals(Order.PaymentStatus.PENDING, order.getPaymentStatus());
        assertEquals(1, order.getItems().size());
        assertEquals(new BigDecimal("7.50"), order.getVatAmount());
        assertEquals(new BigDecimal("107.50"), order.getTotalCost());

    }

    Restaurant createRestaurant() {
        return Restaurant.withRequiredFields("Johnson Pancakes Restaurant", Restaurant.Type.CHAIN);
    }

    PlaceOrderUseCase.PlaceOrderCommand createPlaceOrderCommand(String restaurantId, String mealId) {
        return PlaceOrderUseCase.PlaceOrderCommand.builder()
                .restaurantId(restaurantId)
                .paymentChannel(PaymentChannel.CASH)
                .tableNumber("Table 4")
                .salesChannel(SalesChannel.DINE_IN)
                .customerName("Wale Ojo")
                .items(List.of(new PlaceOrderUseCase.PlaceOrderCommand.Item(mealId, 10)))
                .idempotencyKey(UUID.randomUUID().toString())
                .build();
    }



}
