package io.mobimenu.persistence;

import io.smallrye.mutiny.Uni;
import io.mobimenu.core.port.out.restaurant.RestaurantQueryOperationsPort;
import io.mobimenu.domain.Restaurant;
import io.mobimenu.persistence.mapper.RestaurantMapper;
import io.mobimenu.persistence.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;

/**
 * This class implements all restaurant related queries to the datastore. Strictly retrievals only
 */
@RequiredArgsConstructor
public class RestaurantQueryAdapter implements RestaurantQueryOperationsPort {

    private final RestaurantRepository repository;
    private final RestaurantMapper mapper = RestaurantMapper.INSTANCE;

    @Override
    public Uni<Restaurant> getByName(String name) {
        return repository.findByName(name)
                .onItem().ifNotNull().transform(mapper::entityToDomainObject);
    }

    @Override
    public Uni<Restaurant> getById(String restaurantId) {
        return repository.findById(new ObjectId(restaurantId))
                .onItem().ifNotNull().transform(mapper::entityToDomainObject);
    }

}
