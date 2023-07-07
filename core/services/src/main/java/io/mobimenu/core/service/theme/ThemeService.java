package io.mobimenu.core.service.theme;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.mobimenu.common.api.Page;
import io.mobimenu.core.port.in.theme.ViewThemesUseCase;
import io.mobimenu.core.port.out.theme.ThemeQueryOperationsPort;
import io.mobimenu.domain.Theme;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
public class ThemeService implements ViewThemesUseCase {

    private final ThemeQueryOperationsPort themeQueryOperationsPort;

    @Override
    public Uni<Tuple2<Long, List<Theme>>> loadSystemThemes(Page page) {
        return themeQueryOperationsPort.getAllSystemDefined(page);
    }

}
