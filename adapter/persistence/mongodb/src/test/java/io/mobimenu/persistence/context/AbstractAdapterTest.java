package io.mobimenu.persistence.context;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.mongodb.MongoTestResource;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.mobimenu.domain.Restaurant;
import io.mobimenu.domain.Theme;
import io.mobimenu.domain.User;
import io.mobimenu.persistence.common.Fields;
import io.mobimenu.persistence.repository.RestaurantRepository;
import io.mobimenu.persistence.repository.ThemeRepository;
import io.mobimenu.persistence.repository.UserRepository;
import org.bson.types.ObjectId;

import javax.inject.Inject;

@QuarkusTestResource(MongoTestResource.class)
public abstract class AbstractAdapterTest {

    @Inject
    RestaurantRepository restaurantRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    ThemeRepository themeRepository;

    protected void cleanupRestaurant(String restaurantId) {
        restaurantRepository.delete(Fields._ID, new ObjectId(restaurantId))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted();
    }

    protected void cleanupUser(String userId) {
        userRepository.delete(Fields._ID, new ObjectId(userId))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted();
    }

   protected void cleanup(Theme.ThemeId themeId) {
        themeRepository.delete(Fields._ID, new ObjectId(themeId.themeId()))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted();
    }

}
