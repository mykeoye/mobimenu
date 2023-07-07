package io.mobimenu.persistence.repository;

import java.util.List;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import io.mobimenu.persistence.common.Fields;
import io.mobimenu.persistence.entity.TagEntity;
import javax.enterprise.context.ApplicationScoped;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import org.bson.types.ObjectId;

@ApplicationScoped
public class TagRepository implements ReactivePanacheMongoRepository<TagEntity> {

    public Uni<TagEntity> findByTagId(String tagId) {
        return find(Fields.RESTAURANT_ID, tagId).firstResult();
    }

    public Uni<TagEntity> findByNameAndRestaurant(String name, String restaurantId) {
        return find("%s like :name and %s = :restaurantId".formatted(Fields.NAME, Fields.RESTAURANT_ID), Parameters.with(Fields.NAME, name)
                .and(Fields.RESTAURANT_ID, new ObjectId(restaurantId))).firstResult();
    }

    public Uni<List<TagEntity>> findAllByRestaurant(String restaurantId, int page, int limit) {
        return find(Fields.RESTAURANT_ID, new ObjectId(restaurantId)).page(Page.of(page, limit)).list();
    }

    public Uni<Long> countAllByRestaurant(String restaurantId) {
        return find(Fields.RESTAURANT_ID, new ObjectId(restaurantId)).count();
    }

}
