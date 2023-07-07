package io.mobimenu.core.port.in.payment;

import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.enums.PaymentChannel;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

public interface InitiatePaymentUseCase {

    Uni<String> generateTransactionRef(GeneratePaymentRefCommand command);

    @Value
    @Builder
    @ToString
    class GeneratePaymentRefCommand {
        String restaurantId;
        String orderId;
        PaymentChannel paymentChannel;
    }

}
