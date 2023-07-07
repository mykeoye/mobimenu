package io.mobimenu.persistence;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import io.mobimenu.domain.filters.MealFilter;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.mobimenu.common.api.Page;
import io.mobimenu.core.port.out.meal.MealQueryOperationsPort;
import io.mobimenu.domain.Category;
import io.mobimenu.domain.Meal;
import io.mobimenu.persistence.mapper.MealMapper;
import io.mobimenu.persistence.repository.MealRepository;
import lombok.RequiredArgsConstructor;

/**
 * This class implements all meal related queries to the datastore. Strictly retrievals only
 */
@RequiredArgsConstructor
public class MealQueryAdapter implements MealQueryOperationsPort {

    private final MealRepository repository;
    private final MealMapper mapper = MealMapper.INSTANCE;

    @Override
    public Uni<Meal> getMealById(Meal.MealId mealId) {
        return repository.findByMealId(mealId.mealId()).map(mapper::entityToDomainObject);
    }

    @Override
    public Uni<List<Meal>> getMealsByIdsAndStatus(List<String> mealIds, Meal.Status status) {
        return repository.findByMealIds(mealIds, status).map(mapper::entitiesToDomainObjects);
    }

    @Override
    public Uni<Meal> getByNameAndMenus(String name, Set<String> menuIds) {
        return repository.findByNameAndMenus(name, menuIds)
                .map(mapper::entityToDomainObject);
    }

    @Override
    public Uni<Tuple2<Long, List<Meal>>> getMealsByRestaurant(String restaurantId, Page page) {
        var countUni = repository.countAllByRestaurant(restaurantId);
        var mealUni = repository.findAllByRestaurant(restaurantId, page.getOffset(), page.getLimit())
                .map(meals -> Optional.ofNullable(meals).orElseGet(List::of))
                .map(mapper::entitiesToDomainObjects);
        return Uni.combine().all().unis(countUni, mealUni).asTuple();
    }

    @Override
    public Uni<Tuple2<Long, List<Meal>>> getMealsByFilter(MealFilter filter, Page page) {
        var countUni = repository.countByFilter(filter);
        var mealUni = repository.findByFilter(filter, page.getOffset(), page.getLimit())
                .map(meals -> Optional.ofNullable(meals).orElseGet(List::of))
                .map(mapper::entitiesToDomainObjects);
        return Uni.combine().all().unis(countUni, mealUni).asTuple();
    }

    @Override
    public Uni<Tuple2<Long, List<Meal>>> getMealsByMenu(String menuId, Page page) {
        var countUni = repository.countAllByMenu(menuId);
        var mealUni = repository.findAllByMenu(menuId, page.getOffset(), page.getLimit())
                .map(meals -> Optional.ofNullable(meals).orElseGet(List::of))
                .map(mapper::entitiesToDomainObjects);
        return Uni.combine().all().unis(countUni, mealUni).asTuple();
    }

    @Override
    public Uni<List<Meal>> getMealsByCategory(Category.CategoryId categoryId, Page page) {
        return repository.findAllByRestaurant(categoryId.categoryId(), page.getOffset(), page.getLimit())
                .map(meals -> Optional.ofNullable(meals).orElseGet(List::of))
                .map(mapper::entitiesToDomainObjects);
    }

}
