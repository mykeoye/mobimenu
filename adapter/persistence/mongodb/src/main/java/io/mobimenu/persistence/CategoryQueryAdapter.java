package io.mobimenu.persistence;

import io.smallrye.mutiny.Uni;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.smallrye.mutiny.tuples.Tuple2;
import io.mobimenu.common.api.Page;
import io.mobimenu.domain.Category;
import io.mobimenu.core.port.out.category.CategoryQueryOperationsPort;
import io.mobimenu.persistence.mapper.CategoryMapper;
import io.mobimenu.persistence.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;

/**
 * This class implements all category related queries to the datastore. Strictly retrievals only
 */
@RequiredArgsConstructor
public class CategoryQueryAdapter implements CategoryQueryOperationsPort {

    private final CategoryRepository repository;
    private final CategoryMapper mapper = CategoryMapper.INSTANCE;

    @Override
    public Uni<Category> getByNameAndRestaurant(String name, String restaurantId) {
        return repository.findByNameAndRestaurant(name, restaurantId)
                .map(mapper::entityToDomainObject);
    }

    @Override
    public Uni<Category> getById(Category.CategoryId categoryId) {
        return repository.findByCategoryId(categoryId.categoryId())
                .map(mapper::entityToDomainObject);
    }

    @Override
    public Uni<Tuple2<Long, List<Category>>> getAllByRestaurantId(String restaurantId, Page page) {
        var countUni = repository.countAllByRestaurant(restaurantId);
        var categoriesUni = repository.findAllByRestaurant(restaurantId, page.getOffset(), page.getLimit())
                .map(categories -> Optional.ofNullable(categories).orElseGet(List::of))
                .map(categories -> categories.stream().map(mapper::entityToDomainObject).collect(Collectors.toList()));
        return Uni.combine().all().unis(countUni, categoriesUni).asTuple();
    }

}
