package io.mobimenu.web;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import io.mobimenu.common.api.Failure;
import io.mobimenu.core.port.in.setting.UpdateSettingUseCase;
import io.mobimenu.domain.Setting;
import io.mobimenu.web.common.Constant;
import io.mobimenu.web.request.UpdateSettingRequest;
import io.mobimenu.web.response.GenericResponse;
import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@Path("settings")
@RequiredArgsConstructor
public class SettingResource {

    private final UpdateSettingUseCase updateSettingUseCase;

    @PUT
    @RolesAllowed("**")
    @APIResponse(
        responseCode = "200",
        description = "Success response",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
        schema = @Schema(implementation = GenericResponse.class,
        properties = {
            @SchemaProperty(name = Constant.DATA, implementation = Setting.class)
        }))
    )
    public Uni<Response> updateSetting(@Valid UpdateSettingRequest request) {
        return updateSettingUseCase.updateSetting(request.toCommand())
                .map(GenericResponse::from)
                .map(r -> Response.ok(r).build())
                .onFailure().recoverWithItem(Failure::toResponse);
    }

}
