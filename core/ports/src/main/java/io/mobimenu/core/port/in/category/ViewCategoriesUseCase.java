package io.mobimenu.core.port.in.category;

import java.util.List;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.mobimenu.common.api.Page;
import io.mobimenu.domain.Category;

public interface ViewCategoriesUseCase {

    Uni<Tuple2<Long, List<Category>>> loadCategoriesByRestaurant(String restaurantId, Page page);

}
