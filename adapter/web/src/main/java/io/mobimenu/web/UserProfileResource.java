package io.mobimenu.web;

import io.mobimenu.common.api.Failure;
import io.mobimenu.core.port.in.user.UpdateUserProfileUseCase;
import io.mobimenu.domain.User;
import io.mobimenu.web.common.Constant;
import io.mobimenu.web.request.UpdateUserProfileRequest;
import io.mobimenu.web.response.GenericResponse;
import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("users")
@RequiredArgsConstructor
public class UserProfileResource {
    private final UpdateUserProfileUseCase updateUserProfileUseCase;

    @PUT
    @RolesAllowed("**")
    @Path("{userId}")
    @APIResponse(
            responseCode = "200",
            description = "Success response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = GenericResponse.class,
            properties = {
                     @SchemaProperty(name = Constant.DATA, implementation = User.class)
            }))
    )
    public Uni<Response> updateUserProfile(@Valid @RequestBody UpdateUserProfileRequest request,
                                           @PathParam("userId") String userId) {
        return updateUserProfileUseCase.updateUser(request.command(), userId)
                .map(GenericResponse::from)
                .map(res -> Response.ok(res).build())
                .onFailure().recoverWithItem(Failure::toResponse);
    }
}
