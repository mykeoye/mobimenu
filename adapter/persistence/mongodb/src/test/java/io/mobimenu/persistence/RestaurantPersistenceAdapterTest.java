package io.mobimenu.persistence;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.mobimenu.persistence.context.AbstractAdapterTest;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;
import static io.mobimenu.persistence.context.TestObject.restaurant;
import static io.mobimenu.persistence.context.TestObject.userId;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class RestaurantPersistenceAdapterTest extends AbstractAdapterTest {

    @Inject
    RestaurantPersistenceAdapter restaurantPersistenceAdapter;

    @Test
    void canSuccessfullyCreateRestaurant() {
        var restaurant = restaurantPersistenceAdapter.saveRestaurant(restaurant(), userId())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();
        assertNotNull(restaurant);
        assertNotNull(restaurant);

        cleanupRestaurant(restaurant.getRestaurantId());
    }

}
