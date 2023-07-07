package io.mobimenu.core.port.out.setting;

import io.mobimenu.domain.Setting;
import io.smallrye.mutiny.Uni;

public interface SettingSaveOperationsPort {

    Uni<Setting> updatePreference(String settingId, String preferenceName, Boolean enabled);

}
