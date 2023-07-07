package io.mobimenu.web.request;

import javax.validation.constraints.NotBlank;
import io.mobimenu.core.port.in.user.AuthenticateUserUseCase;

/**
 * A user authentication request
 *
 * @param username  user's username which is most likely their email
 * @param password  user's password
 */
public record AuthenticationRequest(

        @NotBlank
        String username,

        @NotBlank
        String password
){

        public AuthenticateUserUseCase.AuthenticateUserCommand toCommand() {
            return AuthenticateUserUseCase.AuthenticateUserCommand.builder()
                    .username(this.username)
                    .password(this.password)
                    .build();
        }
}