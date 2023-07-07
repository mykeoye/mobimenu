package io.mobimenu.core.port.out.setting;

import io.mobimenu.domain.Setting;
import io.mobimenu.domain.enums.SettingType;
import io.smallrye.mutiny.Uni;

public interface SettingQueryOperationsPort {

    Uni<Setting> getByTypeAndRestaurant(SettingType settingType, String restaurantId);

    Uni<Setting> getById(String settingId);

    Uni<Setting> getByIdAndRestaurant(String settingId, String restaurantId);
}
