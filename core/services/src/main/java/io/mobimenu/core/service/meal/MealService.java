package io.mobimenu.core.service.meal;

import io.mobimenu.domain.filters.MealFilter;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.mobimenu.common.api.Code;
import io.mobimenu.common.api.Failure;
import io.mobimenu.common.api.Page;
import io.mobimenu.core.port.in.meal.CreateMealUseCase;
import io.mobimenu.core.port.in.meal.ViewMealsUseCase;
import io.mobimenu.core.port.out.category.CategoryQueryOperationsPort;
import io.mobimenu.core.port.out.meal.MealQueryOperationsPort;
import io.mobimenu.core.port.out.meal.MealSaveOperationsPort;
import io.mobimenu.core.port.out.menu.MenuQueryOperationsPort;
import io.mobimenu.core.port.out.restaurant.RestaurantQueryOperationsPort;
import io.mobimenu.domain.Category;
import io.mobimenu.domain.Meal;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
public class MealService implements CreateMealUseCase, ViewMealsUseCase {

    private final MealQueryOperationsPort mealQueryOperationsPort;
    private final MealSaveOperationsPort mealSaveOperationsPort;
    private final CategoryQueryOperationsPort categoryQueryOperationsPort;
    private final MenuQueryOperationsPort menuQueryOperationsPort;
    private final RestaurantQueryOperationsPort restaurantQueryOperationsPort;

    @Override
    public Uni<Meal.MealId> createMeal(CreateMealCommand command) {
        var name = command.getName().trim();
        var menuIds = command.getMenuIds();
        var categoryId = command.getCategoryId();
        return menuQueryOperationsPort.getByIds(menuIds)
                .map(menus -> (menus.isEmpty() || menus.size() != menuIds.size()) ? null : menus)
                .onItem().ifNull().failWith(Failure.of(Code.MENU_NOT_FOUND))
                .chain(() -> categoryQueryOperationsPort.getById(categoryId))
                .onItem().ifNull().failWith(Failure.of(Code.CATEGORY_NOT_FOUND))
                .chain(() -> mealQueryOperationsPort.getByNameAndMenus(name, menuIds))
                .onItem().ifNotNull().failWith(Failure.of(Code.MEAL_EXISTS))
                .chain(() -> restaurantQueryOperationsPort.getById(command.getRestaurantId()))
                .onItem().ifNull().failWith(Failure.of(Code.RESTAURANT_NOT_FOUND))
                .chain(() -> mealSaveOperationsPort.saveMeal(
                        Meal.withRequiredFields(
                                name,
                                command.getDescription().trim(),
                                categoryId,
                                command.getMenuIds(),
                                command.getAvailable() ? Meal.Status.AVAILABLE : Meal.Status.UNAVAILABLE,
                                command.getImageURIs(),
                                command.getVideoURI(),
                                command.getNormalPrice(),
                                command.getDiscountPrice(),
                                command.getCookingDuration(),
                                command.getRestaurantId()
                        )
                ));

    }

    @Override
    public Uni<Meal> loadMealById(Meal.MealId mealId) {
        return mealQueryOperationsPort.getMealById(mealId);
    }

    @Override
    public Uni<Tuple2<Long, List<Meal>>> loadMealsByRestaurant(String restaurantId, Page page) {
        return mealQueryOperationsPort.getMealsByRestaurant(restaurantId, page);
    }

    @Override
    public Uni<Tuple2<Long, List<Meal>>> loadMealsByFilter(MealFilter filter, Page page) {
        return mealQueryOperationsPort.getMealsByFilter(filter, page);
    }

    @Override
    public Uni<Tuple2<Long, List<Meal>>> loadMealsByMenu(String menuId, Page page) {
        return mealQueryOperationsPort.getMealsByMenu(menuId, page);
    }

    @Override
    public Uni<List<Meal>> loadMealsByCategory(Category.CategoryId categoryId, Page page) {
        return mealQueryOperationsPort.getMealsByCategory(categoryId, page);
    }

}
