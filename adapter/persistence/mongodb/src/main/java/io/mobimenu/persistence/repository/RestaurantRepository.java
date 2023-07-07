package io.mobimenu.persistence.repository;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.smallrye.mutiny.Uni;
import io.mobimenu.persistence.common.Fields;
import io.mobimenu.persistence.entity.RestaurantEntity;
import org.bson.types.ObjectId;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RestaurantRepository implements ReactivePanacheMongoRepository<RestaurantEntity> {

    public Uni<RestaurantEntity> findByName(String name) {
        return find(Fields.NAME, name).firstResult();
    }

    public Uni<RestaurantEntity> findByRestaurantId(String restaurantId) {
        return findById(new ObjectId(restaurantId));
    }

}
