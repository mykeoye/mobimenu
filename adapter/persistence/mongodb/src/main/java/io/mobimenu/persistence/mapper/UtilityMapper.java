package io.mobimenu.persistence.mapper;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper
public interface UtilityMapper {

    UtilityMapper INSTANCE = Mappers.getMapper(UtilityMapper.class);

    default String entityIdToDomainId(ObjectId objectId) {
        if (objectId == null) {
            return null;
        }
        return objectId.toString();
    }

    default ObjectId domainIdToEntityId(String id) {
        if (id == null) {
            return null;
        }
        return new ObjectId(id);
    }

    Set<String> entityIdsToDomainIds(Set<ObjectId> entityIds);

    Set<ObjectId> domainIdsToEntityIds(Set<String> domainIds);
}
