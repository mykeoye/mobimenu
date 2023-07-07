package io.mobimenu.core.port.out.theme;

import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.Theme;

public interface ThemeSaveOperationsPort {

    Uni<Theme.ThemeId> saveTheme(Theme theme);
}
