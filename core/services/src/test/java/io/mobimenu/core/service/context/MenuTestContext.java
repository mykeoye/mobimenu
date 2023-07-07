package io.mobimenu.core.service.context;

import io.mobimenu.core.port.in.menu.CreateMenuUseCase;
import io.mobimenu.core.port.in.menu.ViewMenusUseCase;
import io.mobimenu.core.port.out.menu.MenuQueryOperationsPort;
import io.mobimenu.core.port.out.menu.MenuSaveOperationsPort;
import io.mobimenu.core.port.out.restaurant.RestaurantQueryOperationsPort;
import io.mobimenu.core.service.menu.MenuService;
import io.mobimenu.persistence.MenuPersistenceAdapter;
import io.mobimenu.persistence.MenuQueryAdapter;
import io.mobimenu.persistence.repository.MenuRepository;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@ApplicationScoped
public class MenuTestContext {

    @Produces
    @Singleton
    public MenuQueryOperationsPort menuQueryOperationsPort(MenuRepository menuRepository) {
        return new MenuQueryAdapter(menuRepository);
    }

    @Produces
    @Singleton
    public MenuSaveOperationsPort menuSaveOperationsPort(MenuRepository menuRepository) {
        return new MenuPersistenceAdapter(menuRepository);
    }

    @Produces
    @Singleton
    public CreateMenuUseCase createMenuUseCase(
            MenuSaveOperationsPort menuSaveOperationsPort,
            MenuQueryOperationsPort menuQueryOperationsPort,
            RestaurantQueryOperationsPort restaurantQueryOperationsPort) {
        return menuService(menuSaveOperationsPort, menuQueryOperationsPort, restaurantQueryOperationsPort);
    }

    @Produces
    @Singleton
    public ViewMenusUseCase viewMenusUseCase(MenuQueryOperationsPort menuQueryOperationsPort) {
        return menuService(null, menuQueryOperationsPort, null);
    }

    private MenuService menuService(
            MenuSaveOperationsPort menuSaveOperationsPort,
            MenuQueryOperationsPort menuQueryOperationsPort,
            RestaurantQueryOperationsPort restaurantQueryOperationsPort) {

        return new MenuService(menuQueryOperationsPort, menuSaveOperationsPort, restaurantQueryOperationsPort);
    }

}
