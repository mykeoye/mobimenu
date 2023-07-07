package io.mobimenu.persistence.mapper;

import io.mobimenu.domain.Order;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import io.mobimenu.persistence.entity.OrderEntity;

@Mapper(uses = {RestaurantMapper.class, OrderItemMapper.class, UtilityMapper.class, UserMapper.class})
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(source = "id", target = "orderId")
    Order entityToDomainObject(OrderEntity entity);

    @Mapping(target = "id", ignore = true)
    OrderEntity domainObjectToPersistEntity(Order order);

    List<OrderEntity> domainObjectsToPersistentEntity(List<Order> orders);

    List<Order> entitiesToDomainObjects(List<OrderEntity> entities);

}
