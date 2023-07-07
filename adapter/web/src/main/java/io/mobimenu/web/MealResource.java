package io.mobimenu.web;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import io.mobimenu.web.common.Constant;
import io.smallrye.mutiny.Uni;
import io.mobimenu.common.api.Failure;
import io.mobimenu.common.api.Page;
import io.mobimenu.domain.Meal;
import io.mobimenu.web.request.CreateMealRequest;
import io.mobimenu.web.response.GenericResponse;
import io.mobimenu.web.response.PagedResponse;
import lombok.RequiredArgsConstructor;
import io.mobimenu.core.port.in.meal.CreateMealUseCase;
import io.mobimenu.core.port.in.meal.ViewMealsUseCase;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@Path("meals")
@RequiredArgsConstructor
public class MealResource {

    private final CreateMealUseCase createMealUseCase;
    private final ViewMealsUseCase viewMealsUseCase;

    @POST
    @RolesAllowed("**")
    @APIResponse(
            responseCode = "200",
            description = "Success response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = GenericResponse.class,
            properties = {
                    @SchemaProperty(name = Constant.DATA, implementation = Meal.MealId.class)
            }))
    )
    public Uni<Response> createMeal(@Valid @RequestBody CreateMealRequest request) {
        return createMealUseCase.createMeal(request.toCommand())
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
                    @SchemaProperty(name = Constant.DATA, implementation = Meal.class, type = SchemaType.ARRAY)
            }))
    )
    public Uni<Response> viewMealByFilters(@QueryParam("pageSize") int pageSize,
                                           @QueryParam("pageNum") int pageNum,
                                           @QueryParam("restaurantId") String restaurantId) {
        return viewMealsUseCase.loadMealsByRestaurant(restaurantId, Page.of(pageNum, pageSize))
                .map(tuple -> PagedResponse.of(tuple.getItem2(), tuple.getItem1()))
                .map(r -> Response.ok(r).build());
    }

    @GET
    @Path("{mealId}")
    @RolesAllowed("**")
    @APIResponse(
            responseCode = "200",
            description = "Success response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = GenericResponse.class,
            properties = {
                    @SchemaProperty(name = Constant.DATA, implementation = Meal.class)
            }))
    )
    public Uni<Response> viewMealById(@PathParam("mealId") String mealId) {
        return viewMealsUseCase.loadMealById(new Meal.MealId(mealId))
                .map(GenericResponse::from)
                .map(r -> Response.ok(r).build());
    }

}
