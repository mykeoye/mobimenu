package io.mobimenu.core.port.in.payment;

import io.mobimenu.domain.Payment;
import io.smallrye.mutiny.Uni;

public interface ReQueryPaymentUseCase {

    Uni<Payment.Status> requeryPayment(Payment.PaymentId paymentId);

}
