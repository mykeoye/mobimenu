package io.mobimenu.persistence.repository;

import io.mobimenu.persistence.common.Fields;
import io.mobimenu.persistence.entity.BankAccountEntity;
import javax.enterprise.context.ApplicationScoped;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import org.bson.types.ObjectId;

import java.util.List;

@ApplicationScoped
public class BankAccountRepository implements ReactivePanacheMongoRepository<BankAccountEntity> {

    public Uni<List<BankAccountEntity>> findByRestaurant(String restaurantId) {
        return find("%s = :restaurantId".formatted(Fields.RESTAURANT_ID),
                Parameters.with(Fields.RESTAURANT_ID, new ObjectId(restaurantId)))
                .list();
    }

}
