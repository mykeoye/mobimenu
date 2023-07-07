package io.mobimenu.web.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import io.mobimenu.core.port.in.menu.CreateMenuUseCase;

public record CreateMenuRequest(

        @NotBlank
        String name,

        @NotNull
        Boolean active,

        @NotBlank
        String themePrimaryColor,

        @NotBlank
        String themeAccentColor,

        @NotBlank
        String themeBackgroundImageURI,

        @NotBlank
        String restaurantId,

        String outletId) {

    public CreateMenuUseCase.CreateMenuCommand toCommand() {
        return CreateMenuUseCase.CreateMenuCommand.builder()
                .name(name)
                .active(active)
                .restaurantId(restaurantId)
                .outletId(outletId)
                .themeAccentColor(themeAccentColor)
                .themePrimaryColor(themePrimaryColor)
                .themeBackgroundImage(themeBackgroundImageURI)
                .build();
    }

}
