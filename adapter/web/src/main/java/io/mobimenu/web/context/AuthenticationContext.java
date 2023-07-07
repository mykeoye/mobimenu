package io.mobimenu.web.context;

import io.mobimenu.common.security.PasswordSecurity;
import io.mobimenu.common.util.AuthenticationTokenProvider;
import io.mobimenu.core.port.in.user.AuthenticateUserUseCase;
import io.mobimenu.core.port.out.user.UserQueryOperationsPort;
import io.mobimenu.core.service.user.AuthenticationService;
import io.mobimenu.domain.User;
import io.mobimenu.web.common.JwtTokenProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

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
    public AuthenticationTokenProvider<User> jwtUtil(@ConfigProperty(name = "mp.jwt.verify.issuer") String issuer) {
        return new JwtTokenProvider(issuer);
    }

}
