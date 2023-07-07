package io.mobimenu.core.service;

import io.mobimenu.common.api.Code;
import io.mobimenu.common.api.Failure;
import io.mobimenu.core.port.in.user.UpdateUserProfileUseCase;
import io.mobimenu.core.port.out.user.UserQueryOperationsPort;
import io.mobimenu.core.port.out.user.UserSaveOperationsPort;
import io.mobimenu.domain.User;
import io.smallrye.mutiny.Uni;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class UserProfileService implements UpdateUserProfileUseCase {
    private final UserQueryOperationsPort userQueryOperationsPort;
    private final UserSaveOperationsPort userSaveOperationsPort;

    @Override
    public Uni<User> updateUser(UpdateUserProfileCommand updateUserProfileCommand, String userId) {
        return userQueryOperationsPort.getById(userId)
                .onItem().ifNull().failWith(Failure.of(Code.USER_NOT_FOUND))
                .chain(() -> userQueryOperationsPort.getByEmail(updateUserProfileCommand.getEmail()))
                .flatMap(user -> {
                    if (user != null && !user.getUserId().equals(userId)) {
                        return Uni.createFrom().failure(Failure.of(Code.ACCOUNT_EXISTS));
                    }
                    return Uni.createFrom().item(user);
                })
                .flatMap(user -> userSaveOperationsPort.updateUserProfile(
                        userId,
                        updateUserProfileCommand.getFirstName(),
                        updateUserProfileCommand.getLastName(),
                        updateUserProfileCommand.getEmail())
                );
    }
}
