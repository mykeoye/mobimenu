package io.mobimenu.web.context;

import io.mobimenu.core.port.in.menu.CreateMenuUseCase;
import io.mobimenu.core.port.in.menu.ViewMenusUseCase;
import io.mobimenu.core.port.out.menu.MenuQueryOperationsPort;
import io.mobimenu.core.port.out.menu.MenuSaveOperationsPort;
import io.mobimenu.core.port.out.restaurant.RestaurantQueryOperationsPort;
import io.mobimenu.core.service.menu.MenuService;
import javax.inject.Singleton;
import javax.enterprise.inject.Produces;
import io.mobimenu.persistence.MenuQueryAdapter;
import javax.enterprise.context.ApplicationScoped;
import io.mobimenu.persistence.MenuPersistenceAdapter;
import io.mobimenu.persistence.repository.MenuRepository;

@ApplicationScoped
public class MenuContext {

    @Produces
    @Singleton
    public MenuQueryOperationsPort menuQueryOperationsPort(MenuRepository repository) {
        return new MenuQueryAdapter(repository);
    }

    @Produces
    @Singleton
    public MenuSaveOperationsPort menuSaveOperationsPort(MenuRepository repository) {
        return new MenuPersistenceAdapter(repository);
    }

    @Produces
    @Singleton
    public ViewMenusUseCase viewMenusUseCase(MenuQueryOperationsPort menuQueryOperationsPort,
                                             MenuSaveOperationsPort menuSaveOperationsPort,
                                             RestaurantQueryOperationsPort restaurantQueryOperationsPort) {
        return menuService(menuQueryOperationsPort, menuSaveOperationsPort, restaurantQueryOperationsPort);
    }

    @Produces
    @Singleton
    public CreateMenuUseCase createMenuUseCase(MenuQueryOperationsPort menuQueryOperationsPort,
                                              MenuSaveOperationsPort menuSaveOperationsPort,
                                              RestaurantQueryOperationsPort restaurantQueryOperationsPort) {
        return menuService(menuQueryOperationsPort, menuSaveOperationsPort, restaurantQueryOperationsPort);
    }

    private MenuService menuService(MenuQueryOperationsPort menuQueryOperationsPort,
                                    MenuSaveOperationsPort menuSaveOperationsPort,
                                    RestaurantQueryOperationsPort restaurantQueryOperationsPort) {
        return new MenuService(menuQueryOperationsPort, menuSaveOperationsPort, restaurantQueryOperationsPort);
    }

}
