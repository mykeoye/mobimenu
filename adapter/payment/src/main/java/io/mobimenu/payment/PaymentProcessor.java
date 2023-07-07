package io.mobimenu.payment;

import io.mobimenu.payment.processors.ProcessorResponse;
import io.smallrye.mutiny.Uni;

/**
 * Captures all operations that can be carried out by a payment processor
 */
public interface PaymentProcessor {

    /**
     * Queries the payment processor for the given transaction reference
     *
     * @param reference    the payment reference to query
     * @return             an object containing information about the payment
     */
    Uni<ProcessorResponse> queryTransaction(String reference);

}
