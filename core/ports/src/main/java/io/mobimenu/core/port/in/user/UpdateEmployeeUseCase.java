package io.mobimenu.core.port.in.user;

import io.mobimenu.domain.User;
import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.PhoneNumber;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;
import java.util.Map;

public interface UpdateEmployeeUseCase {

    Uni<User> updateEmployeeDetails(UpdateEmployeeCommand command, String userId);

    Uni<String> deleteEmployee(String userId);

    @Value
    @Builder
    @ToString
    @RequiredArgsConstructor
    class UpdateEmployeeCommand{
        String firstName;
        String lastName;
        PhoneNumber phoneNumber;
        Map<String, String> accountProperties;
    }

}
