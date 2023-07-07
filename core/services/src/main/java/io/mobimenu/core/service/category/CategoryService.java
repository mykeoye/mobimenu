package io.mobimenu.core.service.category;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.mobimenu.common.api.Code;
import io.mobimenu.common.api.Failure;
import io.mobimenu.common.api.Page;
import io.mobimenu.core.port.in.category.ViewCategoriesUseCase;
import io.mobimenu.core.port.out.category.CategoryQueryOperationsPort;
import io.mobimenu.core.port.out.category.CategorySaveOperationsPort;
import io.mobimenu.core.port.out.restaurant.RestaurantQueryOperationsPort;
import io.mobimenu.domain.Category;
import lombok.RequiredArgsConstructor;
import io.mobimenu.core.port.in.category.CreateCategoryUseCase;

import java.util.List;

@RequiredArgsConstructor
public class CategoryService implements CreateCategoryUseCase, ViewCategoriesUseCase {

    private final CategoryQueryOperationsPort categoryQueryOperationsPort;
    private final CategorySaveOperationsPort categorySaveOperationsPort;
    private final RestaurantQueryOperationsPort restaurantQueryOperationsPort;

    @Override
    public Uni<Category.CategoryId> createCategory(CreateCategoryCommand command) {
        var restaurantId = command.getRestaurantId();
        var name = command.getName().trim();
        return restaurantQueryOperationsPort.getById(restaurantId)
                .onItem().ifNull().failWith(Failure.of(Code.RESTAURANT_NOT_FOUND))
                .flatMap(restaurant -> categoryQueryOperationsPort.getByNameAndRestaurant(name, restaurant.getRestaurantId()))
                .onItem().ifNotNull().failWith(Failure.of(Code.CATEGORY_EXISTS))
                .flatMap(ignored -> categorySaveOperationsPort.saveCategory(
                        Category.withRequiredFields(
                                name,
                                command.getActive() ? Category.Status.ACTIVE : Category.Status.INACTIVE,
                                restaurantId, false)
                ));
    }

    @Override
    public Uni<Tuple2<Long, List<Category>>> loadCategoriesByRestaurant(String restaurantId, Page page) {
        return categoryQueryOperationsPort.getAllByRestaurantId(restaurantId, page);
    }

}
