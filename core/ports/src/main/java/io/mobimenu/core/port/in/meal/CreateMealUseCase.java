package io.mobimenu.core.port.in.meal;

import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.Category;
import io.mobimenu.domain.Meal;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import java.util.Set;
import java.math.BigDecimal;

public interface CreateMealUseCase {

    Uni<Meal.MealId> createMeal(CreateMealCommand command);

    @Value
    @Builder
    @ToString
    class CreateMealCommand {
        String name;
        Set<String> menuIds;
        String categoryId;
        BigDecimal normalPrice;
        BigDecimal discountPrice;
        String description;
        long cookingDuration;
        Boolean available;
        Set<String> imageURIs;
        String videoURI;
        String restaurantId;

        public Category.CategoryId getCategoryId() {
            return new Category.CategoryId(categoryId);
        }
    }


}
