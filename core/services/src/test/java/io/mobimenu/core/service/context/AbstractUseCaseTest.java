package io.mobimenu.core.service.context;

import io.mobimenu.persistence.repository.QrCodeRepository;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.mongodb.MongoTestResource;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.mobimenu.persistence.repository.CategoryRepository;
import io.mobimenu.persistence.repository.MealRepository;
import io.mobimenu.persistence.repository.MenuRepository;
import io.mobimenu.persistence.repository.OrderRepository;
import io.mobimenu.persistence.repository.RestaurantRepository;
import io.mobimenu.persistence.repository.TagRepository;
import io.mobimenu.persistence.repository.UserRepository;
import javax.inject.Inject;

@QuarkusTestResource(MongoTestResource.class)
public abstract class AbstractUseCaseTest {

    @Inject
    CategoryRepository categoryRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    MenuRepository menuRepository;

    @Inject
    RestaurantRepository restaurantRepository;

    @Inject
    MealRepository mealRepository;

    @Inject
    TagRepository tagRepository;

    @Inject
    OrderRepository orderRepository;

    @Inject
    QrCodeRepository qrCodeRepository;

    protected void cleanupRestaurant() {
        restaurantRepository.deleteAll()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted();
    }

    protected void cleanupUser() {
        userRepository.deleteAll()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted();
    }

    protected void cleanupMenu() {
        menuRepository.deleteAll()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted();
    }

    protected void cleanupCategory() {
        categoryRepository.deleteAll()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted();
    }

    protected void cleanupMeal() {
        mealRepository.deleteAll()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted();
    }

    protected void cleanupTag() {
        tagRepository.deleteAll()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted();
    }

    protected void cleanupOrders() {
        orderRepository.deleteAll()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted();
    }

    protected void cleanupQrCode() {
        qrCodeRepository.deleteAll()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted();
    }

}
