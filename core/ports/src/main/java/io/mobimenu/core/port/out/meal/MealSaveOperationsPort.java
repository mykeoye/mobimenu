package io.mobimenu.core.port.out.meal;

import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.Meal;

import java.util.List;

public interface MealSaveOperationsPort {

    Uni<Meal.MealId> saveMeal(Meal meal);

    Uni<Void> saveMeals(List<Meal> meals);

}
