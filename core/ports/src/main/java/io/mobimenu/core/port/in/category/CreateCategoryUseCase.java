package io.mobimenu.core.port.in.category;

import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.Category;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public interface CreateCategoryUseCase {

    Uni<Category.CategoryId> createCategory(CreateCategoryCommand command);

    @Builder
    @Value
    @ToString
    @RequiredArgsConstructor
    class CreateCategoryCommand {
        @NotBlank String name;
        @NotNull Boolean active;
        @NotBlank String restaurantId;
    }

}
