package io.mobimenu.core.port.out.payment;

import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.Payment;
import java.util.Map;

public interface PaymentSaveOperationsPort {

    Uni<Payment> savePayment(Payment payment);

    Uni<Payment> updatePayment(Payment payment);

    Uni<Payment> updatePayment(Payment.PaymentId paymentId, Payment.Status status, Map<String, String> details);

}
