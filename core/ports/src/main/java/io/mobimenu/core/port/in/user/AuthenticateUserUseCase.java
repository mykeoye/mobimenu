package io.mobimenu.core.port.in.user;

import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.User;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;

public interface AuthenticateUserUseCase {

    Uni<User> authenticate(AuthenticateUserCommand command);

    @Builder
    @Value
    @ToString
    @RequiredArgsConstructor
    class AuthenticateUserCommand  {
        String username;
        String password;
    }

}
