package io.mobimenu.core.port.in.meal;

import java.util.List;

import io.mobimenu.domain.filters.MealFilter;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.mobimenu.common.api.Page;
import io.mobimenu.domain.Meal;
import io.mobimenu.domain.Category;

public interface ViewMealsUseCase {

    Uni<Meal> loadMealById(Meal.MealId mealId);

    Uni<Tuple2<Long, List<Meal>>> loadMealsByRestaurant(String restaurantId, Page page);

    Uni<Tuple2<Long, List<Meal>>> loadMealsByFilter(MealFilter filter, Page page);

    Uni<Tuple2<Long, List<Meal>>> loadMealsByMenu(String menuId, Page page);

    Uni<List<Meal>> loadMealsByCategory(Category.CategoryId categoryId, Page page);

}
