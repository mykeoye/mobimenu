package io.mobimenu.web;

import io.mobimenu.domain.Tag;
import io.mobimenu.web.common.Constant;
import io.smallrye.mutiny.Uni;
import io.mobimenu.common.api.Page;
import io.mobimenu.core.port.in.tag.ViewTagUseCase;
import io.mobimenu.web.request.CreateTagRequest;
import io.mobimenu.web.response.GenericResponse;
import io.mobimenu.web.response.PagedResponse;
import lombok.RequiredArgsConstructor;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import io.mobimenu.core.port.in.tag.CreateTagUseCase;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@Path("tags")
@RequiredArgsConstructor
public class TagResource {

    private final CreateTagUseCase createTagUseCase;
    private final ViewTagUseCase viewTagUseCase;

    @POST
    @RolesAllowed("**")
    @APIResponse(
            responseCode = "200",
            description = "Success response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = GenericResponse.class,
            properties = {
                    @SchemaProperty(name = Constant.DATA, implementation = Tag.TagId.class)
            }))
    )
    public Uni<Response> createTag(@Valid @RequestBody CreateTagRequest request) {
        return createTagUseCase.createTag(request.toCommand())
                .map(GenericResponse::from)
                .map(r -> Response.ok(r).build());
    }

    @GET
    @RolesAllowed("**")
    @APIResponse(
            responseCode = "200",
            description = "Success response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = PagedResponse.class,
            properties = {
                    @SchemaProperty(name = Constant.DATA, implementation = Tag.class, type = SchemaType.ARRAY)
            }))
    )
    public Uni<Response> viewTag(@QueryParam("pageSize") int pageSize,
                                 @QueryParam("pageNum") int pageNum,
                                 @QueryParam("restaurantId") String restaurantId) {
        return viewTagUseCase.loadTagsByRestaurant(restaurantId, Page.of(pageNum, pageSize))
                .map(tuple -> PagedResponse.of(tuple.getItem2(), tuple.getItem1()))
                .map(r -> Response.ok(r).build());
    }

}
