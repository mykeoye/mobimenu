package io.mobimenu.persistence.mapper;

import io.mobimenu.domain.Payment;
import io.mobimenu.persistence.entity.PaymentEntity;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {RestaurantMapper.class, OrderMapper.class, UtilityMapper.class})
public interface PaymentMapper {

    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    Payment entityToDomainObject(PaymentEntity entity);

    @Mapping(target = "id", ignore = true)
    PaymentEntity domainObjectToPersistEntity(Payment payment);

    default Payment.PaymentId entityIdToDomainId(ObjectId paymentId) {
        return new Payment.PaymentId(paymentId.toString());
    }

    default ObjectId domainIdToEntityId(Payment.PaymentId paymentId) {
        return new ObjectId(paymentId.paymentId());
    }

}
