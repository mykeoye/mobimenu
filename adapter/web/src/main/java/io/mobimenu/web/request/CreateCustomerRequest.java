package io.mobimenu.web.request;

import io.mobimenu.core.port.in.user.CreateCustomerUseCase;
import io.mobimenu.domain.PhoneNumber;
import javax.validation.constraints.NotBlank;

public record CreateCustomerRequest(

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @NotBlank
        String email,

        PhoneNumber phoneNumber,

        @NotBlank
        String restaurantId) {

    public CreateCustomerUseCase.CreateCustomerCommand toCommand() {
        return CreateCustomerUseCase.CreateCustomerCommand.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .restaurantId(restaurantId)
                .build();
    }
}
