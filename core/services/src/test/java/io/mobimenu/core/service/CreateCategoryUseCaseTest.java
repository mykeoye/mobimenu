package io.mobimenu.core.service;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.mobimenu.common.api.Code;
import io.mobimenu.common.api.Failure;
import io.mobimenu.core.port.in.category.CreateCategoryUseCase;
import io.mobimenu.core.port.out.category.CategoryQueryOperationsPort;
import io.mobimenu.core.port.out.restaurant.RestaurantSaveOperationsPort;
import io.mobimenu.core.service.context.AbstractUseCaseTest;
import io.mobimenu.domain.Restaurant;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class CreateCategoryUseCaseTest extends AbstractUseCaseTest {

    @Inject
    CreateCategoryUseCase createCategoryUseCase;

    @Inject
    RestaurantSaveOperationsPort restaurantSaveOperationsPort;

    @Inject
    CategoryQueryOperationsPort categoryQueryOperationsPort;

    @Test
    @DisplayName("creating a category should be a successful operation")
    void testCreatingACategory() {
        var restaurant = restaurantSaveOperationsPort.saveRestaurant(Restaurant.withRequiredFields(
                "Hooli Chops", Restaurant.Type.SINGLE), ObjectId.get().toString())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();
        assertNotNull(restaurant);

        var categoryId = createCategoryUseCase.createCategory(createCategoryCommand("Spicy Grills", restaurant.getRestaurantId()))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();
        assertNotNull(categoryId);
        assertNotNull(categoryId.categoryId());

        var category = categoryQueryOperationsPort.getById(categoryId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        assertNotNull(category);
        assertEquals(restaurant.getRestaurantId(), category.restaurantId());
        assertEquals(categoryId, category.id());

    }

    @Test
    @DisplayName("creating a category in a non-existent restaurant should not be a successful operation")
    void testCreatingACategoryInANonExistentRestaurant() {
        var throwable = createCategoryUseCase.createCategory(createCategoryCommand("Spicy Grills", ObjectId.get().toString()))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitFailure().assertFailed().getFailure();
        assertNotNull(throwable);

        var failure = Failure.from(throwable);
        assertEquals(Code.RESTAURANT_NOT_FOUND, failure.getError());
    }

    @Test
    @DisplayName("creating a category with the same name in the same restaurant should not be a successful operation")
    void testCreatingACategoryWithSameNameInSameRestaurant() {
        var restaurantId = restaurantSaveOperationsPort.saveRestaurant(Restaurant.withRequiredFields(
                        "Hooli Chops", Restaurant.Type.SINGLE), ObjectId.get().toString())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();
        assertNotNull(restaurantId);

        var categoryId = createCategoryUseCase.createCategory(createCategoryCommand("Spicy Grills", restaurantId.getRestaurantId()))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();
        assertNotNull(categoryId);
        assertNotNull(categoryId.categoryId());

        var throwable = createCategoryUseCase.createCategory(createCategoryCommand("Spicy Grills", restaurantId.getRestaurantId()))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitFailure().assertFailed().getFailure();
        assertNotNull(throwable);

        var failure = Failure.from(throwable);
        assertEquals(Code.CATEGORY_EXISTS, failure.getError());
    }

    CreateCategoryUseCase.CreateCategoryCommand createCategoryCommand(String name, String restaurantId) {
        return CreateCategoryUseCase.CreateCategoryCommand.builder()
                .active(true)
                .name(name)
                .restaurantId(restaurantId)
                .build();
    }

    @AfterEach
    void cleanup() {
        cleanupRestaurant();
        cleanupCategory();
    }

}
