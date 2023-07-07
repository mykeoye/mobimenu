package io.mobimenu.web.request;

import io.mobimenu.core.port.in.order.ProcessOrderUseCase;
import io.mobimenu.domain.Order;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

public record ProcessOrderRequest(

        @NotBlank
        String restaurantId,

        Order.Status status,

        String reason,

        Long waitTimeInMinutes) {

    public ProcessOrderUseCase.ProcessOrderCommand toCommand(String orderId) {
        return ProcessOrderUseCase.ProcessOrderCommand.builder()
                .orderId(orderId)
                .restaurantId(restaurantId)
                .status(status)
                .reason(reason)
                .waitTimeInMinutes((Objects.isNull(waitTimeInMinutes) || waitTimeInMinutes <= 0) ? 30 : waitTimeInMinutes)
                .build();
    }
}
