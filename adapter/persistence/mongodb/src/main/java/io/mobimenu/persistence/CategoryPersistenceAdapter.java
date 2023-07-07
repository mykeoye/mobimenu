package io.mobimenu.persistence;

import io.smallrye.mutiny.Uni;
import io.mobimenu.core.port.out.category.CategorySaveOperationsPort;
import io.mobimenu.domain.Category;
import io.mobimenu.persistence.mapper.CategoryMapper;
import io.mobimenu.persistence.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;

/**
 * This class implements all category persistence commands which include saves and updates
 */
@RequiredArgsConstructor
public class CategoryPersistenceAdapter implements CategorySaveOperationsPort {

    private final CategoryRepository repository;
    private final CategoryMapper mapper = CategoryMapper.INSTANCE;

    @Override
    public Uni<Category.CategoryId> saveCategory(Category category) {
        var entity = mapper.domainObjectToPersistEntity(category);
        return repository.persist(entity).map(persisted -> new Category.CategoryId(persisted.id.toString()));
    }

}
