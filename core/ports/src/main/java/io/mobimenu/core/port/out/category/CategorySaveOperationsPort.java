package io.mobimenu.core.port.out.category;

import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.Category;

public interface CategorySaveOperationsPort {

    Uni<Category.CategoryId> saveCategory(Category category);

}
