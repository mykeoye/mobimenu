package io.mobimenu.web;

import io.mobimenu.web.common.Constant;
import io.smallrye.mutiny.Uni;
import io.mobimenu.common.api.Failure;
import io.mobimenu.core.port.in.user.CreateEmployeeUseCase;
import io.mobimenu.core.port.in.user.UpdateEmployeeUseCase;
import io.mobimenu.core.port.in.user.ViewEmployeeUseCase;
import io.mobimenu.domain.User;
import io.mobimenu.web.request.CreateEmployeeRequest;
import io.mobimenu.web.request.UpdateEmployeeRequest;
import io.mobimenu.web.response.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("employees")
@RequiredArgsConstructor
public class EmployeeResource {

    private final CreateEmployeeUseCase createEmployeeUseCase;
    private final UpdateEmployeeUseCase updateEmployeeUseCase;
    private final ViewEmployeeUseCase viewEmployeeUseCase;

    @POST
    @RolesAllowed("**")
    @APIResponse(
        responseCode = "200",
        description = "Success response",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
        schema = @Schema(implementation = GenericResponse.class,
        properties = {
            @SchemaProperty(name = Constant.DATA, implementation = User.class)
        }))
    )
    public Uni<Response> createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {
        return createEmployeeUseCase.createEmployee(request.toCommand())
                .map(GenericResponse::from)
                .map(res -> Response.ok(res).build())
                .onFailure().recoverWithItem(Failure::toResponse);
    }

    @GET
    @Path("{userId}")
    @RolesAllowed("**")
    @APIResponse(
        responseCode = "200",
        description = "Success response",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
        schema = @Schema(implementation = GenericResponse.class,
        properties = {
            @SchemaProperty(name = Constant.DATA, implementation = User.class)
        }))
    )
    public Uni<Response> viewSingleEmployee(@PathParam("userId") String userId){
        return viewEmployeeUseCase.loadSingleEmployee(userId)
                .map(GenericResponse::from)
                .map(res -> Response.ok(res).build())
                .onFailure().recoverWithItem(Failure::toResponse);
    }

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
    public Uni<Response> updateEmployee(@Valid @RequestBody UpdateEmployeeRequest request,
                                        @PathParam("userId") String userId) {
        return updateEmployeeUseCase.updateEmployeeDetails(request.command(), userId)
                .map(GenericResponse::from)
                .map(res -> Response.ok(res).build())
                .onFailure().recoverWithItem(Failure::toResponse);
    }

    @DELETE
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
    public Uni<Response> deleteEmployee(@PathParam("userId") String userId) {
        return updateEmployeeUseCase.deleteEmployee(userId)
                .map(GenericResponse::from)
                .map(res -> Response.ok(res).build())
                .onFailure().recoverWithItem(Failure::toResponse);
    }
}