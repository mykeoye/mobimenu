package io.mobimenu.core.service.user;

import io.smallrye.mutiny.Uni;
import io.mobimenu.common.api.Code;
import io.mobimenu.common.api.Failure;
import io.mobimenu.common.security.PasswordSecurity;
import io.mobimenu.core.port.in.user.AuthenticateUserUseCase;
import io.mobimenu.core.port.out.user.UserQueryOperationsPort;
import io.mobimenu.domain.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthenticationService implements AuthenticateUserUseCase {

    private final UserQueryOperationsPort userQueryOperationsPort;
    private final PasswordSecurity passwordSecurity;

    @Override
    public Uni<User> authenticate(AuthenticateUserCommand command) {
        return userQueryOperationsPort.getByEmail(command.getUsername().trim())
                .onItem().ifNull().failWith(Failure.of(Code.AUTHENTICATION_ERROR))
                .map(user -> {
                    if (passwordSecurity.matches(command.getPassword(), user.getPassword())) {
                        return user;
                    }
                    return null;
                })
                .onItem().ifNull().failWith(Failure.of(Code.AUTHENTICATION_ERROR));
    }

}
