package io.mobimenu.domain.filters;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Value
@EqualsAndHashCode(callSuper = true)
public class MealFilter extends Filter {

    String categoryId;
    String restaurantId;

    public static MealFilter from(String restaurantId, String categoryId) {
        return MealFilter.builder()
                .restaurantId(restaurantId)
                .categoryId(categoryId)
                .build();
    }

}
