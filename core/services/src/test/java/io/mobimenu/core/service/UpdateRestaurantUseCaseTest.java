package io.mobimenu.core.service;

import io.mobimenu.common.api.Failure;
import io.mobimenu.core.port.in.restaurant.UpdateRestaurantUseCase;
import io.mobimenu.core.port.out.restaurant.RestaurantSaveOperationsPort;
import io.mobimenu.core.service.context.AbstractUseCaseTest;
import io.mobimenu.domain.PhoneNumber;
import io.mobimenu.domain.Restaurant;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Set;

import static io.mobimenu.common.api.Code.RESTAURANT_NOT_FOUND;
import static io.mobimenu.domain.enums.SalesChannel.DELIVERY;
import static io.mobimenu.domain.enums.SalesChannel.DINE_IN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class UpdateRestaurantUseCaseTest extends AbstractUseCaseTest {

    @Inject
    UpdateRestaurantUseCase updateRestaurantUseCase;

    @Inject
    RestaurantSaveOperationsPort restaurantSaveOperationsPort;


    @Test
    void updateNonExistentRestaurantShouldFail() {
        var command = getUpdateRestaurantCommand();
        var failedResponse = updateRestaurantUseCase.updateRestaurant(ObjectId.get().toString(), command)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitFailure().assertFailed().getFailure();

        var error = Failure.from(failedResponse).getError();
        assertEquals(RESTAURANT_NOT_FOUND, error);

    }

    @Test
    void updateExistingRestaurantShouldPass() {
        var restaurant = restaurantSaveOperationsPort.saveRestaurant(createRestaurant(),
                        null)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        var createdRestaurant = updateRestaurantUseCase.updateRestaurant(restaurant.getRestaurantId(), getUpdateRestaurantCommand())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

       assertNotNull(createdRestaurant);
       assertNotNull(createdRestaurant.getTheme());
       assertNotNull(createdRestaurant.getLogoUri());
       assertEquals(restaurant.getName(), createdRestaurant.getTheme().name());
       cleanupRestaurant();

    }

    private Restaurant createRestaurant() {
        return Restaurant.builder()
                .name("Vapiano Italinisch")
                .type(Restaurant.Type.CHAIN)
                .build();
    }

    private UpdateRestaurantUseCase.UpdateRestaurantCommand getUpdateRestaurantCommand() {
        return UpdateRestaurantUseCase.UpdateRestaurantCommand.builder()
                .email("test@test.com")
                .phoneNumber(new PhoneNumber("+234", "80123445"))
                .address("plot 222")
                .salesChannels(Set.of(DINE_IN, DELIVERY))
                .accentColour("#4285F4")
                .primaryColour("#4285F4")
                .logo("data:image/jpeg;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==")
                .build();
    }


}
