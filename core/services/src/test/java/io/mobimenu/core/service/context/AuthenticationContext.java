package io.mobimenu.core.service.context;

import io.mobimenu.common.security.PasswordSecurity;
import io.mobimenu.common.util.AuthenticationTokenProvider;
import io.mobimenu.core.port.in.user.AuthenticateUserUseCase;
import io.mobimenu.core.port.out.user.UserQueryOperationsPort;
import io.mobimenu.core.service.user.AuthenticationService;
import io.mobimenu.domain.User;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import java.util.UUID;

@ApplicationScoped
public class AuthenticationContext {

    @Produces
    @Singleton
    public AuthenticateUserUseCase authenticateUserUseCase(UserQueryOperationsPort userQueryOperationsPort,
                                                           PasswordSecurity passwordSecurity) {
        return new AuthenticationService(userQueryOperationsPort, passwordSecurity);
    }

    @Singleton
    @Produces
    public AuthenticationTokenProvider<User> authenticationTokenProvider() {
        return principal -> UUID.randomUUID().toString();
    }

}
