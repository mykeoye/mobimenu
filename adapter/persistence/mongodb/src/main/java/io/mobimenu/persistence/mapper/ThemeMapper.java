package io.mobimenu.persistence.mapper;

import io.mobimenu.domain.Theme;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import io.mobimenu.persistence.entity.ThemeEntity;

@Mapper
public interface ThemeMapper {

    ThemeMapper INSTANCE = Mappers.getMapper(ThemeMapper.class);

    @Mapping(target = "id", ignore = true)
    ThemeEntity domainObjectToPersistEntity(Theme theme);

    Theme entityToDomainObject(ThemeEntity entity);

    default ObjectId domainIdToEntityId(Theme.ThemeId themeId) {
        if (themeId == null) {
            return null;
        }
        return new ObjectId(themeId.themeId());
    }

    default Theme.ThemeId entityIdToDomainId(ObjectId themeId) {
        if (themeId == null) {
            return null;
        }
        return new Theme.ThemeId(themeId.toString());
    }

}
