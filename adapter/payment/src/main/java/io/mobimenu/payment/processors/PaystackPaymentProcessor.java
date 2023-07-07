package io.mobimenu.payment.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mobimenu.payment.PaymentProcessor;
import io.mobimenu.common.http.WebClient;
import io.mobimenu.payment.mappers.ProcessorMapper;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class PaystackPaymentProcessor implements PaymentProcessor {

    private final String verificationUri;
    private final String secretKey;
    private final WebClient client;
    private final ObjectMapper objectMapper;

    @Override
    public Uni<ProcessorResponse> queryTransaction(String reference) {
        var uri = "%s/%s".formatted(verificationUri, reference);
        log.info("Verifying payment made to PAYSTACK with reference: {}", reference);
        return client.get(uri, Map.of("Authorization", "Bearer %s".formatted(secretKey)))
                .map(Unchecked.function((response) -> objectMapper.readValue(response.body(), PaystackResponse.class)))
                .map(ProcessorMapper::fromPaystack)
                .log();
    }

}
