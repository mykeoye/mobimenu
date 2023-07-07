package io.mobimenu.persistence;

import io.mobimenu.domain.PhoneNumber;
import io.mobimenu.domain.Theme;
import io.mobimenu.domain.enums.SalesChannel;
import io.mobimenu.persistence.entity.ThemeEntity;
import io.smallrye.mutiny.Uni;
import io.mobimenu.core.port.out.restaurant.RestaurantSaveOperationsPort;
import io.mobimenu.domain.Restaurant;
import io.mobimenu.persistence.mapper.RestaurantMapper;
import io.mobimenu.persistence.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;

import java.util.Set;

/**
 * This class implements all restaurant persistence commands which include saves and updates
 */
@RequiredArgsConstructor
public class RestaurantPersistenceAdapter implements RestaurantSaveOperationsPort {

    private final RestaurantRepository repository;
    private final RestaurantMapper mapper = RestaurantMapper.INSTANCE;

    @Override
    public Uni<Restaurant> saveRestaurant(Restaurant restaurant, String userId) {
        var entity = mapper.domainObjectToPersistEntity(restaurant, userId);
        return repository.persist(entity).map(mapper::entityToDomainObject);
    }

    @Override
    public Uni<Restaurant> updateRestaurant(String restaurantId,
                                            String email,
                                            String address,
                                            PhoneNumber phoneNumber,
                                            Set<SalesChannel> salesChannels,
                                            Theme theme) {
        return repository.findByRestaurantId(restaurantId).map(
                restaurant -> {
                    if (restaurant.theme == null) {
                        restaurant.theme = new ThemeEntity();
                        restaurant.theme.name = restaurant.name;
                    }

                    restaurant.theme.accentColor = theme.accentColor();
                    restaurant.logoUri = theme.logo();
                    restaurant.theme.primaryColor = theme.primaryColor();
                    restaurant.address = address;
                    restaurant.email = email;
                    restaurant.phoneNumber = phoneNumber;
                    restaurant.salesChannels = salesChannels;

                    return restaurant;
                })
                .flatMap(repository::update)
                .map(mapper::entityToDomainObject);
    }

}
