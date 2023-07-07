package io.mobimenu.web.context;

import io.mobimenu.core.port.in.tag.CreateTagUseCase;
import io.mobimenu.core.port.in.tag.ViewTagUseCase;
import io.mobimenu.core.port.out.restaurant.RestaurantQueryOperationsPort;
import io.mobimenu.core.port.out.tag.TagQueryOperationsPort;
import io.mobimenu.core.port.out.tag.TagSaveOperationsPort;
import io.mobimenu.core.service.tag.TagService;
import io.mobimenu.persistence.TagPersistenceAdapter;
import io.mobimenu.persistence.TagQueryAdapter;
import io.mobimenu.persistence.repository.TagRepository;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@ApplicationScoped
public class TagContext {

    @Produces
    @Singleton
    public TagQueryOperationsPort tagQueryOperationsPort(TagRepository repository) {
        return new TagQueryAdapter(repository);
    }

    @Produces
    @Singleton
    public TagSaveOperationsPort tagSaveOperationsPort(TagRepository repository) {
        return new TagPersistenceAdapter(repository);
    }

    @Produces
    @Singleton
    public CreateTagUseCase createTagUseCase(RestaurantQueryOperationsPort restaurantQueryOperationsPort,
                                             TagQueryOperationsPort tagQueryOperationsPort,
                                             TagSaveOperationsPort tagSaveOperationsPort) {
        return tagService(restaurantQueryOperationsPort, tagQueryOperationsPort, tagSaveOperationsPort);
    }

    @Produces
    @Singleton
    public ViewTagUseCase viewTagUseCase(RestaurantQueryOperationsPort restaurantQueryOperationsPort,
                                           TagQueryOperationsPort tagQueryOperationsPort,
                                           TagSaveOperationsPort tagSaveOperationsPort) {
        return tagService(restaurantQueryOperationsPort, tagQueryOperationsPort, tagSaveOperationsPort);
    }

    private TagService tagService(RestaurantQueryOperationsPort restaurantQueryOperationsPort,
                                  TagQueryOperationsPort tagQueryOperationsPort,
                                  TagSaveOperationsPort tagSaveOperationsPort) {
        return new TagService(restaurantQueryOperationsPort,
                tagQueryOperationsPort,
                tagSaveOperationsPort);
    }

}
