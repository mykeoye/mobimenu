package io.mobimenu.persistence.repository;

import java.util.List;
import java.util.Set;
import io.mobimenu.domain.Meal;
import io.mobimenu.domain.filters.MealFilter;
import io.mobimenu.persistence.common.MongoOperator;
import org.bson.Document;
import org.bson.types.ObjectId;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import io.quarkus.panache.common.Page;
import io.mobimenu.persistence.common.Fields;
import io.mobimenu.persistence.common.MappingUtil;
import io.mobimenu.persistence.entity.MealEntity;
import javax.enterprise.context.ApplicationScoped;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;

@ApplicationScoped
public class MealRepository implements ReactivePanacheMongoRepository<MealEntity> {

    public Uni<MealEntity> findByMealId(String mealId) {
        return findById(new ObjectId(mealId));
    }

    public Uni<List<MealEntity>> findByMealIds(List<String> mealIds, Meal.Status status) {
        return find("%s in :ids and %s = :status".formatted(Fields._ID, Fields.STATUS), Parameters.with(Fields.IDS, MappingUtil.toObjectIds(mealIds))
                .and(Fields.STATUS, status)).list();
    }

    public Uni<MealEntity> findByNameAndMenus(String name, Set<String> menuIds) {
        return find("%s like :name and %s in :menuId".formatted(Fields.NAME, Fields.MENU_ID),
                Parameters.with(Fields.NAME, name).and(Fields.MENU_ID, MappingUtil.toObjectIds(menuIds))).firstResult();
    }

    public Uni<List<MealEntity>> findAllByCategory(String categoryId, int page, int limit) {
        return find(Fields.CATEGORY_ID, new ObjectId(categoryId)).page(Page.of(page, limit)).list();
    }

    public Uni<List<MealEntity>> findAllByRestaurant(String restaurantId, int page, int limit) {
        return find(Fields.RESTAURANT_ID, new ObjectId(restaurantId)).page(Page.of(page, limit)).list();
    }

    public Uni<Long> countAllByRestaurant(String restaurantId) {
        return find(Fields.RESTAURANT_ID, new ObjectId(restaurantId)).count();
    }

    public Uni<Long> countAllByMenu(String menuId) {
        return find(Fields.MENU_ID, new ObjectId(menuId)).count();
    }

    public Uni<List<MealEntity>> findAllByMenu(String menuId, int page, int limit) {
        return find(Fields.MENU_ID, new ObjectId(menuId)).page(Page.of(page, limit)).list();
    }

    public Uni<List<MealEntity>> findByFilter(MealFilter filter, int page, int limit) {
        return find(searchFilterToDocumentFilter(filter)).list();
    }

    public Uni<Long> countByFilter(MealFilter filter) {
        return find(searchFilterToDocumentFilter(filter)).count();
    }

    private Document searchFilterToDocumentFilter(MealFilter filter) {
        var query = new Document();
        if (filter.getRestaurantId() != null) {
            query.put(Fields.RESTAURANT_ID, new ObjectId(filter.getRestaurantId()));
        }
        if (filter.getCategoryId() != null) {
            query.put(Fields.CATEGORY_ID, new ObjectId(filter.getCategoryId()));
        }
        var from = filter.getFrom();
        var to = filter.getTo();
        if (from != null && to != null) {
            query.put(MongoOperator.AND, List.of(
                    new Document(Fields.CREATED, new Document(MongoOperator.GTE, filter.getFrom())),
                    new Document(Fields.CREATED, new Document(MongoOperator.LTE, filter.getTo()))
            ));
        }
        if (from != null && to == null) {
            query.put(Fields.CREATED, new Document(MongoOperator.GTE, filter.getFrom()));
        }
        if (from == null && to != null) {
            query.put(Fields.CREATED, new Document(MongoOperator.LTE, filter.getTo()));
        }
        if (filter.getDeleted() != null) {
            query.put(Fields.DELETED, filter.getDeleted());
        }
        return query;
    }

}
