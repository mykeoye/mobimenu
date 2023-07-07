package io.mobimenu.core.service;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.mobimenu.common.api.Code;
import io.mobimenu.common.api.Failure;
import io.mobimenu.common.security.PasswordSecurity;
import io.mobimenu.core.port.in.user.AuthenticateUserUseCase;
import io.mobimenu.core.port.out.user.UserSaveOperationsPort;
import io.mobimenu.core.service.context.AbstractUseCaseTest;
import io.mobimenu.domain.enums.AccountType;
import io.mobimenu.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class AuthenticateUserUseCaseTest extends AbstractUseCaseTest {

    @Inject
    PasswordSecurity passwordSecurity;

    @Inject
    UserSaveOperationsPort userSaveOperationsPort;

    @Inject
    AuthenticateUserUseCase authenticateUserUseCase;


    @Test
    @DisplayName("a valid user authenticates successfully")
    void testValidUserAuthentication() {
        var userToCreate = User.withRequiredFields(
                "Smith",
                "Johnson",
                "smith.johnson@gmail.com",
                AccountType.OWNER,
                passwordSecurity.hash("password@1234"));
        var userId = userSaveOperationsPort.saveUser(userToCreate)
        .subscribe().withSubscriber(UniAssertSubscriber.create())
        .awaitItem().assertCompleted().getItem();
        assertNotNull(userId);
        assertNotNull(userId.getUserId());

        var user = authenticateUserUseCase.authenticate(authenticateUserCommand("smith.johnson@gmail.com", "password@1234"))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

        assertNotNull(user);
        assertNotNull(user.getUserId());
        assertEquals(userToCreate.getEmail(), user.getEmail());
        assertEquals(userToCreate.getFirstName(), user.getFirstName());
        assertEquals(userToCreate.getLastName(), user.getLastName());

    }

    @Test
    @DisplayName("authenticating a non-existent user should fail with an authentication exception")
    void testAuthenticatingNonExistentUser() {
        var throwable = authenticateUserUseCase.authenticate(authenticateUserCommand("smith@gmail.com", "password"))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitFailure().assertFailed().getFailure();

        var failure = Failure.from(throwable);
        assertEquals(Code.AUTHENTICATION_ERROR, failure.getError());
    }

    AuthenticateUserUseCase.AuthenticateUserCommand authenticateUserCommand(String email, String password) {
        return AuthenticateUserUseCase.AuthenticateUserCommand
                .builder()
                .username(email)
                .password(password)
                .build();
    }

    @AfterEach
    void cleanup() {
        cleanupUser();
    }

}
