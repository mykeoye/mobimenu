package io.mobimenu.payment.mappers;

import io.mobimenu.payment.processors.PaystackResponse;
import io.mobimenu.payment.processors.ProcessorResponse;

import java.math.BigDecimal;

public class ProcessorMapper {

    public static ProcessorResponse fromPaystack(PaystackResponse response) {
        var data = response.data();
        var authorisation = data.authorization();
        return new ProcessorResponse(data.status(), data.reference(), data.currency(), BigDecimal.valueOf(data.amount()),
                new ProcessorResponse.Card(authorisation.code(),
                        authorisation.bin(),
                        authorisation.cardType(),
                        authorisation.expMonth(),
                        authorisation.expYear(),
                        authorisation.brand(),
                        authorisation.lastFourDigits()));
    }
}
