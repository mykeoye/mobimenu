package io.mobimenu.core.service;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.mobimenu.common.api.Code;
import io.mobimenu.common.api.Failure;
import io.mobimenu.core.port.in.menu.CreateMenuUseCase;
import io.mobimenu.core.port.out.menu.MenuQueryOperationsPort;
import io.mobimenu.core.port.out.restaurant.RestaurantSaveOperationsPort;
import io.mobimenu.core.service.context.AbstractUseCaseTest;
import io.mobimenu.domain.Menu;
import io.mobimenu.domain.Restaurant;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class CreateMenuUseCaseTest extends AbstractUseCaseTest {

    @Inject
    CreateMenuUseCase createMenuUseCase;

    @Inject
    RestaurantSaveOperationsPort restaurantSaveOperationsPort;

    @Inject
    MenuQueryOperationsPort menuQueryOperationsPort;

    @Test
    void testThatCreatingAMenuForANonExistentRestaurantIsNotPossible() {
        var throwable = createMenuUseCase.createMenu(createMenuCommand(ObjectId.get().toString()))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitFailure().getFailure();

        var failure = Failure.from(throwable);
        assertEquals(Code.RESTAURANT_NOT_FOUND, failure.getError());
    }

    @Test
    void testThatCreatingAMenuForAnExistentRestaurantIsPossible() {
        var restaurant = restaurantSaveOperationsPort.saveRestaurant(createRestaurant(), ObjectId.get().toString())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        var menu = createMenuUseCase.createMenu(createMenuCommand(restaurant.getRestaurantId()))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        assertNotNull(menu);
        assertNotNull(menu.getMenuId());

        var fetched = menuQueryOperationsPort.getById(menu.getMenuId())
                        .subscribe().withSubscriber(UniAssertSubscriber.create())
                        .awaitItem().assertCompleted().getItem();

        assertNotNull(fetched);
        assertNotNull(fetched.getTheme());
        assertEquals(fetched.getMenuId(), menu.getMenuId());
        assertEquals(fetched.getRestaurantId(), restaurant.getRestaurantId());
        assertEquals(fetched.getStatus(), Menu.Status.ACTIVE);

    }

    CreateMenuUseCase.CreateMenuCommand createMenuCommand(String restaurantId) {
        return CreateMenuUseCase.CreateMenuCommand
                .builder()
                .active(true)
                .name("Christmas Delight")
                .restaurantId(restaurantId)
                .themeAccentColor("#fafaca")
                .themeBackgroundImage("#ffffff")
                .build();
    }

    Restaurant createRestaurant() {
        return Restaurant.withRequiredFields("Johnson Pancakes Restaurant", Restaurant.Type.CHAIN);
    }

    @AfterEach
    void cleanup() {
        cleanupMenu();
        cleanupRestaurant();
    }

}
