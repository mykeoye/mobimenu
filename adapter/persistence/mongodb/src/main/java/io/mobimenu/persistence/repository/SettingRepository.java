package io.mobimenu.persistence.repository;

import javax.enterprise.context.ApplicationScoped;
import io.mobimenu.domain.enums.SettingType;
import io.mobimenu.persistence.common.Fields;
import io.mobimenu.persistence.entity.SettingEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import org.bson.types.ObjectId;

@ApplicationScoped
public class SettingRepository implements ReactivePanacheMongoRepository<SettingEntity> {

    public Uni<SettingEntity> findByTypeAndRestaurant(SettingType type, String restaurantId) {
        return find("%s = :type and %s = :restaurantId".formatted(Fields.TYPE, Fields.RESTAURANT_ID),
                Parameters.with(Fields.TYPE, type).and(Fields.RESTAURANT_ID, new ObjectId(restaurantId))).firstResult();
    }

    public Uni<SettingEntity> findByIdAndRestaurant(String settingId, String restaurantId) {
        return find("%s = :type and %s = :restaurantId".formatted(Fields.TYPE, Fields.RESTAURANT_ID),
                Parameters.with(Fields._ID, new ObjectId(settingId)).and(Fields.RESTAURANT_ID, new ObjectId(restaurantId))).firstResult();
    }

}
