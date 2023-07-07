package io.mobimenu.persistence.mapper;

import io.mobimenu.domain.Category;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import io.mobimenu.persistence.entity.CategoryEntity;

@Mapper(uses = {RestaurantMapper.class, UtilityMapper.class})
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category entityToDomainObject(CategoryEntity entity);

    @Mapping(target = "id", ignore = true)
    CategoryEntity domainObjectToPersistEntity(Category category);

    default Category.CategoryId entityIdToDomainId(ObjectId categoryId) {
        return new Category.CategoryId(categoryId.toString());
    }

    default ObjectId domainIdToEntityId(Category.CategoryId categoryId) {
        return new ObjectId(categoryId.categoryId());
    }
}
