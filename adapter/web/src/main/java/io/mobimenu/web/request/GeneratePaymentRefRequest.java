package io.mobimenu.web.request;

import io.mobimenu.core.port.in.payment.InitiatePaymentUseCase;
import io.mobimenu.domain.enums.PaymentChannel;

import javax.validation.constraints.NotBlank;

public record GeneratePaymentRefRequest(

        @NotBlank
        String restaurantId,

        @NotBlank
        String orderId,

        PaymentChannel paymentChannel) {

    public InitiatePaymentUseCase.GeneratePaymentRefCommand toCommand() {
        return InitiatePaymentUseCase.GeneratePaymentRefCommand.builder()
                .orderId(orderId)
                .restaurantId(restaurantId)
                .paymentChannel(paymentChannel)
                .build();
    }

}
