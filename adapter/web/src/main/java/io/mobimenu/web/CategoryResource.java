package io.mobimenu.web;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import io.mobimenu.domain.Category;
import io.mobimenu.web.common.Constant;
import io.smallrye.mutiny.Uni;
import io.mobimenu.common.api.Failure;
import io.mobimenu.common.api.Page;
import io.mobimenu.core.port.in.category.CreateCategoryUseCase;
import io.mobimenu.core.port.in.category.ViewCategoriesUseCase;
import io.mobimenu.web.request.CreateCategoryRequest;
import io.mobimenu.web.response.GenericResponse;
import io.mobimenu.web.response.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@Path("categories")
@RequiredArgsConstructor
public class CategoryResource {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final ViewCategoriesUseCase viewCategoriesUseCase;

    @POST
    @RolesAllowed("**")
    @APIResponse(
            responseCode = "200",
            description = "Success response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = GenericResponse.class,
            properties = {
                    @SchemaProperty(name = Constant.DATA, implementation = Category.CategoryId.class)
            }))
    )
    public Uni<Response> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        return createCategoryUseCase.createCategory(request.toCommand())
                .map(GenericResponse::from)
                .map(r -> Response.ok(r).build())
                .onFailure().recoverWithItem(Failure::toResponse);
    }

    @GET
    @APIResponse(
            responseCode = "200",
            description = "Success response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = PagedResponse.class,
            properties = {
                    @SchemaProperty(name = Constant.DATA, implementation = Category.class, type = SchemaType.ARRAY)
            }))
    )
    public Uni<Response> viewCategoryByRestaurant(@QueryParam("pageSize") int pageSize,
                                                  @QueryParam("pageNum") int pageNum,
                                                  @QueryParam("restaurantId") String restaurantId) {
        return viewCategoriesUseCase.loadCategoriesByRestaurant(restaurantId, Page.of(pageNum, pageSize))
                .map(tuple -> PagedResponse.of(tuple.getItem2(), tuple.getItem1()))
                .map(r -> Response.ok(r).build());
    }

}
