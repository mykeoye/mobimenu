package io.mobimenu.core.service.context;

import io.mobimenu.common.security.DefaultPasswordSecurityProvider;
import io.mobimenu.common.security.PasswordSecurity;
import io.mobimenu.core.port.in.restaurant.CreateRestaurantUseCase;
import io.mobimenu.core.port.in.restaurant.UpdateRestaurantUseCase;
import io.mobimenu.core.port.out.restaurant.RestaurantQueryOperationsPort;
import io.mobimenu.core.port.out.restaurant.RestaurantSaveOperationsPort;
import io.mobimenu.core.port.out.user.UserQueryOperationsPort;
import io.mobimenu.core.port.out.user.UserSaveOperationsPort;
import io.mobimenu.core.service.restaurant.RestaurantService;
import io.mobimenu.persistence.RestaurantPersistenceAdapter;
import io.mobimenu.persistence.RestaurantQueryAdapter;
import io.mobimenu.persistence.repository.RestaurantRepository;
import io.mobimenu.storage.StorageProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@ApplicationScoped
public class SharedTestContext {

    @Produces
    @Singleton
    public RestaurantSaveOperationsPort restaurantPersistenceAdapter(RestaurantRepository repository) {
        return new RestaurantPersistenceAdapter(repository);
    }

    @Produces
    @Singleton
    public RestaurantQueryOperationsPort restaurantQueryAdapter(RestaurantRepository repository) {
        return new RestaurantQueryAdapter(repository);
    }

    @Produces
    @Singleton
    public PasswordSecurity passwordSecurity() {
        return new DefaultPasswordSecurityProvider();
    }

    @Produces
    @Singleton
    public CreateRestaurantUseCase createRestaurantUseCase(RestaurantQueryOperationsPort restaurantQueryOperationsPort,
                                                           RestaurantSaveOperationsPort restaurantSaveOperationsPort,
                                                           UserQueryOperationsPort userQueryOperationsPort,
                                                           UserSaveOperationsPort userSaveOperationsPort,
                                                           PasswordSecurity passwordSecurity,
                                                           StorageProvider storageProvider) {
        return getRestaurantService(restaurantQueryOperationsPort,
                restaurantSaveOperationsPort,
                userQueryOperationsPort,
                userSaveOperationsPort,
                passwordSecurity,
                storageProvider);
    }

    @Produces
    @Singleton
    public UpdateRestaurantUseCase updateRestaurantUseCase(RestaurantQueryOperationsPort restaurantQueryOperationsPort,
                                                           RestaurantSaveOperationsPort restaurantSaveOperationsPort,
                                                           UserQueryOperationsPort userQueryOperationsPort,
                                                           UserSaveOperationsPort userSaveOperationsPort,
                                                           PasswordSecurity passwordSecurity,
                                                           StorageProvider storageProvider) {
        return getRestaurantService(restaurantQueryOperationsPort,
                restaurantSaveOperationsPort,
                userQueryOperationsPort,
                userSaveOperationsPort,
                passwordSecurity,
                storageProvider);
    }

    private static RestaurantService getRestaurantService(RestaurantQueryOperationsPort restaurantQueryOperationsPort, RestaurantSaveOperationsPort restaurantSaveOperationsPort, UserQueryOperationsPort userQueryOperationsPort, UserSaveOperationsPort userSaveOperationsPort, PasswordSecurity passwordSecurity, StorageProvider storageProvider) {
        return new RestaurantService(restaurantQueryOperationsPort,
                restaurantSaveOperationsPort,
                userQueryOperationsPort,
                userSaveOperationsPort,
                passwordSecurity,
                storageProvider);
    }

}
