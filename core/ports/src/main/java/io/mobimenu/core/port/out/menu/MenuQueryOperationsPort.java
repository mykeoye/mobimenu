package io.mobimenu.core.port.out.menu;

import java.util.List;
import java.util.Set;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.mobimenu.common.api.Page;
import io.mobimenu.domain.Menu;

public interface MenuQueryOperationsPort {

    Uni<Menu> getById(String menuId);

    Uni<Menu> getActiveMenuByRestaurant(String restaurantId);

    Uni<List<Menu>> getByIds(Set<String> menuIds);

    Uni<Menu> getByNameAndRestaurant(String name, String restaurantId);

    Uni<Tuple2<Long, List<Menu>>> getAllByRestaurantId(String restaurantId, Page page);

}
