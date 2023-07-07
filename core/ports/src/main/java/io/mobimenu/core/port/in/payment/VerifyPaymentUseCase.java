package io.mobimenu.core.port.in.payment;

import io.mobimenu.domain.Payment;
import io.smallrye.mutiny.Uni;

public interface VerifyPaymentUseCase {

    Uni<Payment.Status> verifyPayment(Payment.PaymentId paymentId);

}
