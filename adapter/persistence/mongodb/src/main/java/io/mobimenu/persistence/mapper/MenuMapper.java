package io.mobimenu.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import io.mobimenu.domain.Menu;
import io.mobimenu.persistence.entity.MenuEntity;

@Mapper(uses = {RestaurantMapper.class, UtilityMapper.class, ThemeMapper.class})
public interface MenuMapper {

    MenuMapper INSTANCE = Mappers.getMapper(MenuMapper.class);

    @Mapping(target = "menuId", source = "id")
    Menu entityToDomainObject(MenuEntity entity);

    @Mapping(target = "id", ignore = true)
    MenuEntity domainObjectToPersistEntity(Menu menu);

}
