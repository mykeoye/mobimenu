package io.mobimenu.core.service;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.mobimenu.common.api.Page;
import io.mobimenu.core.port.in.menu.ViewMenusUseCase;
import io.mobimenu.core.port.out.menu.MenuSaveOperationsPort;
import io.mobimenu.core.port.out.restaurant.RestaurantSaveOperationsPort;
import io.mobimenu.core.service.context.AbstractUseCaseTest;
import io.mobimenu.domain.Menu;
import io.mobimenu.domain.Restaurant;
import io.mobimenu.domain.Theme;
import io.mobimenu.domain.User;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class ViewMenusUseCaseTest extends AbstractUseCaseTest {

    @Inject
    ViewMenusUseCase viewMenusUseCase;

    @Inject
    MenuSaveOperationsPort menuSaveOperationsPort;

    @Inject
    RestaurantSaveOperationsPort restaurantSaveOperationsPort;

    @Test
    @DisplayName("loading menus for a non-existent restaurant should return an empty list")
    void testFetchingMenusForNonExistentRestaurant() {
        var restaurantId = ObjectId.get().toString();
        var unis = viewMenusUseCase.loadMenusByRestaurant(restaurantId, Page.of(1, 10))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        assertNotNull(unis);
        assertTrue(unis.getItem2().isEmpty());
    }

    @Test
    @DisplayName("loading menus for an existing restaurant should return the menus associated with that restaurant")
    void testFetchingMenusForExistingRestaurant() {
        var restaurant = restaurantSaveOperationsPort.saveRestaurant(Restaurant.withRequiredFields(
                "Fruity Pops", Restaurant.Type.SINGLE), ObjectId.get().toString())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        assertNotNull(restaurant);

        var menuId = menuSaveOperationsPort.saveMenu(Menu.withRequiredFields(
                "Spicy Special", Menu.Status.ACTIVE, restaurant.getRestaurantId(), new Theme("theme-one", "#fff", "#fff", "image")))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        assertNotNull(menuId);

        var unis = viewMenusUseCase.loadMenusByRestaurant(restaurant.getRestaurantId(), Page.of(1, 10))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        assertNotNull(unis);
        assertEquals(1, unis.getItem1());

        var menu = unis.getItem2().get(0);
        assertNotNull(menu);
        assertEquals(menu.getRestaurantId(), restaurant.getRestaurantId());

    }

    @AfterEach
    void cleanup() {
        cleanupMenu();
        cleanupRestaurant();
    }

}
