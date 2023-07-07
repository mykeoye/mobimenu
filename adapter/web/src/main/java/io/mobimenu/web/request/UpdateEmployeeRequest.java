package io.mobimenu.web.request;

import io.mobimenu.core.port.in.user.UpdateEmployeeUseCase;
import io.mobimenu.domain.PhoneNumber;

import java.util.Map;

public record UpdateEmployeeRequest(
        String firstName,
        String lastName,
        PhoneNumber phoneNumber,
        String employeeId,
        String title) {
    public UpdateEmployeeUseCase.UpdateEmployeeCommand command(){
        return UpdateEmployeeUseCase.UpdateEmployeeCommand.builder()
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .accountProperties(
                        Map.of("employeeId", employeeId, "title", title)
                ).build();
    }
}
