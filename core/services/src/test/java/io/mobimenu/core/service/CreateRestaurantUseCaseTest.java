package io.mobimenu.core.service;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.mobimenu.common.api.Code;
import io.mobimenu.common.api.Failure;
import io.mobimenu.core.port.in.restaurant.CreateRestaurantUseCase;
import io.mobimenu.core.port.out.restaurant.RestaurantQueryOperationsPort;
import io.mobimenu.core.port.out.user.UserQueryOperationsPort;
import io.mobimenu.core.service.context.AbstractUseCaseTest;
import io.mobimenu.domain.enums.AccountType;
import io.mobimenu.domain.Restaurant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class CreateRestaurantUseCaseTest extends AbstractUseCaseTest {

    @Inject
    CreateRestaurantUseCase createRestaurantUseCase;

    @Inject
    RestaurantQueryOperationsPort restaurantQueryOperationsPort;

    @Inject
    UserQueryOperationsPort userQueryOperationsPort;

    @AfterEach
    void cleanup() {
        cleanupRestaurant();
    }

    @Test
    void testThatCreatingARestaurantIsPossible() {
        var command = createRestaurantCommand();
        var restaurant = createRestaurantUseCase.createRestaurant(command)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        assertNotNull(restaurant);
        assertNotNull(restaurant.getRestaurantId());

        var fetchedRestaurant = restaurantQueryOperationsPort.getById(restaurant.getRestaurantId())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        assertNotNull(fetchedRestaurant);
        assertEquals(restaurant.getRestaurantId(), fetchedRestaurant.getRestaurantId());

        var fetchedUser = userQueryOperationsPort.getById(fetchedRestaurant.getOwnerId())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        assertNotNull(fetchedUser);
        assertNotNull(fetchedUser.getUserId());
        assertNotNull(fetchedUser.getUserId());
        assertEquals(command.getEmailAddress(), fetchedUser.getEmail());
        assertEquals(AccountType.OWNER, fetchedUser.getAccountType());

        cleanupRestaurant();
        cleanupUser();

    }

    @Test
    void testThatCreatingARestaurantWithTheSameOwnerEmailIsNotPossible() {
        var command = createRestaurantCommand();
        var restaurant = createRestaurantUseCase.createRestaurant(command)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        assertNotNull(restaurant);
        assertNotNull(restaurant.getRestaurantId());

        var fetchedRestaurant = restaurantQueryOperationsPort.getById(restaurant.getRestaurantId())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        assertNotNull(fetchedRestaurant);
        assertEquals(restaurant.getRestaurantId(), fetchedRestaurant.getRestaurantId());

        var throwable = createRestaurantUseCase.createRestaurant(createRestaurantCommand())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitFailure().getFailure();

        var failure = Failure.from(throwable);
        assertEquals(Code.ACCOUNT_EXISTS, failure.getError());

        cleanupRestaurant();
        cleanupUser();
    }

    @Test
    void testThatCreatingARestaurantWithTheSameNameIsNotPossible() {
        var command = createRestaurantCommand();
        var restaurant = createRestaurantUseCase.createRestaurant(command)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        assertNotNull(restaurant);
        assertNotNull(restaurant.getRestaurantId());

        var fetchedRestaurant = restaurantQueryOperationsPort.getById(restaurant.getRestaurantId())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        assertNotNull(fetchedRestaurant);
        assertEquals(restaurant.getRestaurantId(), fetchedRestaurant.getRestaurantId());

        var throwable = createRestaurantUseCase.createRestaurant(createRestaurantCommandSameName())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitFailure().getFailure();

        var failure = Failure.from(throwable);
        assertEquals(Code.RESTAURANT_EXISTS, failure.getError());

        cleanupRestaurant();
        cleanupUser();
    }

    CreateRestaurantUseCase.CreateRestaurantCommand createRestaurantCommand() {
        return CreateRestaurantUseCase.CreateRestaurantCommand.builder()
                .restaurantName("Starbucks Restaurant")
                .restaurantType(Restaurant.Type.CHAIN)
                .emailAddress("admin@starbucks.com")
                .firstName("Patek")
                .lastName("Phillipe")
                .password("88JfsirXsbsif")
                .build();
    }

    CreateRestaurantUseCase.CreateRestaurantCommand createRestaurantCommandSameName() {
        return CreateRestaurantUseCase.CreateRestaurantCommand.builder()
                .restaurantName("Starbucks Restaurant")
                .restaurantType(Restaurant.Type.CHAIN)
                .emailAddress("patek@starbucks.com")
                .firstName("Patek")
                .lastName("Phillipe")
                .password("88JfsirXsbsif")
                .build();
    }


}
