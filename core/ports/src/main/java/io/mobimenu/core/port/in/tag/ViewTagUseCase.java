package io.mobimenu.core.port.in.tag;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.mobimenu.common.api.Page;
import io.mobimenu.domain.Tag;
import java.util.List;

public interface ViewTagUseCase {

    Uni<Tuple2<Long, List<Tag>>> loadTagsByRestaurant(String restaurantId, Page page);

}
