package io.mobimenu.web.request;

import io.mobimenu.domain.Restaurant;

import javax.validation.constraints.NotBlank;

import io.mobimenu.core.port.in.restaurant.CreateRestaurantUseCase;

public record CreateRestaurantRequest(

        @NotBlank
        String firstname,

        @NotBlank
        String lastname,

        @NotBlank
        String restaurantName,

        @NotBlank
        String email,

        Restaurant.Type restaurantType,

        @NotBlank
        String password) {

    public CreateRestaurantUseCase.CreateRestaurantCommand toCommand() {
        return CreateRestaurantUseCase.CreateRestaurantCommand.builder()
                .firstName(firstname)
                .lastName(lastname)
                .restaurantName(restaurantName)
                .restaurantType(restaurantType)
                .emailAddress(email)
                .password(password)
                .build();
    }
}
