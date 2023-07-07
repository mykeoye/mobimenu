package io.mobimenu.core.port.out.menu;

import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.Menu;

public interface MenuSaveOperationsPort {

    Uni<Menu> saveMenu(Menu menu);

}
