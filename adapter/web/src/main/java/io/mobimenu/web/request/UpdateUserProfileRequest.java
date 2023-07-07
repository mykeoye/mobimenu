package io.mobimenu.web.request;

import io.mobimenu.core.port.in.user.UpdateUserProfileUseCase;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record UpdateUserProfileRequest(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank
        @Email
        String email) {

    public UpdateUserProfileUseCase.UpdateUserProfileCommand command() {
        return UpdateUserProfileUseCase.UpdateUserProfileCommand.builder()
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }
}
