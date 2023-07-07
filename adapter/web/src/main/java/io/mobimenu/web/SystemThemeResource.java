package io.mobimenu.web;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import io.mobimenu.domain.Theme;
import io.mobimenu.web.common.Constant;
import io.smallrye.mutiny.Uni;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import io.mobimenu.common.api.Page;
import io.mobimenu.web.response.PagedResponse;
import lombok.RequiredArgsConstructor;
import io.mobimenu.core.port.in.theme.ViewThemesUseCase;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@Path("systhemes")
@RequiredArgsConstructor
public class SystemThemeResource {

    private final ViewThemesUseCase viewThemesUseCase;

    @GET
    @RolesAllowed("**")
    @APIResponse(
            responseCode = "200",
            description = "Success response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = PagedResponse.class,
            properties = {
                    @SchemaProperty(name = Constant.DATA, implementation = Theme.class, type = SchemaType.ARRAY)
            }))
    )
    public Uni<Response> viewSystemThemes(@QueryParam("pageSize") int pageSize,
                                          @QueryParam("pageNum") int pageNum) {
        return viewThemesUseCase.loadSystemThemes(Page.of(pageNum, pageSize))
                .map(tuple -> PagedResponse.of(tuple.getItem2(), tuple.getItem1()))
                .map(r -> Response.ok(r).build());
    }

}
