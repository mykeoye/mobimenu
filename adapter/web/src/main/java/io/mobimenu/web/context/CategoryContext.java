package io.mobimenu.web.context;

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
public class CategoryContext {

    @Produces
    @Singleton
    public CategoryQueryOperationsPort categoryQueryOperationsPort(CategoryRepository repository) {
        return new CategoryQueryAdapter(repository);
    }

    @Produces
    @Singleton
    public CategorySaveOperationsPort categorySaveOperationsPort(CategoryRepository repository) {
        return new CategoryPersistenceAdapter(repository);
    }

    @Produces
    @Singleton
    public CreateCategoryUseCase createCategoryUseCase(CategoryQueryOperationsPort categoryQueryOperationsPort,
                                                       CategorySaveOperationsPort categorySaveOperationsPort,
                                                       RestaurantQueryOperationsPort restaurantQueryOperationsPort) {
        return categoryService(categoryQueryOperationsPort,
                categorySaveOperationsPort,
                restaurantQueryOperationsPort);
    }

    @Produces
    @Singleton
    public ViewCategoriesUseCase viewCategoriesUseCase(CategoryQueryOperationsPort categoryQueryOperationsPort,
                                                       CategorySaveOperationsPort categorySaveOperationsPort,
                                                       RestaurantQueryOperationsPort restaurantQueryOperationsPort) {
        return categoryService(categoryQueryOperationsPort,
                categorySaveOperationsPort,
                restaurantQueryOperationsPort);
    }

    private CategoryService categoryService(CategoryQueryOperationsPort categoryQueryOperationsPort,
                                            CategorySaveOperationsPort categorySaveOperationsPort,
                                            RestaurantQueryOperationsPort restaurantQueryOperationsPort) {
        return new CategoryService(categoryQueryOperationsPort,
                categorySaveOperationsPort,
                restaurantQueryOperationsPort);
    }
}
