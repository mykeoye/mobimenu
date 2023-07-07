package io.mobimenu.payment.processors;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Full response body can be found here
 * - https://paystack.com/docs/api/#transaction-verify
 */
public record PaystackResponse(boolean status, String message, Data data) {

    public record Data(
            String status,
            String reference,
            double amount,
            String channel,
            String currency,
            Authorization authorization
    ) {}

    public record Authorization(
            @JsonProperty("authorization_code") String code,
            String bin,
            @JsonProperty("last4") String lastFourDigits,
            @JsonProperty("exp_month") String expMonth,
            @JsonProperty("exp_year") String expYear,
            String channel,
            @JsonProperty("card_type") String cardType,
            String bank,
            @JsonProperty("country_code") String countryCode,
            String brand,
            boolean reusable,
            String signature,
            String accountName
    ) {}

    public boolean isTransactionSuccessful() {
        return "success".equalsIgnoreCase(data().status());
    }
}
