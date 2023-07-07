package io.mobimenu.persistence.entity;

import io.mobimenu.domain.enums.PaymentType;
import io.quarkus.mongodb.panache.common.MongoEntity;
import io.mobimenu.domain.Payment;
import io.mobimenu.domain.enums.PaymentChannel;
import io.mobimenu.domain.enums.PaymentProcessor;
import lombok.ToString;
import org.bson.types.ObjectId;
import java.util.HashMap;
import java.util.Map;

@ToString
@MongoEntity(collection = "payments")
public class PaymentEntity extends BaseEntity {
    public String paymentRef;
    public ObjectId restaurantId;
    public ObjectId orderId;
    public PaymentProcessor paymentProcessor;
    public Payment.Status status;
    public PaymentChannel paymentChannel;
    public PaymentType paymentType;
    public Map<String, String> processorDetails = new HashMap<>();
}
