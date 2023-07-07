package io.mobimenu.persistence;

import io.mobimenu.core.port.out.setting.SettingSaveOperationsPort;
import io.mobimenu.domain.Setting;
import io.mobimenu.persistence.mapper.SettingMapper;
import io.mobimenu.persistence.repository.SettingRepository;
import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import java.time.Instant;

@RequiredArgsConstructor
public class SettingPersistenceAdapter implements SettingSaveOperationsPort {

    private final SettingRepository repository;
    private final SettingMapper mapper = SettingMapper.INSTANCE;

    @Override
    public Uni<Setting> updatePreference(String settingId, String preferenceName, Boolean enabled) {
        return repository.findById(new ObjectId(settingId))
                .map(entity -> {
                    entity.preferences.computeIfPresent(preferenceName, (k, v) -> {
                        v.enabled = enabled;
                        return v;
                    });
                    entity.updated = Instant.now();
                    return entity;
                })
                .flatMap(repository::update)
                .map(mapper::entityToDomainObject);
    }

}
