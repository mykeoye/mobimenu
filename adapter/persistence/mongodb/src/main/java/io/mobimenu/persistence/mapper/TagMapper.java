package io.mobimenu.persistence.mapper;

import io.mobimenu.domain.Tag;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import io.mobimenu.persistence.entity.TagEntity;

@Mapper(uses = { RestaurantMapper.class, UtilityMapper.class })
public interface TagMapper {

    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    Tag entityToDomainObject(TagEntity entity);

    @Mapping(target = "id", ignore = true)
    TagEntity domainObjectToPersistEntity(Tag tag);

    default Tag.TagId entityIdToDomainId(ObjectId tagId) {
        return new Tag.TagId(tagId.toString());
    }

    default ObjectId domainIdToEntityId(Tag.TagId tagId) {
        return new ObjectId(tagId.tagId());
    }
}
