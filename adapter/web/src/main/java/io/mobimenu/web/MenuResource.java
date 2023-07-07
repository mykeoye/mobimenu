package io.mobimenu.web;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import io.mobimenu.core.port.in.meal.ViewMealsUseCase;
import io.mobimenu.domain.Meal;
import io.mobimenu.domain.Menu;
import io.mobimenu.web.common.Constant;
import io.smallrye.mutiny.Uni;
import io.mobimenu.common.api.Failure;
import io.mobimenu.common.api.Page;
import io.mobimenu.core.port.in.menu.CreateMenuUseCase;
import io.mobimenu.core.port.in.menu.ViewMenusUseCase;
import io.mobimenu.web.request.CreateMenuRequest;
import io.mobimenu.web.response.GenericResponse;
import io.mobimenu.web.response.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@Path("menus")
@RequiredArgsConstructor
public class MenuResource {

    private final CreateMenuUseCase createMenuUseCase;
    private final ViewMenusUseCase viewMenusUseCase;
    private final ViewMealsUseCase viewMealsUseCase;

    @POST
    @RolesAllowed("**")
    @APIResponse(
        responseCode = "200",
        description = "Success response",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
        schema = @Schema(implementation = GenericResponse.class,
        properties = {
            @SchemaProperty(name = Constant.DATA, implementation = Menu.class)
        }))
    )
    public Uni<Response> createMenu(@Valid @RequestBody CreateMenuRequest request) {
        return createMenuUseCase.createMenu(request.toCommand())
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
            @SchemaProperty(name = Constant.DATA, implementation = Menu.class, type = SchemaType.ARRAY)
        }))
    )
    public Uni<Response> viewMenusByRestaurant(@QueryParam("pageSize") int pageSize,
                                               @QueryParam("pageNum") int pageNum,
                                               @QueryParam("restaurantId") String restaurantId) {
        return viewMenusUseCase.loadMenusByRestaurant(restaurantId, Page.of(pageNum, pageSize))
                .map(tuple -> PagedResponse.of(tuple.getItem2(), tuple.getItem1()))
                .map(r -> Response.ok(r).build());
    }

    @GET
    @PermitAll
    @Path("{menuId}/meals")
    @APIResponse(
        responseCode = "200",
        description = "Success response",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
        schema = @Schema(implementation = PagedResponse.class,
        properties = {
            @SchemaProperty(name = Constant.DATA, implementation = Meal.class, type = SchemaType.ARRAY)
        }))
    )
    public Uni<Response> viewMealsByMenu(@PathParam("menuId") String menuId,
                                         @QueryParam("pageSize") int pageSize,
                                         @QueryParam("pageNum") int pageNum) {
        return viewMealsUseCase.loadMealsByMenu(menuId, Page.of(pageNum, pageSize))
                .map(tuple -> PagedResponse.of(tuple.getItem2(), tuple.getItem1()))
                .map(r -> Response.ok(r).build());
    }

}
