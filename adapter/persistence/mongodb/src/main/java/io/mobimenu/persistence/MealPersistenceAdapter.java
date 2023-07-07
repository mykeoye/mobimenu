package io.mobimenu.persistence;

import io.smallrye.mutiny.Uni;
import io.mobimenu.core.port.out.meal.MealSaveOperationsPort;
import io.mobimenu.domain.Meal;
import io.mobimenu.persistence.mapper.MealMapper;
import io.mobimenu.persistence.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;

/**
 * This class implements all meal persistence commands which include saves and updates
 */
@RequiredArgsConstructor
public class MealPersistenceAdapter implements MealSaveOperationsPort {

    private final MealRepository repository;
    private final MealMapper mapper = MealMapper.INSTANCE;

    @Override
    public Uni<Meal.MealId> saveMeal(Meal meal) {
        var entity = mapper.domainObjectToPersistEntity(meal);
        return repository.persist(entity).map(persisted -> new Meal.MealId(persisted.id.toString()));
    }

    @Override
    public Uni<Void> saveMeals(List<Meal> meals) {
        return repository.persist(mapper.domainObjectsToPersistentEntity(meals));
    }

}
