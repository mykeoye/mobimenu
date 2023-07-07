package io.mobimenu.core.port.in.menu;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.mobimenu.common.api.Page;
import io.mobimenu.domain.Menu;

import java.util.List;

public interface ViewMenusUseCase {

    Uni<Tuple2<Long, List<Menu>>> loadMenusByRestaurant(String restaurantId, Page page);

}
