package io.mobimenu.persistence;

import io.smallrye.mutiny.Uni;
import io.mobimenu.core.port.out.payment.PaymentSaveOperationsPort;
import io.mobimenu.domain.Payment;
import io.mobimenu.persistence.mapper.PaymentMapper;
import io.mobimenu.persistence.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import java.time.Instant;
import java.util.Map;

@RequiredArgsConstructor
public class PaymentPersistenceAdapter implements PaymentSaveOperationsPort {

    private final PaymentRepository repository;
    private final PaymentMapper mapper = PaymentMapper.INSTANCE;

    @Override
    public Uni<Payment> savePayment(Payment payment) {
        return repository.persist(mapper.domainObjectToPersistEntity(payment))
                .map(mapper::entityToDomainObject);
    }

    @Override
    public Uni<Payment> updatePayment(Payment payment) {
        return repository.findById(new ObjectId(payment.getId().paymentId()))
                .map(entity -> {
                    var details = payment.getProcessorDetails();
                    if (details != null && !details.isEmpty()) {
                        entity.processorDetails = details;
                    }
                    entity.status = payment.getStatus();
                    entity.paymentType = payment.getPaymentType();
                    entity.updated = Instant.now();
                    return entity;
                }).map(mapper::entityToDomainObject);
    }

    @Override
    public Uni<Payment> updatePayment(Payment.PaymentId paymentId, Payment.Status status, Map<String, String> details) {
        return repository.findById(new ObjectId(paymentId.paymentId()))
                .map(entity -> {
                    if (details != null && !details.isEmpty()) {
                        entity.processorDetails = details;
                    }
                    entity.status = status;
                    entity.updated = Instant.now();
                    return entity;
                }).map(mapper::entityToDomainObject);
    }

}
