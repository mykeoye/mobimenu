package io.mobimenu.core.port.in.restaurant;

import io.mobimenu.domain.PhoneNumber;
import io.mobimenu.domain.Restaurant;
import io.mobimenu.domain.enums.SalesChannel;
import io.smallrye.mutiny.Uni;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.Set;

public interface UpdateRestaurantUseCase {
    Uni<Restaurant> updateRestaurant(String restaurantId, UpdateRestaurantUseCase.UpdateRestaurantCommand command);

    @Value
    @Builder
    @RequiredArgsConstructor
    class UpdateRestaurantCommand {
        PhoneNumber phoneNumber;
        String address;
        String email;
        Set<SalesChannel> salesChannels;
        String accentColour;
        String primaryColour;
        String logo;
    }

}
