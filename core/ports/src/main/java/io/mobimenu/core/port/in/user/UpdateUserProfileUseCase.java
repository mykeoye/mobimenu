package io.mobimenu.core.port.in.user;

import io.mobimenu.domain.User;
import io.smallrye.mutiny.Uni;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

public interface UpdateUserProfileUseCase {
    Uni<User> updateUser(UpdateUserProfileCommand updateUserProfileCommand, String userId);

    @Value
    @Builder
    @RequiredArgsConstructor
    class UpdateUserProfileCommand {
        String firstName;
        String lastName;
        String email;
    }
}
