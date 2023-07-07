package io.mobimenu.persistence;

import io.smallrye.mutiny.Uni;
import io.mobimenu.core.port.out.payment.PaymentQueryOperationsPort;
import io.mobimenu.domain.Payment;
import io.mobimenu.persistence.mapper.PaymentMapper;
import io.mobimenu.persistence.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;

@RequiredArgsConstructor
public class PaymentQueryAdapter implements PaymentQueryOperationsPort {

    private final PaymentMapper mapper = PaymentMapper.INSTANCE;
    private final PaymentRepository repository;

    @Override
    public Uni<Payment> getByOrderAndRestaurant(String orderId, String restaurantId) {
        return repository.findByOrderAndRestaurant(orderId, restaurantId)
                .map(mapper::entityToDomainObject);
    }

    @Override
    public Uni<Payment> getByPaymentRef(String paymentRef) {
        return repository.findByPaymentRef(paymentRef).map(mapper::entityToDomainObject);
    }

    @Override
    public Uni<Payment> getById(Payment.PaymentId paymentId) {
        return repository.findById(new ObjectId(paymentId.paymentId())).map(mapper::entityToDomainObject);
    }

    @Override
    public Uni<Payment> getByPaymentRefAndRestaurant(String paymentRef, String restaurantId) {
        return repository.findByPaymentRefAndRestaurant(paymentRef, restaurantId).map(mapper::entityToDomainObject);
    }

}
