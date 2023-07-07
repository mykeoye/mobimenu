package io.mobimenu.core.port.in.setting;

import io.mobimenu.domain.Setting;
import io.mobimenu.domain.enums.SettingType;
import io.smallrye.mutiny.Uni;

public interface ViewSettingUseCase {

    Uni<Setting> loadByTypeAndRestaurant(SettingType settingType, String restaurantId);

}
