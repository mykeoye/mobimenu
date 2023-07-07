package io.mobimenu.payment.processors;

import io.mobimenu.payment.PaymentProcessor;
import java.math.BigDecimal;

/**
 * This record servers as a generic data object for all payment processors, regardless of the
 * data structure returned from the provider, implementors of the {@link PaymentProcessor}
 * interface must conform their data to this structure
 *
 * @param status        indicates the status of the transaction - SUCCESSFUL or FAILED
 * @param reference     the unique transaction reference
 * @param currency      the currency the payment was made in
 * @param amount        the paid amount
 * @param card          card details if provided
 */
public record ProcessorResponse(String status,
                                String reference,
                                String currency,
                                BigDecimal amount,
                                Card card) {

    /**
     * Holds just enough information needed to charge a customer's card. This isn't the
     * actual card info, just a subset that the payment processor understands and that
     * can be used to make recurring charges on the customer's behalf
     *
     * @param authorisation     token sent to the payment processor to initiate a payment
     * @param bin               the card bin
     * @param type              the card type
     * @param expiryMonth       month of expiry as indicated on the card
     * @param expiryYear        year of expiry as indicated on the card
     * @param brand             the card brand (visa, mastercard) etc
     * @param lastFourDigits    the last four digits on the card - strictly for display purposes
     */
    public record Card(String authorisation,
                       String bin,
                       String type,
                       String expiryMonth,
                       String expiryYear,
                       String brand,
                       String lastFourDigits) {}

    public boolean isTransactionSuccessful() {
        return "success".equalsIgnoreCase(status);
    }
}
