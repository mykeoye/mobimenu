package io.mobimenu.core.service.context;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mobimenu.payment.PaymentProcessor;
import io.mobimenu.common.http.WebClient;
import io.mobimenu.core.port.in.payment.InitiatePaymentUseCase;
import io.mobimenu.core.port.in.payment.ReQueryPaymentUseCase;
import io.mobimenu.core.port.in.payment.VerifyPaymentUseCase;
import io.mobimenu.core.port.out.order.OrderQueryOperationsPort;
import io.mobimenu.core.port.out.order.OrderSaveOperationsPort;
import io.mobimenu.core.port.out.payment.PaymentQueryOperationsPort;
import io.mobimenu.core.port.out.payment.PaymentSaveOperationsPort;
import io.mobimenu.core.port.out.restaurant.RestaurantQueryOperationsPort;
import io.mobimenu.core.service.payment.PaymentService;
import io.mobimenu.persistence.PaymentPersistenceAdapter;
import io.mobimenu.persistence.PaymentQueryAdapter;
import io.mobimenu.persistence.repository.PaymentRepository;
import io.mobimenu.payment.processors.PaystackPaymentProcessor;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import java.util.concurrent.Executors;

@ApplicationScoped
public class PaymentTestContext {

    @Produces
    @Singleton
    public PaymentSaveOperationsPort paymentSaveOperationsPort(PaymentRepository repository) {
        return new PaymentPersistenceAdapter(repository);
    }

    @Produces
    @Singleton
    public PaymentQueryOperationsPort paymentQueryOperationsPort(PaymentRepository repository) {
        return new PaymentQueryAdapter(repository);
    }

    @Produces
    @Singleton
    public InitiatePaymentUseCase initiatePaymentUseCase(RestaurantQueryOperationsPort restaurantQueryOperationsPort,
                                                         OrderQueryOperationsPort orderQueryOperationsPort,
                                                         PaymentSaveOperationsPort paymentSaveOperationsPort,
                                                         PaymentQueryOperationsPort paymentQueryOperationsPort,
                                                         PaymentProcessor paymentProcessor,
                                                         OrderSaveOperationsPort orderSaveOperationsPort) {
        return paymentService(restaurantQueryOperationsPort, orderQueryOperationsPort, paymentSaveOperationsPort, paymentQueryOperationsPort, paymentProcessor, orderSaveOperationsPort);
    }

    @Produces
    @Singleton
    public ReQueryPaymentUseCase reQueryPaymentUseCase(RestaurantQueryOperationsPort restaurantQueryOperationsPort,
                                                       OrderQueryOperationsPort orderQueryOperationsPort,
                                                       PaymentSaveOperationsPort paymentSaveOperationsPort,
                                                       PaymentQueryOperationsPort paymentQueryOperationsPort,
                                                       PaymentProcessor paymentProcessor,
                                                       OrderSaveOperationsPort orderSaveOperationsPort) {
        return paymentService(restaurantQueryOperationsPort, orderQueryOperationsPort, paymentSaveOperationsPort, paymentQueryOperationsPort, paymentProcessor, orderSaveOperationsPort);
    }

    @Produces
    @Singleton
    public VerifyPaymentUseCase verifyPaymentUseCase(RestaurantQueryOperationsPort restaurantQueryOperationsPort,
                                                     OrderQueryOperationsPort orderQueryOperationsPort,
                                                     PaymentSaveOperationsPort paymentSaveOperationsPort,
                                                     PaymentQueryOperationsPort paymentQueryOperationsPort,
                                                     PaymentProcessor paymentProcessor,
                                                     OrderSaveOperationsPort orderSaveOperationsPort) {
        return paymentService(restaurantQueryOperationsPort, orderQueryOperationsPort, paymentSaveOperationsPort, paymentQueryOperationsPort, paymentProcessor, orderSaveOperationsPort);
    }

    PaymentService paymentService(RestaurantQueryOperationsPort restaurantQueryOperationsPort,
                                  OrderQueryOperationsPort orderQueryOperationsPort,
                                  PaymentSaveOperationsPort paymentSaveOperationsPort,
                                  PaymentQueryOperationsPort paymentQueryOperationsPort,
                                  PaymentProcessor paymentProcessor,
                                  OrderSaveOperationsPort orderSaveOperationsPort) {
        return new PaymentService(restaurantQueryOperationsPort, paymentQueryOperationsPort, paymentSaveOperationsPort, orderQueryOperationsPort, paymentProcessor, orderSaveOperationsPort);
    }

    @Singleton
    @Produces
    PaymentProcessor paymentProcessor(WebClient webClient,
                                      ObjectMapper mapper,
                                      @ConfigProperty(name = "paystack.transaction.verification.url") String verificationUri,
                                      @ConfigProperty(name = "paystack.secret.key") String secretKey) {
        return new PaystackPaymentProcessor(verificationUri, secretKey, webClient, mapper);
    }

    @Produces
    public WebClient webClient(@ConfigProperty(name = "http.client.timeout.secs", defaultValue = "60") int timeout,
                               @ConfigProperty(name = "http.client.thread.pool.size", defaultValue = "4") int threadPoolSize) {
        return new WebClient(Executors.newFixedThreadPool(threadPoolSize), timeout);
    }

    @Produces
    public ObjectMapper objectMapper() {
        var mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

}
