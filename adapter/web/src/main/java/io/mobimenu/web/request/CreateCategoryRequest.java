package io.mobimenu.web.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import io.mobimenu.core.port.in.category.CreateCategoryUseCase;

public record CreateCategoryRequest (

    @NotBlank
    String name,

    @NotNull
    Boolean active,

    @NotBlank
    String restaurantId) {

    public CreateCategoryUseCase.CreateCategoryCommand toCommand() {
        return CreateCategoryUseCase.CreateCategoryCommand.builder()
                .name(name.trim())
                .active(active)
                .restaurantId(restaurantId)
                .build();
    }

}
