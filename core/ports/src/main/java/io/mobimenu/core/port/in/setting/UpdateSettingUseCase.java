package io.mobimenu.core.port.in.setting;

import io.mobimenu.domain.Setting;
import io.mobimenu.domain.enums.SettingType;
import io.smallrye.mutiny.Uni;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

public interface UpdateSettingUseCase {

    Uni<Setting> updateSetting(UpdateSettingCommand command);

    @Value
    @Builder
    @ToString
    class UpdateSettingCommand {
        SettingType type;
        String restaurantId;
        String preferenceName;
        boolean enabled;
    }
}
