package io.mobimenu.persistence.mapper;

import io.mobimenu.domain.Setting;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import io.mobimenu.persistence.entity.SettingEntity;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(uses = {RestaurantMapper.class, UtilityMapper.class})
public interface SettingMapper {

    SettingMapper INSTANCE = Mappers.getMapper(SettingMapper.class);

    @Mapping(target = "id", ignore = true)
    SettingEntity domainObjectToPersistEntity(Setting setting);

    @Mapping(target = "settingId", source = "id")
    Setting entityToDomainObject(SettingEntity entity);

    Setting.Preference prefEntityToPrefDomain(SettingEntity.Preference preference);

    SettingEntity.Preference prefDomainToPrefEntity(Setting.Preference preference);

    default Map<String, SettingEntity.Preference> toPrefEntityMap(Map<String, Setting.Preference> preferences) {
        if (preferences == null || preferences.isEmpty()) {
            return Map.of();
        }
        return preferences.values().stream().collect(Collectors.toMap((Setting.Preference::name), (this::prefDomainToPrefEntity)));
    }

    default Map<String, Setting.Preference> toPrefDomainMap(Map<String, SettingEntity.Preference> preferences) {
        if (preferences == null || preferences.isEmpty()) {
            return Map.of();
        }
        return preferences.values().stream().collect(Collectors.toMap((k -> k.name), (this::prefEntityToPrefDomain)));
    }

}
