package io.mobimenu.persistence;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.mobimenu.common.api.Page;
import io.mobimenu.core.port.out.menu.MenuQueryOperationsPort;
import io.mobimenu.domain.Menu;
import io.mobimenu.persistence.mapper.MenuMapper;
import io.mobimenu.persistence.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class implements all menu related queries to the datastore. Strictly retrievals only
 */
@RequiredArgsConstructor
public class MenuQueryAdapter implements MenuQueryOperationsPort {

    private final MenuRepository menuRepository;
    private final MenuMapper mapper = MenuMapper.INSTANCE;

    @Override
    public Uni<Menu> getById(String menuId) {
        return menuRepository.findByMenuId(menuId).map(mapper::entityToDomainObject);
    }

    @Override
    public Uni<Menu> getActiveMenuByRestaurant(String restaurantId) {
        return menuRepository.findActiveByRestaurant(restaurantId).map(mapper::entityToDomainObject);
    }

    @Override
    public Uni<List<Menu>> getByIds(Set<String> menuIds) {
        return menuRepository.findByMenuIds(menuIds)
                .map(menus -> Optional.ofNullable(menus).orElseGet(List::of))
                .map(menus -> menus.stream().map(mapper::entityToDomainObject).collect(Collectors.toList()));
    }

    @Override
    public Uni<Menu> getByNameAndRestaurant(String name, String restaurantId) {
        return menuRepository.findByNameAndRestaurantId(name, restaurantId).map(mapper::entityToDomainObject);
    }

    @Override
    public Uni<Tuple2<Long, List<Menu>>> getAllByRestaurantId(String restaurantId, Page page) {
        var countUni = menuRepository.countAllByRestaurant(restaurantId);
        var menusUni = menuRepository.findAllByRestaurant(restaurantId, page.getOffset(), page.getLimit())
                .map(menus -> Optional.ofNullable(menus).orElseGet(List::of))
                .map(menus -> menus.stream().map(mapper::entityToDomainObject).collect(Collectors.toList()));
        return Uni.combine().all().unis(countUni, menusUni).asTuple();
    }

}
