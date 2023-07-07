package io.mobimenu.web.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;
import io.mobimenu.core.port.in.meal.CreateMealUseCase;

public record CreateMealRequest(

        @NotBlank
        String name,

        @NotEmpty
        Set<String> menuIds,

        @NotBlank
        String categoryId,

        @NotNull
        BigDecimal normalPrice,

        @NotNull
        BigDecimal discountPrice,

        @NotBlank
        String description,

        @Min(value = 1, message = "cookingDuration must be at least 1 minute")
        long cookingDuration,

        @NotNull
        Boolean isAvailable,

        Set<String> imageURIs,

        String videoURI,

        @NotBlank
        String restaurantId) {

    public CreateMealUseCase.CreateMealCommand toCommand() {
        return CreateMealUseCase.CreateMealCommand.builder()
                .name(name.trim())
                .menuIds(Set.copyOf(menuIds))
                .categoryId(categoryId)
                .normalPrice(normalPrice)
                .discountPrice(discountPrice)
                .description(description)
                .cookingDuration(cookingDuration)
                .available(isAvailable)
                .imageURIs(Set.copyOf(imageURIs))
                .videoURI(videoURI)
                .restaurantId(restaurantId)
                .build();
    }
}
