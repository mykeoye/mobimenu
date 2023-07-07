package io.mobimenu.persistence.repository;

import io.mobimenu.domain.Menu;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import io.mobimenu.persistence.common.Fields;
import io.mobimenu.persistence.common.MappingUtil;
import io.mobimenu.persistence.entity.MenuEntity;
import org.bson.types.ObjectId;
import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class MenuRepository implements ReactivePanacheMongoRepository<MenuEntity> {

    public Uni<MenuEntity> findByMenuId(String menuId) {
        return findById(new ObjectId(menuId));
    }

    public Uni<MenuEntity> findActiveByRestaurant(String restaurantId) {
        return find("%s = :status and %s = :restaurantId".formatted(Fields.STATUS, Fields.RESTAURANT_ID),
                Parameters.with(Fields.STATUS, Menu.Status.ACTIVE).and(Fields.RESTAURANT_ID, new ObjectId(restaurantId)))
                .firstResult();
    }

    public Uni<List<MenuEntity>> findByMenuIds(Set<String> menuIds) {
        return find("%s in :ids".formatted(Fields._ID), Parameters.with(Fields.IDS, MappingUtil.toObjectIds(menuIds))).list();
    }

    public Uni<MenuEntity> findByNameAndRestaurantId(String name, String restaurantId) {
        return find("%s = :name and %s = :restaurantId".formatted(Fields.NAME, Fields.RESTAURANT_ID),
                Parameters.with(Fields.NAME, name).and(Fields.RESTAURANT_ID, new ObjectId(restaurantId))).firstResult();
    }

    public Uni<List<MenuEntity>> findAllByRestaurant(String restaurantId, int page, int limit) {
        return find(Fields.RESTAURANT_ID, new ObjectId(restaurantId)).page(Page.of(page, limit)).list();
    }

    public Uni<Long> countAllByRestaurant(String restaurantId) {
        return find(Fields.RESTAURANT_ID, new ObjectId(restaurantId)).count();
    }

}
