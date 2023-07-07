package io.mobimenu.persistence.repository;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import io.mobimenu.persistence.common.Fields;
import io.mobimenu.persistence.entity.PaymentEntity;
import org.bson.types.ObjectId;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PaymentRepository implements ReactivePanacheMongoRepository<PaymentEntity> {

    public Uni<PaymentEntity> findByOrderAndRestaurant(String orderId, String restaurantId) {
        return find("%s = :orderId and %s = :restaurantId".formatted(Fields.ORDER_ID, Fields.RESTAURANT_ID),
                Parameters.with(Fields.ORDER_ID, new ObjectId(orderId)).and(Fields.RESTAURANT_ID, new ObjectId(restaurantId))).firstResult();
    }

    public Uni<PaymentEntity> findByPaymentRef(String paymentRef) {
        return find("%s = :paymentRef".formatted(Fields.PAYMENT_REF),
                Parameters.with(Fields.PAYMENT_REF, paymentRef)).firstResult();
    }

    public Uni<PaymentEntity> findByPaymentRefAndRestaurant(String paymentRef, String restaurantId) {
        return find("%s = :paymentRef and %s = :restaurantId".formatted(Fields.PAYMENT_REF, Fields.RESTAURANT_ID),
                Parameters.with(Fields.PAYMENT_REF, paymentRef).and(Fields.RESTAURANT_ID, new ObjectId(restaurantId))).firstResult();
    }

}
