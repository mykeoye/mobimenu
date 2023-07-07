package io.mobimenu.persistence.repository;

import io.quarkus.panache.common.Page;
import java.util.List;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import io.mobimenu.persistence.common.Fields;
import io.mobimenu.persistence.entity.CategoryEntity;
import javax.enterprise.context.ApplicationScoped;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import org.bson.types.ObjectId;

@ApplicationScoped
public class CategoryRepository implements ReactivePanacheMongoRepository<CategoryEntity> {

    public Uni<CategoryEntity> findByNameAndRestaurant(String name, String restaurantId) {
        return find("%s like :name and %s = :restaurantId".formatted(Fields.NAME, Fields.RESTAURANT_ID), Parameters.with(Fields.NAME, name)
                        .and(Fields.RESTAURANT_ID, new ObjectId(restaurantId))).firstResult();
    }

    public Uni<CategoryEntity> findByCategoryId(String categoryId) {
        return findById(new ObjectId(categoryId));
    }

    public Uni<List<CategoryEntity>>  findAllByRestaurant(String restaurantId, int page, int limit) {
        return find(Fields.RESTAURANT_ID, new ObjectId(restaurantId)).page(Page.of(page, limit)).list();
    }

    public Uni<Long> countAllByRestaurant(String restaurantId) {
        return find(Fields.RESTAURANT_ID, new ObjectId(restaurantId)).count();
    }

}
