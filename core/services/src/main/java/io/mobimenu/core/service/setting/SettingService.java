package io.mobimenu.core.service.setting;

import io.mobimenu.common.api.Code;
import io.mobimenu.common.api.Failure;
import io.mobimenu.core.port.in.setting.UpdateSettingUseCase;
import io.mobimenu.core.port.in.setting.ViewSettingUseCase;
import io.mobimenu.core.port.out.setting.SettingQueryOperationsPort;
import io.mobimenu.domain.enums.SettingType;
import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.Setting;
import lombok.RequiredArgsConstructor;
import io.mobimenu.core.port.out.setting.SettingSaveOperationsPort;

@RequiredArgsConstructor
public class SettingService implements UpdateSettingUseCase, ViewSettingUseCase {

    private final SettingSaveOperationsPort settingSaveOperationsPort;
    private final SettingQueryOperationsPort settingQueryOperationsPort;

    @Override
    public Uni<Setting> loadByTypeAndRestaurant(SettingType settingType, String restaurantId) {
        return settingQueryOperationsPort.getByTypeAndRestaurant(settingType, restaurantId);
    }

    @Override
    public Uni<Setting> updateSetting(UpdateSettingCommand command) {
        return settingQueryOperationsPort.getByTypeAndRestaurant(command.getType(), command.getRestaurantId())
                .onItem().ifNull().failWith(Failure.of(Code.SETTING_NOT_FOUND))
                .flatMap(setting -> settingSaveOperationsPort.updatePreference(
                        setting.settingId(),
                        command.getPreferenceName(),
                        command.isEnabled()));
    }
}
