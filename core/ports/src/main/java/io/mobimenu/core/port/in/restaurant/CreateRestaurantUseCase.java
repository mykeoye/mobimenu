package io.mobimenu.core.port.in.restaurant;

import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.Restaurant;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;

public interface CreateRestaurantUseCase {

    Uni<Restaurant> createRestaurant(CreateRestaurantCommand command);

    @Value
    @Builder
    @ToString
    @RequiredArgsConstructor
    class CreateRestaurantCommand {
        String firstName;
        String lastName;
        String restaurantName;
        Restaurant.Type restaurantType;
        String emailAddress;
        String password;
    }
}
