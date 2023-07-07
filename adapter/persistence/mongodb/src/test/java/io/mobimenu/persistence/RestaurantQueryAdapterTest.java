package io.mobimenu.persistence;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.mobimenu.persistence.context.AbstractAdapterTest;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;
import static io.mobimenu.persistence.context.TestObject.restaurant;
import static io.mobimenu.persistence.context.TestObject.userId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class RestaurantQueryAdapterTest  extends AbstractAdapterTest {

    @Inject
    RestaurantPersistenceAdapter restaurantPersistenceAdapter;

    @Inject
    RestaurantQueryAdapter restaurantQueryAdapter;

    @Test
    void canFetchCreatedRestaurantById() {
        var testRestaurant = restaurant();
        var testUserId = userId();
        var restaurant = restaurantPersistenceAdapter.saveRestaurant(testRestaurant, testUserId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        var fetched = restaurantQueryAdapter.getById(restaurant.getRestaurantId())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        assertNotNull(fetched);
        assertEquals(testRestaurant.getName(), fetched.getName());
        assertEquals(testRestaurant.getType(), fetched.getType());
        assertEquals(testUserId, fetched.getOwnerId());

        cleanupRestaurant(fetched.getRestaurantId());

    }

    @Test
    void canFetchCreatedRestaurantByName() {
        var testRestaurant = restaurant("Starbucks");
        var testUserId = userId();
        var restaurant = restaurantPersistenceAdapter.saveRestaurant(testRestaurant, testUserId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        var fetched = restaurantQueryAdapter.getByName(testRestaurant.getName())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().getItem();

        assertNotNull(fetched);
        assertEquals(testRestaurant.getName(), fetched.getName());
        assertEquals(testRestaurant.getType(), fetched.getType());
        assertEquals(testUserId, fetched.getOwnerId());

        cleanupRestaurant(fetched.getRestaurantId());

    }
}
