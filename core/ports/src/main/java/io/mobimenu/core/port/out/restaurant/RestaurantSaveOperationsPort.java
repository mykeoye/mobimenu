package io.mobimenu.core.port.out.restaurant;

import io.mobimenu.domain.PhoneNumber;
import io.mobimenu.domain.Theme;
import io.mobimenu.domain.enums.SalesChannel;
import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.Restaurant;

import java.util.Set;

public interface RestaurantSaveOperationsPort {

    Uni<Restaurant> saveRestaurant(Restaurant restaurant, String userId);

    Uni<Restaurant> updateRestaurant(String restaurantId,
                                     String email,
                                     String address,
                                     PhoneNumber phoneNumber,
                                     Set<SalesChannel> salesChannels,
                                     Theme theme);

}
