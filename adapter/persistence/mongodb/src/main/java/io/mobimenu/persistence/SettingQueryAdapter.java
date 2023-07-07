package io.mobimenu.persistence;

import io.mobimenu.core.port.out.setting.SettingQueryOperationsPort;
import io.mobimenu.domain.Setting;
import io.mobimenu.domain.enums.SettingType;
import io.mobimenu.persistence.mapper.SettingMapper;
import io.mobimenu.persistence.repository.SettingRepository;
import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;

@RequiredArgsConstructor
public class SettingQueryAdapter implements SettingQueryOperationsPort {

    private final SettingRepository repository;
    private final SettingMapper mapper = SettingMapper.INSTANCE;

    @Override
    public Uni<Setting> getByTypeAndRestaurant(SettingType settingType, String restaurantId) {
        return repository.findByTypeAndRestaurant(settingType, restaurantId).map(mapper::entityToDomainObject);
    }

    @Override
    public Uni<Setting> getById(String settingId) {
        return repository.findById(new ObjectId(settingId)).map(mapper::entityToDomainObject);
    }

    @Override
    public Uni<Setting> getByIdAndRestaurant(String settingId, String restaurantId) {
        return repository.findByIdAndRestaurant(settingId, restaurantId).map(mapper::entityToDomainObject);
    }

}
