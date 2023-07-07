package io.mobimenu.web.context;

import io.mobimenu.core.port.in.theme.ViewThemesUseCase;
import io.mobimenu.core.port.out.theme.ThemeSaveOperationsPort;
import io.mobimenu.core.service.theme.ThemeService;
import javax.inject.Singleton;
import javax.enterprise.inject.Produces;
import io.mobimenu.persistence.ThemePersistenceAdapter;
import io.mobimenu.persistence.ThemeQueryAdapter;
import javax.enterprise.context.ApplicationScoped;
import io.mobimenu.core.port.out.theme.ThemeQueryOperationsPort;
import io.mobimenu.persistence.repository.ThemeRepository;

@ApplicationScoped
public class ThemeContext {

    @Produces
    @Singleton
    public ThemeQueryOperationsPort themeQueryOperationsPort(ThemeRepository themeRepository) {
        return new ThemeQueryAdapter(themeRepository);
    }

    @Produces
    @Singleton
    public ThemeSaveOperationsPort themeSaveOperationsPort(ThemeRepository themeRepository) {
        return new ThemePersistenceAdapter(themeRepository);
    }

    @Produces
    @Singleton
    public ViewThemesUseCase viewThemesUseCase(ThemeQueryOperationsPort themeQueryOperationsPort) {
        return new ThemeService(themeQueryOperationsPort);
    }

}
