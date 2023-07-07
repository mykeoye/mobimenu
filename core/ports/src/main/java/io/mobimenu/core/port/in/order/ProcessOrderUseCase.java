package io.mobimenu.core.port.in.order;

import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.Order;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;

public interface ProcessOrderUseCase {

    Uni<Order> processOrder(ProcessOrderCommand command);

    @Value
    @Builder
    @ToString
    @RequiredArgsConstructor
    class ProcessOrderCommand {
        String orderId;
        String restaurantId;
        Order.Status status;
        String reason;
        Long waitTimeInMinutes;
    }

}
