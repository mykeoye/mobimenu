package io.mobimenu.core.service.context;

import io.mobimenu.core.port.in.meal.CreateMealUseCase;
import io.mobimenu.core.port.in.meal.ViewMealsUseCase;
import io.mobimenu.core.port.out.category.CategoryQueryOperationsPort;
import io.mobimenu.core.port.out.meal.MealQueryOperationsPort;
import io.mobimenu.core.port.out.meal.MealSaveOperationsPort;
import io.mobimenu.core.port.out.menu.MenuQueryOperationsPort;
import io.mobimenu.core.port.out.restaurant.RestaurantQueryOperationsPort;
import io.mobimenu.core.service.meal.MealService;
import io.mobimenu.persistence.MealPersistenceAdapter;
import io.mobimenu.persistence.MealQueryAdapter;
import io.mobimenu.persistence.repository.MealRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@ApplicationScoped
public class MealTestContext {

    @Produces
    @Singleton
    public MealSaveOperationsPort mealSaveOperationsPort(MealRepository mealRepository) {
        return new MealPersistenceAdapter(mealRepository);
    }

    @Produces
    @Singleton
    public MealQueryOperationsPort mealQueryOperationsPort(MealRepository mealRepository) {
        return new MealQueryAdapter(mealRepository);
    }

    @Produces
    @Singleton
    public CreateMealUseCase createMealUseCase(
            MealSaveOperationsPort mealSaveOperationsPort,
            MealQueryOperationsPort mealQueryOperationsPort,
            MenuQueryOperationsPort menuQueryOperationsPort,
            CategoryQueryOperationsPort categoryQueryOperationsPort,
            RestaurantQueryOperationsPort restaurantQueryOperationsPort) {
        return mealService(mealSaveOperationsPort, mealQueryOperationsPort, menuQueryOperationsPort, categoryQueryOperationsPort, restaurantQueryOperationsPort);
    }

    @Produces
    @Singleton
    public ViewMealsUseCase viewMealsUseCase(
            MealQueryOperationsPort mealQueryOperationsPort) {
        return mealService(null, mealQueryOperationsPort, null, null, null);
    }

    private MealService mealService(
            MealSaveOperationsPort mealSaveOperationsPort,
            MealQueryOperationsPort mealQueryOperationsPort,
            MenuQueryOperationsPort menuQueryOperationsPort,
            CategoryQueryOperationsPort categoryQueryOperationsPort,
            RestaurantQueryOperationsPort restaurantQueryOperationsPort) {

        return new MealService(
                mealQueryOperationsPort,
                mealSaveOperationsPort,
                categoryQueryOperationsPort,
                menuQueryOperationsPort, restaurantQueryOperationsPort);
    }
}
