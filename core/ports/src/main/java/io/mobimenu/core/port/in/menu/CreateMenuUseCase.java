package io.mobimenu.core.port.in.menu;

import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.Menu;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

public interface CreateMenuUseCase {

    Uni<Menu> createMenu(CreateMenuCommand command);

    @Builder
    @Value
    @ToString
    class CreateMenuCommand  {
        String name;
        Boolean active;
        String themePrimaryColor;
        String themeAccentColor;
        String themeBackgroundImage;
        String restaurantId;
        String outletId;
    }

}
