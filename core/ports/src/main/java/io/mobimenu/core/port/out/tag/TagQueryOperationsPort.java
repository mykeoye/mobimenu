package io.mobimenu.core.port.out.tag;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.mobimenu.common.api.Page;
import io.mobimenu.domain.Tag;

import java.util.List;

public interface TagQueryOperationsPort {

    Uni<Tag> getById(Tag.TagId tagId);

    Uni<Tag> getByNameAndRestaurant(String name, String restaurantId);

    Uni<Tuple2<Long, List<Tag>>> getTagsByRestaurant(String restaurantId, Page page);

}
