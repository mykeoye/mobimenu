package io.mobimenu.persistence.context;

import io.mobimenu.core.port.out.menu.MenuQueryOperationsPort;
import io.mobimenu.core.port.out.menu.MenuSaveOperationsPort;
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

}
