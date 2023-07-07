package io.mobimenu.web.request;

import io.mobimenu.core.port.in.user.CreateEmployeeUseCase;
import io.mobimenu.domain.PhoneNumber;

import javax.validation.constraints.NotBlank;

public record CreateEmployeeRequest(
        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @NotBlank
        String email,

        @NotBlank
        PhoneNumber phoneNumber,

        String title,

        String employeeId,

        @NotBlank
        String restaurantId
) {

    public CreateEmployeeUseCase.CreateEmployeeCommand toCommand(){
        return CreateEmployeeUseCase.CreateEmployeeCommand.builder()
                .firstName(firstName.trim())
                .lastName(lastName.trim())
                .email(email)
                .phoneNumber(phoneNumber)
                .title(title)
                .employeeId(employeeId)
                .build();
    }
}