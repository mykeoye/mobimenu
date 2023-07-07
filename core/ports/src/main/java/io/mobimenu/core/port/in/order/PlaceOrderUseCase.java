package io.mobimenu.core.port.in.order;

import io.mobimenu.domain.Order;
import io.mobimenu.domain.enums.PaymentChannel;
import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.enums.SalesChannel;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface PlaceOrderUseCase {

    Uni<Order> placeOrder(PlaceOrderCommand command);

    @Value
    @Builder
    @ToString
    class PlaceOrderCommand {

        String idempotencyKey;

        String tableNumber;

        SalesChannel salesChannel;

        PaymentChannel paymentChannel;

        List<Item> items;

        String restaurantId;

        String customerId;

        String customerName;

        public record Item(String mealId, long quantity) { }

        public List<String> getMealIdFromItems() {
            return items.stream().map(Item::mealId).collect(Collectors.toList());
        }

        public Map<String, Item> getMapOfItems() {
            return items.stream().collect(Collectors.toMap(k -> k.mealId, v -> v));
        }
    }
}
