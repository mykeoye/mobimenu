package io.mobimenu.web.request;

import io.mobimenu.domain.Order;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.mobimenu.core.port.in.order.UpdateOrderStatusUseCase;
import io.mobimenu.domain.enums.PaymentChannel;

public record UpdatePaymentRequest(

        @NotBlank
        String restaurantId,

        @NotNull
        PaymentChannel paymentChannel,

        @NotNull
        Order.PaymentStatus status) {

    public UpdateOrderStatusUseCase.UpdatePaymentStatusCommand toCommand() {
        return UpdateOrderStatusUseCase.UpdatePaymentStatusCommand.builder()
                .restaurantId(restaurantId)
                .paymentChannel(paymentChannel)
                .paymentStatus(status)
                .build();
    }

}
