package io.mobimenu.persistence.mapper;

import java.util.Set;

import io.mobimenu.domain.OrderItem;
import io.mobimenu.persistence.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {OrderMapper.class})
public interface OrderItemMapper {

    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

    OrderItem entityToDomainObject(OrderEntity.OrderItem entity);

    OrderEntity.OrderItem domainObjectToPersistEntity(OrderItem item);

    Set<OrderEntity.OrderItem> domainObjectsToPersistentEntity(Set<OrderItem> items);

    Set<OrderItem> entitiesToDomainObjects(Set<OrderEntity.OrderItem> entities);

}
