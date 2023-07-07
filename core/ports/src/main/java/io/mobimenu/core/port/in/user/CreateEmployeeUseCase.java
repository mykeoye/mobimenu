package io.mobimenu.core.port.in.user;

import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.PhoneNumber;
import io.mobimenu.domain.User;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

public interface CreateEmployeeUseCase {

    Uni<User> createEmployee(CreateEmployeeCommand command);

    @Value
    @Builder
    @ToString
    class CreateEmployeeCommand {
        String firstName;
        String lastName;
        PhoneNumber phoneNumber;
        String email;
        String password;
        String restaurantId;
        String title;
        String employeeId;
    }

}