package io.mobimenu.web;

import io.mobimenu.common.util.AuthenticationTokenProvider;
import io.mobimenu.domain.User;
import io.smallrye.mutiny.Uni;
import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import io.mobimenu.common.api.Failure;
import lombok.RequiredArgsConstructor;
import javax.annotation.security.PermitAll;
import io.mobimenu.web.request.AuthenticationRequest;
import io.mobimenu.web.response.AuthenticationResponse;
import io.mobimenu.core.port.in.user.AuthenticateUserUseCase;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@Path("auth")
@RequiredArgsConstructor
public class AuthenticationResource {

    private final AuthenticationTokenProvider<User> jwtProvider;
    private final AuthenticateUserUseCase authenticateUserUseCase;

    @POST
    @PermitAll
    @APIResponse(
            responseCode = "200",
            description = "Success response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = AuthenticationResponse.class))
    )
    public Uni<Response> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return authenticateUserUseCase.authenticate(request.toCommand())
                .map(jwtProvider::generateToken)
                .map(token -> Response.ok(new AuthenticationResponse(token)).build())
                .onFailure().recoverWithItem(Failure::toResponse);
    }

}
