package io.mobimenu.core.port.out.theme;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.mobimenu.common.api.Page;
import io.mobimenu.domain.Theme;

import java.util.List;

public interface ThemeQueryOperationsPort {

    Uni<Theme> getById(Theme.ThemeId themeId);

    Uni<Tuple2<Long, List<Theme>>> getAllSystemDefined(Page page);

}
