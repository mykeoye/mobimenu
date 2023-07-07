package io.mobimenu.core.port.in.order;

import io.mobimenu.domain.enums.PaymentChannel;
import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.Order;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;

public interface UpdateOrderStatusUseCase {

    Uni<Order.Status> updateOrderStatus(UpdateOrderStatusCommand command);

    Uni<Order.PaymentStatus> updatePaymentStatus(UpdatePaymentStatusCommand command);

    @Value
    @Builder
    @ToString
    @RequiredArgsConstructor
    class UpdateOrderStatusCommand {
        String orderId;
        String restaurantId;
        Order.Status status;
    }

    @Value
    @Builder
    @ToString
    @RequiredArgsConstructor
    class UpdatePaymentStatusCommand {
        String orderId;
        String restaurantId;
        Order.PaymentStatus paymentStatus;
        PaymentChannel paymentChannel;
    }

}
