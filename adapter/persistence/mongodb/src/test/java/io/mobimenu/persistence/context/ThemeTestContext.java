package io.mobimenu.persistence.context;

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
    public ThemeQueryAdapter themeQueryAdapter(ThemeRepository themeRepository) {
        return new ThemeQueryAdapter(themeRepository);
    }

    @Produces
    @Singleton
    public ThemePersistenceAdapter themePersistenceAdapter(ThemeRepository themeRepository) {
        return new ThemePersistenceAdapter(themeRepository);
    }

}
