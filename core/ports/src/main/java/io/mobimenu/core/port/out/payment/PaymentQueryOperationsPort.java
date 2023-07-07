package io.mobimenu.core.port.out.payment;

import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.Payment;

public interface PaymentQueryOperationsPort {

    Uni<Payment> getByOrderAndRestaurant(String orderId, String restaurantId);

    Uni<Payment> getByPaymentRef(String paymentRef);

    Uni<Payment> getById(Payment.PaymentId paymentId);

    Uni<Payment> getByPaymentRefAndRestaurant(String reference, String restaurantId);

}
