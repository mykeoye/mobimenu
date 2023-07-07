package io.mobimenu.core.port.out.category;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.mobimenu.common.api.Page;
import io.mobimenu.domain.Category;

import java.util.List;

public interface CategoryQueryOperationsPort {

    Uni<Category> getByNameAndRestaurant(String name, String restaurantId);

    Uni<Category> getById(Category.CategoryId categoryId);

    Uni<Tuple2<Long, List<Category>>> getAllByRestaurantId(String restaurantId, Page page);

}
