package io.mobimenu.core.service.context;

import io.mobimenu.core.port.in.theme.ViewThemesUseCase;
import io.mobimenu.core.port.out.theme.ThemeQueryOperationsPort;
import io.mobimenu.core.port.out.theme.ThemeSaveOperationsPort;
import io.mobimenu.core.service.theme.ThemeService;
import io.mobimenu.persistence.ThemePersistenceAdapter;
import io.mobimenu.persistence.ThemeQueryAdapter;
import io.mobimenu.persistence.repository.ThemeRepository;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@ApplicationScoped
public class ThemeTestContext {

    @Produces
    @Singleton
    public ThemeSaveOperationsPort themeSaveOperationsPort(ThemeRepository themeRepository) {
        return new ThemePersistenceAdapter(themeRepository);
    }

    @Produces
    @Singleton
    public ThemeQueryOperationsPort themeQueryOperationsPort(ThemeRepository themeRepository) {
        return new ThemeQueryAdapter(themeRepository);
    }

    @Produces
    @Singleton
    public ViewThemesUseCase viewThemesUseCase(ThemeQueryOperationsPort themeQueryOperationsPort) {
        return new ThemeService(themeQueryOperationsPort);
    }

}
