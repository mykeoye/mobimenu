package io.mobimenu.web.context;

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
public class MealContext {

    @Produces
    @Singleton
    public MealQueryOperationsPort mealQueryOperationsPort(MealRepository repository) {
        return new MealQueryAdapter(repository);
    }

    @Produces
    @Singleton
    public MealSaveOperationsPort mealSaveOperationsPort(MealRepository repository) {
        return new MealPersistenceAdapter(repository);
    }

    @Produces
    @Singleton
    public CreateMealUseCase createMealUseCase(MealQueryOperationsPort mealQueryOperationsPort,
                                               MealSaveOperationsPort mealSaveOperationsPort,
                                               CategoryQueryOperationsPort categoryQueryOperationsPort,
                                               MenuQueryOperationsPort menuQueryOperationsPort,
                                               RestaurantQueryOperationsPort restaurantQueryOperationsPort) {
        return mealService(mealQueryOperationsPort,
                mealSaveOperationsPort,
                categoryQueryOperationsPort,
                menuQueryOperationsPort,
                restaurantQueryOperationsPort);
    }

    @Produces
    @Singleton
    public ViewMealsUseCase viewMealsUseCase(MealQueryOperationsPort mealQueryOperationsPort,
                                             MealSaveOperationsPort mealSaveOperationsPort,
                                             CategoryQueryOperationsPort categoryQueryOperationsPort,
                                             MenuQueryOperationsPort menuQueryOperationsPort,
                                             RestaurantQueryOperationsPort restaurantQueryOperationsPort) {
        return mealService(mealQueryOperationsPort,
                mealSaveOperationsPort,
                categoryQueryOperationsPort,
                menuQueryOperationsPort,
                restaurantQueryOperationsPort);

    }

    private MealService mealService(MealQueryOperationsPort mealQueryOperationsPort,
                                    MealSaveOperationsPort mealSaveOperationsPort,
                                    CategoryQueryOperationsPort categoryQueryOperationsPort,
                                    MenuQueryOperationsPort menuQueryOperationsPort,
                                    RestaurantQueryOperationsPort restaurantQueryOperationsPort) {
        return new MealService(mealQueryOperationsPort,
                mealSaveOperationsPort,
                categoryQueryOperationsPort,
                menuQueryOperationsPort,
                restaurantQueryOperationsPort);
    }

}
