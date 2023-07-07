package io.mobimenu.core.service.context;

import io.mobimenu.core.port.in.category.CreateCategoryUseCase;
import io.mobimenu.core.port.in.category.ViewCategoriesUseCase;
import io.mobimenu.core.port.out.category.CategoryQueryOperationsPort;
import io.mobimenu.core.port.out.category.CategorySaveOperationsPort;
import io.mobimenu.core.port.out.restaurant.RestaurantQueryOperationsPort;
import io.mobimenu.core.service.category.CategoryService;
import io.mobimenu.persistence.CategoryPersistenceAdapter;
import io.mobimenu.persistence.CategoryQueryAdapter;
import io.mobimenu.persistence.repository.CategoryRepository;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@ApplicationScoped
public class CategoryTestContext {

    @Produces
    @Singleton
    public CategorySaveOperationsPort categorySaveOperationsPort(CategoryRepository repository) {
        return new CategoryPersistenceAdapter(repository);
    }

    @Produces
    @Singleton
    public CategoryQueryOperationsPort categoryQueryOperationsPort(CategoryRepository repository) {
        return new CategoryQueryAdapter(repository);
    }

    @Produces
    @Singleton
    public CreateCategoryUseCase createCategoryUseCase(
            CategorySaveOperationsPort categorySaveOperationsPort,
            CategoryQueryOperationsPort categoryQueryOperationsPort,
            RestaurantQueryOperationsPort restaurantQueryOperationsPort) {
        return categoryService(categorySaveOperationsPort, categoryQueryOperationsPort, restaurantQueryOperationsPort);
    }

    @Produces
    @Singleton
    public ViewCategoriesUseCase viewCategoriesUseCase(
            CategorySaveOperationsPort categorySaveOperationsPort,
            CategoryQueryOperationsPort categoryQueryOperationsPort,
            RestaurantQueryOperationsPort restaurantQueryOperationsPort) {
        return categoryService(categorySaveOperationsPort, categoryQueryOperationsPort, restaurantQueryOperationsPort);
    }

    private CategoryService categoryService(
            CategorySaveOperationsPort categorySaveOperationsPort,
            CategoryQueryOperationsPort categoryQueryOperationsPort,
            RestaurantQueryOperationsPort restaurantQueryOperationsPort) {
        return new CategoryService(categoryQueryOperationsPort, categorySaveOperationsPort, restaurantQueryOperationsPort);
    }

}
