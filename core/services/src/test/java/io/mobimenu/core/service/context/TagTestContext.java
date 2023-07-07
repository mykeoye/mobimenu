package io.mobimenu.core.service.context;

import io.mobimenu.core.port.in.tag.CreateTagUseCase;
import io.mobimenu.core.port.out.restaurant.RestaurantQueryOperationsPort;
import io.mobimenu.core.port.out.tag.TagQueryOperationsPort;
import io.mobimenu.core.port.out.tag.TagSaveOperationsPort;
import io.mobimenu.core.service.tag.TagService;
import io.mobimenu.persistence.TagPersistenceAdapter;
import io.mobimenu.persistence.TagQueryAdapter;
import io.mobimenu.persistence.repository.TagRepository;
import javax.inject.Singleton;
import javax.enterprise.inject.Produces;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TagTestContext {

    @Singleton
    @Produces
    public TagQueryOperationsPort tagQueryOperationsPort(TagRepository repository) {
        return new TagQueryAdapter(repository);
    }


    @Produces
    @Singleton
    public TagSaveOperationsPort tagSaveOperationsPort(TagRepository repository) {
        return new TagPersistenceAdapter(repository);
    }

    @Singleton
    @Produces
    public CreateTagUseCase createTagUseCase(RestaurantQueryOperationsPort restaurantQueryOperationsPort,
                                             TagQueryOperationsPort tagQueryOperationsPort,
                                             TagSaveOperationsPort tagSaveOperationsPort) {
        return new TagService(restaurantQueryOperationsPort, tagQueryOperationsPort, tagSaveOperationsPort);
    }

}
