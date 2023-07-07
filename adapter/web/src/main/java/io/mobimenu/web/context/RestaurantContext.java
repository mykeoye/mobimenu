package io.mobimenu.web.context;

import io.mobimenu.common.security.PasswordSecurity;
import io.mobimenu.core.port.in.restaurant.CreateRestaurantUseCase;
import io.mobimenu.core.port.in.restaurant.UpdateRestaurantUseCase;
import io.mobimenu.core.port.out.restaurant.RestaurantQueryOperationsPort;
import io.mobimenu.core.port.out.user.UserQueryOperationsPort;
import io.mobimenu.core.port.out.user.UserSaveOperationsPort;
import io.mobimenu.core.service.restaurant.RestaurantService;
import io.mobimenu.persistence.RestaurantQueryAdapter;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import io.mobimenu.persistence.RestaurantPersistenceAdapter;
import io.mobimenu.core.port.out.restaurant.RestaurantSaveOperationsPort;
import io.mobimenu.persistence.repository.RestaurantRepository;
import io.mobimenu.storage.StorageProvider;

@ApplicationScoped
public class RestaurantContext {

    @Produces
    @Singleton
    public RestaurantSaveOperationsPort restaurantSaveOperationsPort(RestaurantRepository repository) {
        return new RestaurantPersistenceAdapter(repository);
    }

    @Produces
    @Singleton
    public RestaurantQueryOperationsPort restaurantQueryOperationsPort(RestaurantRepository repository) {
        return new RestaurantQueryAdapter(repository);
    }

    @Produces
    @Singleton
    public CreateRestaurantUseCase createRestaurantUseCase(RestaurantQueryOperationsPort restaurantQueryOperationsPort,
                                                           RestaurantSaveOperationsPort restaurantSaveOperationsPort,
                                                           UserQueryOperationsPort userQueryOperationsPort,
                                                           UserSaveOperationsPort userSaveOperationsPort,
                                                           PasswordSecurity passwordSecurity,
                                                           StorageProvider storageProvider) {
        return restaurantService(restaurantQueryOperationsPort,
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
        return restaurantService(restaurantQueryOperationsPort,
                restaurantSaveOperationsPort,
                userQueryOperationsPort,
                userSaveOperationsPort,
                passwordSecurity,
                storageProvider);
    }

    private RestaurantService restaurantService(RestaurantQueryOperationsPort restaurantQueryOperationsPort,
                                                RestaurantSaveOperationsPort restaurantSaveOperationsPort,
                                                UserQueryOperationsPort userQueryOperationsPort,
                                                UserSaveOperationsPort userSaveOperationsPort,
                                                PasswordSecurity passwordSecurity,
                                                StorageProvider storageProvider) {
        return new RestaurantService(restaurantQueryOperationsPort,
                restaurantSaveOperationsPort,
                userQueryOperationsPort,
                userSaveOperationsPort,
                passwordSecurity,
                storageProvider);
    }

}
