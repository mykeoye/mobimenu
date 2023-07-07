package io.mobimenu.core.service;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.mobimenu.common.api.Code;
import io.mobimenu.common.api.Failure;
import io.mobimenu.core.port.in.tag.CreateTagUseCase;
import io.mobimenu.core.port.out.restaurant.RestaurantSaveOperationsPort;
import io.mobimenu.domain.Restaurant;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;
import io.mobimenu.core.service.context.AbstractUseCaseTest;
import java.net.HttpURLConnection;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class CreateTagUseCaseTest extends AbstractUseCaseTest {

    @Inject
    CreateTagUseCase createTagUseCase;

    @Inject
    RestaurantSaveOperationsPort restaurantSaveOperationsPort;

    @Test
    @DisplayName("we cannot create a tag for a non-existent restaurant")
    void testNonExistentRestaurant()  {
        var throwable = executeCreateTagTest(ObjectId.get().toString());
        var error = Failure.from(throwable).getError();
        assertEquals(Code.RESTAURANT_NOT_FOUND, error);
        assertEquals(HttpURLConnection.HTTP_NOT_FOUND, error.getHttpCode());
    }

    @Test
    @DisplayName("we cannot create a tag with the same name in the same restaurant")
    void testForExistingTagInRestaurant()  {
        var restaurantId = restaurantSaveOperationsPort.saveRestaurant(
                Restaurant.withRequiredFields("Buchi's ChickWizz", Restaurant.Type.SINGLE),
                ObjectId.get().toString())
            .subscribe().withSubscriber(UniAssertSubscriber.create())
            .awaitItem().assertCompleted().getItem();

        var tagId = createTagUseCase.createTag(createTagCommand("sweet and sour", restaurantId.getRestaurantId()))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        assertNotNull(tagId);
        assertNotNull(tagId.tagId());

        var throwable = executeCreateTagTest(restaurantId.getRestaurantId());
        var error = Failure.from(throwable).getError();
        assertEquals(Code.TAG_EXISTS, error);
        assertEquals(HttpURLConnection.HTTP_CONFLICT, error.getHttpCode());
    }

    private Throwable executeCreateTagTest(String restaurantId) {
        return createTagUseCase.createTag(createTagCommand("sweet and sour", restaurantId))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitFailure().assertFailed().getFailure();
    }

    CreateTagUseCase.CreateTagCommand createTagCommand(String name, String restaurantId) {
        return CreateTagUseCase.CreateTagCommand.builder()
                .name(name)
                .restaurantId(restaurantId)
                .iconURI("https://s3-smartmenu.aws.com/somecoolicon.svg")
                .active(false)
                .build();
    }

}
