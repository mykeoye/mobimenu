package io.mobimenu.web.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.mobimenu.core.port.in.setting.UpdateSettingUseCase;
import io.mobimenu.domain.enums.SettingType;

public record UpdateSettingRequest(

        @NotNull
        SettingType settingType,

        @NotBlank
        String restaurantId,

        @NotBlank
        String preferenceName,

        boolean enabled) {

    public UpdateSettingUseCase.UpdateSettingCommand toCommand() {
        return UpdateSettingUseCase.UpdateSettingCommand.builder()
                .type(settingType)
                .enabled(enabled)
                .preferenceName(preferenceName)
                .restaurantId(restaurantId)
                .build();
    }

}
