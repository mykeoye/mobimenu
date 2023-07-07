package io.mobimenu.core.port.out.meal;

import java.util.List;
import java.util.Set;

import io.mobimenu.domain.filters.MealFilter;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.mobimenu.common.api.Page;
import io.mobimenu.domain.Category;
import io.mobimenu.domain.Meal;

public interface MealQueryOperationsPort {

    Uni<Meal> getMealById(Meal.MealId mealId);

    Uni<List<Meal>> getMealsByIdsAndStatus(List<String> mealIds, Meal.Status status);

    Uni<Meal> getByNameAndMenus(String name, Set<String> menus);

    Uni<Tuple2<Long, List<Meal>>> getMealsByRestaurant(String restaurantId, Page page);

    Uni<Tuple2<Long, List<Meal>>> getMealsByFilter(MealFilter filter, Page page);

    Uni<Tuple2<Long, List<Meal>>> getMealsByMenu(String menuId, Page page);

    Uni<List<Meal>> getMealsByCategory(Category.CategoryId categoryId, Page page);

}
