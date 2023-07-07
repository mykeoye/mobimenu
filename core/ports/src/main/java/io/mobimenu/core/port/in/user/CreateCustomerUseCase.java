package io.mobimenu.core.port.in.user;

import io.mobimenu.domain.PhoneNumber;
import io.mobimenu.domain.User;
import io.smallrye.mutiny.Uni;
import lombok.Builder;
import lombok.Value;

public interface CreateCustomerUseCase {

    Uni<User> createCustomer(CreateCustomerCommand command);

    @Builder
    @Value
    class CreateCustomerCommand {
        String firstName;
        String lastName;
        String email;
        PhoneNumber phoneNumber;
        String restaurantId;
    }

}
