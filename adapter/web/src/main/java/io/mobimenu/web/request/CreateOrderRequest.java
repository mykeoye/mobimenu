package io.mobimenu.web.request;

import java.util.List;
import java.util.stream.Collectors;
import io.mobimenu.core.port.in.order.PlaceOrderUseCase;
import io.mobimenu.domain.enums.SalesChannel;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record CreateOrderRequest(

        String tableNumber,

        @NotNull
        SalesChannel salesChannel,

        String customerId,

        @NotBlank
        String customerName,

        @NotBlank
        String restaurantId,

        @NotEmpty
        List<Item> items) {

    public record Item(String mealId, long quantity) { }

    public PlaceOrderUseCase.PlaceOrderCommand toCommand(String idempotencyKey) {
        return PlaceOrderUseCase.PlaceOrderCommand.builder()
                .items(toDomainItems())
                .customerName(customerName)
                .customerId(customerId)
                .idempotencyKey(idempotencyKey)
                .tableNumber(tableNumber)
                .restaurantId(restaurantId)
                .customerId(customerId)
                .salesChannel(salesChannel)
                .build();
    }

    private List<PlaceOrderUseCase.PlaceOrderCommand.Item> toDomainItems() {
        return items.stream().map(item -> new PlaceOrderUseCase.PlaceOrderCommand.Item(item.mealId, item.quantity))
                .collect(Collectors.toList());
    }

}
