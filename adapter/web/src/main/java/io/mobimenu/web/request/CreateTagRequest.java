package io.mobimenu.web.request;

import io.mobimenu.core.port.in.tag.CreateTagUseCase;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record CreateTagRequest(

        @NotBlank
        String name,

        @NotBlank
        String iconURI,

        @NotNull
        Boolean active,

        @NotBlank
        String restaurantId) {

    public CreateTagUseCase.CreateTagCommand toCommand() {
        return CreateTagUseCase.CreateTagCommand.builder()
                .name(name.trim())
                .restaurantId(restaurantId)
                .iconURI(iconURI)
                .active(active)
                .build();
    }

}
