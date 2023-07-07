package io.mobimenu.core.service.menu;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.mobimenu.common.api.Code;
import io.mobimenu.common.api.Failure;
import io.mobimenu.common.api.Page;
import io.mobimenu.core.port.in.menu.CreateMenuUseCase;
import io.mobimenu.core.port.in.menu.ViewMenusUseCase;
import io.mobimenu.core.port.out.menu.MenuQueryOperationsPort;
import io.mobimenu.core.port.out.menu.MenuSaveOperationsPort;
import io.mobimenu.core.port.out.restaurant.RestaurantQueryOperationsPort;
import io.mobimenu.domain.Menu;
import io.mobimenu.domain.Theme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class MenuService implements CreateMenuUseCase, ViewMenusUseCase {

    private final MenuQueryOperationsPort menuQueryOperationsPort;
    private final MenuSaveOperationsPort menuSaveOperationsPort;
    private final RestaurantQueryOperationsPort restaurantQueryOperationsPort;

    @Override
    public Uni<Menu> createMenu(CreateMenuCommand command) {
        var restaurantId = command.getRestaurantId();
        return restaurantQueryOperationsPort.getById(restaurantId)
                .onItem().ifNull().failWith(Failure.of(Code.RESTAURANT_NOT_FOUND))
                .flatMap(ignored -> menuQueryOperationsPort.getByNameAndRestaurant(command.getName(), restaurantId))
                .onItem().ifNotNull().failWith(Failure.of(Code.MENU_EXISTS))
                .map(ignored -> new Theme("%s-theme".formatted(
                        command.getName().trim().toLowerCase()),
                        command.getThemePrimaryColor(),
                        command.getThemeAccentColor(),
                        command.getThemeBackgroundImage()))
                .map(theme -> Menu.withRequiredFields(
                        command.getName(),
                        command.getActive() ? Menu.Status.ACTIVE: Menu.Status.INACTIVE,
                        restaurantId,
                        theme))
                .flatMap(menuSaveOperationsPort::saveMenu);
    }

    @Override
    public Uni<Tuple2<Long, List<Menu>>> loadMenusByRestaurant(String restaurantId, Page page) {
        return menuQueryOperationsPort.getAllByRestaurantId(restaurantId, page);
    }

}
