package io.mobimenu.persistence.mapper;

import io.mobimenu.domain.Meal;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import io.mobimenu.persistence.entity.MealEntity;

import java.util.List;

@Mapper(uses = {CategoryMapper.class, MenuMapper.class, RestaurantMapper.class, UtilityMapper.class})
public interface MealMapper {

    MealMapper INSTANCE = Mappers.getMapper(MealMapper.class);

    Meal entityToDomainObject(MealEntity entity);

    @Mapping(target = "id", ignore = true)
    MealEntity domainObjectToPersistEntity(Meal meal);

    List<MealEntity> domainObjectsToPersistentEntity(List<Meal> meals);

    List<Meal> entitiesToDomainObjects(List<MealEntity> entities);

    default Meal.MealId entityIdToDomainId(ObjectId mealId) {
        return new Meal.MealId(mealId.toString());
    }

    default ObjectId domainIdToEntityId(Meal.MealId mealId) {
        return new ObjectId(mealId.mealId());
    }

}
