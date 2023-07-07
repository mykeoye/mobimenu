package io.mobimenu.web.request;

import io.mobimenu.domain.Order;
import javax.validation.constraints.NotBlank;
import io.mobimenu.core.port.in.order.UpdateOrderStatusUseCase;

public record UpdateOrderStatusRequest(

        @NotBlank
        String restaurantId,

        Order.Status status) {

    public UpdateOrderStatusUseCase.UpdateOrderStatusCommand toCommand() {
        return UpdateOrderStatusUseCase.UpdateOrderStatusCommand.builder()
                .restaurantId(restaurantId)
                .status(status)
                .build();
    }

}
