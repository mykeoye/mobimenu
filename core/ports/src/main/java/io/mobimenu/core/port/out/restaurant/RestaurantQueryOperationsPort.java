package io.mobimenu.core.port.out.restaurant;

import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.Restaurant;

public interface RestaurantQueryOperationsPort {

    Uni<Restaurant> getByName(String name);

    Uni<Restaurant> getById(String id);

}
