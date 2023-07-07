package io.mobimenu.core.service;

import io.mobimenu.common.api.Code;
import io.mobimenu.common.api.Failure;
import io.mobimenu.common.security.PasswordSecurity;
import io.mobimenu.core.port.in.user.UpdateUserProfileUseCase;
import io.mobimenu.core.port.out.user.UserSaveOperationsPort;
import io.mobimenu.core.service.context.AbstractUseCaseTest;
import io.mobimenu.domain.User;
import io.mobimenu.domain.enums.AccountType;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class UpdateUserProfileUseCaseTest extends AbstractUseCaseTest {
    private static final String TEST_EMAIL = "johndoe@testmail.com";
    private static final String TEST_EMAIL_2 = "janedoe@testmail.com";
    @Inject
    UpdateUserProfileUseCase updateUserProfileUseCase;

    @Inject
    PasswordSecurity passwordSecurity;

    @Inject
    UserSaveOperationsPort userSaveOperationsPort;


    @Test
    @DisplayName("Updating existing user profile should be successful")
    void updateExistingUserProfileShouldPass() {
        var createdUser = createUser(TEST_EMAIL);
        var userUpdateRequest = buildUserProfileCommand(TEST_EMAIL_2);

        var updateUser = updateUserProfileUseCase.updateUser(userUpdateRequest, createdUser.getUserId())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();
        assertEquals(createdUser.getUserId(), updateUser.getUserId());
        assertEquals(userUpdateRequest.getEmail(), updateUser.getEmail());
        assertEquals(userUpdateRequest.getLastName(), updateUser.getLastName());
        assertEquals(userUpdateRequest.getFirstName(), updateUser.getFirstName());
    }

    @Test
    @DisplayName("Updating non-existent user profile should fail")
    void updateNonExistentUserProfileShouldFail() {
        var userUpdateRequest = buildUserProfileCommand(TEST_EMAIL_2);

        var failedResponse = updateUserProfileUseCase.updateUser(userUpdateRequest, "6375515b456f2359de7cded7")
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitFailure().assertFailed().getFailure();
        var error = Failure.from(failedResponse).getError();
        assertEquals(Code.USER_NOT_FOUND, error);
    }

    @Test
    @DisplayName("Updating existing user profile with already existing email address should fail")
    void updateExistingUserProfileWithAlreadyExistingEmailShouldFail() {
        createUser(TEST_EMAIL_2);
        var createdUser = createUser(TEST_EMAIL);
        var userUpdateRequest = buildUserProfileCommand(TEST_EMAIL_2);

        var failedResponse = updateUserProfileUseCase.updateUser(userUpdateRequest, createdUser.getUserId())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitFailure().assertFailed().getFailure();
        var error = Failure.from(failedResponse).getError();
        assertEquals(Code.ACCOUNT_EXISTS, error);
    }

    @AfterEach
    void performUserCleanUp(){
        cleanupUser();

    }

    private static UpdateUserProfileUseCase.UpdateUserProfileCommand buildUserProfileCommand(String email) {
        return UpdateUserProfileUseCase.UpdateUserProfileCommand.builder()
                .email(email)
                .firstName("Jane")
                .lastName("doe")
                .build();
    }

    private User createUser(String email) {
        var user = User.withRequiredFields(
                "John",
                "Doe",
                email,
                AccountType.OWNER,
                passwordSecurity.hash("password"));
        return userSaveOperationsPort.saveUser(user)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem().assertCompleted().getItem();

    }
}
