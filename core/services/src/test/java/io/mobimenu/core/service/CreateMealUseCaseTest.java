package io.mobimenu.core.service;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.mobimenu.core.port.in.meal.CreateMealUseCase;
import io.mobimenu.core.port.out.category.CategorySaveOperationsPort;
import io.mobimenu.core.port.out.meal.MealQueryOperationsPort;
import io.mobimenu.core.port.out.menu.MenuSaveOperationsPort;
import io.mobimenu.core.port.out.restaurant.RestaurantSaveOperationsPort;
import io.mobimenu.core.service.context.AbstractUseCaseTest;
import io.mobimenu.domain.Category;
import io.mobimenu.domain.Meal;
import io.mobimenu.domain.Menu;
import io.mobimenu.domain.Restaurant;
import io.mobimenu.domain.Theme;
import io.mobimenu.domain.User;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class CreateMealUseCaseTest extends AbstractUseCaseTest {

    @Inject
    CreateMealUseCase createMealUseCase;

    @Inject
    RestaurantSaveOperationsPort restaurantSaveOperationsPort;

    @Inject
    MealQueryOperationsPort mealQueryOperationsPort;

    @Inject
    MenuSaveOperationsPort menuSaveOperationsPort;

    @Inject
    CategorySaveOperationsPort categorySaveOperationsPort;

    @Test
    @DisplayName("creating a meal with all required fields is a successful operation")
    void testCanCreateMealSuccessfully() {
        var restaurant = restaurantSaveOperationsPort.saveRestaurant(createRestaurant(), ObjectId.get().toString())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();
        var menu = menuSaveOperationsPort.saveMenu(
                Menu.withRequiredFields(
                        "Christmas Special",
                        Menu.Status.ACTIVE,
                        restaurant.getRestaurantId(), new Theme("theme-one", "#fff", "#fff", "image")))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();
        assertNotNull(menu);

        var categoryId = categorySaveOperationsPort.saveCategory(
                Category.withRequiredFields(
                     "Hot and Spicy",
                     Category.Status.ACTIVE,
                     restaurant.getRestaurantId(),
                     false))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();
        assertNotNull(categoryId);

        var mealId = createMealUseCase.createMeal(
                CreateMealUseCase.CreateMealCommand.builder()
                        .available(true)
                        .restaurantId(restaurant.getRestaurantId())
                        .categoryId(categoryId.categoryId())
                        .description("This is one of the best home cooked meals you can have")
                        .cookingDuration(10)
                        .menuIds(Set.of(menu.getMenuId()))
                        .normalPrice(new BigDecimal("100.00"))
                        .discountPrice(new BigDecimal("50.00"))
                        .name("Yam with Egg Sauce")
                        .build())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        assertNotNull(mealId);
        assertNotNull(mealId.mealId());

        var meal = mealQueryOperationsPort.getMealById(mealId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        assertNotNull(meal);
        assertEquals(mealId, meal.getId());
        assertEquals(Meal.Status.AVAILABLE, meal.getStatus());
        assertEquals(categoryId, meal.getCategoryId());
    }

    @AfterEach
    void cleanup() {
        cleanupMenu();
        cleanupCategory();
        cleanupMeal();
    }

    Restaurant createRestaurant() {
        return Restaurant.withRequiredFields("Johnson Pancakes Restaurant", Restaurant.Type.CHAIN);
    }
}
