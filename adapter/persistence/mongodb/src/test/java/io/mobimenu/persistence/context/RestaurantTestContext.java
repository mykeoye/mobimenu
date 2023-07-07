package io.mobimenu.persistence.context;

import io.mobimenu.persistence.RestaurantPersistenceAdapter;
import io.mobimenu.persistence.RestaurantQueryAdapter;
import io.mobimenu.persistence.repository.RestaurantRepository;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@ApplicationScoped
public class RestaurantTestContext {

    @Produces
    @Singleton
    public RestaurantPersistenceAdapter restaurantPersistenceAdapter(RestaurantRepository repository) {
        return new RestaurantPersistenceAdapter(repository);
    }

    @Produces
    @Singleton
    public RestaurantQueryAdapter restaurantQueryAdapter(RestaurantRepository repository) {
        return new RestaurantQueryAdapter(repository);
    }
}
