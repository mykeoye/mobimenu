package io.mobimenu.core.service.tag;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.mobimenu.common.api.Code;
import io.mobimenu.common.api.Failure;
import io.mobimenu.common.api.Page;
import io.mobimenu.core.port.in.tag.ViewTagUseCase;
import io.mobimenu.core.port.out.tag.TagQueryOperationsPort;
import io.mobimenu.domain.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.mobimenu.core.port.in.tag.CreateTagUseCase;
import io.mobimenu.core.port.out.tag.TagSaveOperationsPort;
import io.mobimenu.core.port.out.restaurant.RestaurantQueryOperationsPort;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class TagService implements CreateTagUseCase, ViewTagUseCase {

    private final RestaurantQueryOperationsPort restaurantQueryOperationsPort;
    private final TagQueryOperationsPort tagQueryOperationsPort;
    private final TagSaveOperationsPort tagSaveOperationsPort;

    @Override
    public Uni<Tag.TagId> createTag(CreateTagCommand command) {
        var restaurantId = command.getRestaurantId();
        return restaurantQueryOperationsPort.getById(restaurantId)
                .onItem().ifNull().failWith(() -> Failure.of(Code.RESTAURANT_NOT_FOUND))
                .flatMap(ignored -> tagQueryOperationsPort.getByNameAndRestaurant(command.getName(), restaurantId))
                .onItem().ifNotNull().failWith(Failure.of(Code.TAG_EXISTS))
                .flatMap(ignored -> tagSaveOperationsPort.saveTag(
                        Tag.withRequiredFields(command.getName(), command.getIconURI(),
                                command.getActive() ? Tag.Status.ACTIVE : Tag.Status.INACTIVE, restaurantId)
                ));
    }

    @Override
    public Uni<Tuple2<Long, List<Tag>>> loadTagsByRestaurant(String restaurantId, Page page) {
        return tagQueryOperationsPort.getTagsByRestaurant(restaurantId, page);
    }

}
