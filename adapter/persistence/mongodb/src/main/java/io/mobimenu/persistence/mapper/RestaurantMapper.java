package io.mobimenu.persistence.mapper;

import io.mobimenu.domain.Restaurant;
import io.mobimenu.persistence.entity.RestaurantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = { UserMapper.class, UtilityMapper.class, ThemeMapper.class })
public interface RestaurantMapper {

    RestaurantMapper INSTANCE = Mappers.getMapper(RestaurantMapper.class);

    @Mapping(target = "restaurantId", source = "id")
    Restaurant entityToDomainObject(RestaurantEntity entity);

    @Mapping(target = "ownerId", source = "userId")
    @Mapping(target = "id", ignore = true)
    RestaurantEntity domainObjectToPersistEntity(Restaurant restaurant, String userId);

}
