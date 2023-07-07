package io.mobimenu.domain;

import io.mobimenu.domain.enums.PaymentChannel;
import io.mobimenu.domain.enums.PaymentType;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import java.util.HashMap;
import java.util.Map;
import io.mobimenu.domain.enums.PaymentProcessor;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * Represents a payment on the system, a payment made through any of the system's supported payment channels
 */
@Value
@Builder
@ToString
public class Payment {

    /**
     * Unique identifier for the payment
     */
    @JsonUnwrapped
    PaymentId id;

    /**
     * Unique payment reference, this is generated before the payment is processed. This only
     * applies to online payments made via card. For payments made via CASH, POS or BANK TRANSFER
     * this field will be generated after the payment status on the order has been updated
     */
    String paymentRef;

    /**
     * The id of the restaurant where this payment originated from
     */
    String restaurantId;

    /**
     * The id of the order associated with this payment
     */
    String orderId;

    /**
     * The payment processor used
     */
    PaymentProcessor paymentProcessor;

    /**
     * The status of the payment
     */
    @Builder.Default
    Status status = Status.PENDING;

    /**
     * The payment channel, could be bank, card, pos etc
     */
    PaymentChannel paymentChannel;

    /**
     * The type of payment being made, either subscription or customer bill
     */
    PaymentType paymentType;

    /**
     * Useful transaction details from the payment processor
     */
    @Builder.Default
    Map<String, String> processorDetails = new HashMap<>();

    /**
     * Unique identifier for the transaction
     */
    public record PaymentId(String paymentId) {}

    /**
     * Represents the different states a payment transitions through
     */
    public enum Status {
        PENDING, SUCCESSFUL, FAILED
    }

    /**
     * Creates a payment object with the required fields
     *
     * @param paymentRef            Unique payment reference, this is generated before the payment is processed
     * @param restaurantId          The id of the restaurant where this payment originated from
     * @param orderId               The id of the order associated with this payment
     * @param paymentChannel        The payment channel, could be bank, card, pos etc
     * @param paymentType           the type of payment made either bill or subscription
     * @return                      The payment object with all required fields
     */
    public static Payment withRequiredFields(
            String paymentRef,
            String restaurantId,
            String orderId,
            PaymentChannel paymentChannel,
            PaymentType paymentType,
            Status status) {

        return Payment.builder()
                .paymentRef(paymentRef)
                .restaurantId(restaurantId)
                .orderId(orderId)
                .paymentChannel(paymentChannel)
                .paymentType(paymentType)
                .status(status)
                .build();
    }

    /**
     * Creates a payment object with all the fields
     *
     * @param id                    Unique identifier for the payment
     * @param paymentRef            Unique payment reference, this is generated before the payment is processed
     * @param restaurantId          The id of the restaurant where this payment originated from
     * @param orderId               The id of the order associated with this payment
     * @param paymentChannel        The payment channel, could be bank, card, pos etc
     * @return                      The payment object with all required fields
     */
    public static Payment withAllFields(
            PaymentId id,
            String paymentRef,
            String restaurantId,
            String orderId,
            Status status,
            PaymentChannel paymentChannel,
            PaymentType paymentType) {

        return Payment.builder()
                .id(id)
                .paymentRef(paymentRef)
                .restaurantId(restaurantId)
                .orderId(orderId)
                .status(status)
                .paymentChannel(paymentChannel)
                .paymentType(paymentType)
                .build();
    }

}
