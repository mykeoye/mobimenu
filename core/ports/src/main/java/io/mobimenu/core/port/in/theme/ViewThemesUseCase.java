package io.mobimenu.core.port.in.theme;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.mobimenu.common.api.Page;
import io.mobimenu.domain.Theme;
import java.util.List;

public interface ViewThemesUseCase {

    Uni<Tuple2<Long, List<Theme>>> loadSystemThemes(Page page);

}
