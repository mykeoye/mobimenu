package io.mobimenu.web;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import io.mobimenu.core.port.in.payment.ViewBankAccountUseCase;
import io.mobimenu.core.port.in.restaurant.UpdateRestaurantUseCase;
import io.mobimenu.core.port.in.user.ViewCustomerUseCase;
import io.mobimenu.core.port.in.setting.ViewSettingUseCase;
import io.mobimenu.domain.BankAccount;
import io.mobimenu.domain.Category;
import io.mobimenu.domain.Meal;
import io.mobimenu.domain.Menu;
import io.mobimenu.domain.Order;
import io.mobimenu.domain.Setting;
import io.mobimenu.domain.User;
import io.mobimenu.domain.enums.SettingType;
import io.mobimenu.domain.filters.MealFilter;
import io.mobimenu.domain.filters.QrCodeFilter;
import io.mobimenu.web.common.Constant;
import io.mobimenu.web.request.UpdateRestaurantRequest;
import io.smallrye.mutiny.Uni;
import io.mobimenu.common.api.Failure;
import io.mobimenu.common.api.Page;
import io.mobimenu.core.port.in.restaurant.CreateRestaurantUseCase;
import io.mobimenu.core.port.in.category.ViewCategoriesUseCase;
import io.mobimenu.core.port.in.user.ViewEmployeeUseCase;
import io.mobimenu.core.port.in.meal.ViewMealsUseCase;
import io.mobimenu.core.port.in.menu.ViewMenusUseCase;
import io.mobimenu.core.port.in.order.ViewOrderUseCase;
import io.mobimenu.core.port.in.qrcode.ViewQrCodesUseCase;
import io.mobimenu.domain.QrCode;
import io.mobimenu.domain.Restaurant;
import io.mobimenu.domain.filters.DateRange;
import io.mobimenu.domain.filters.OrderFilter;
import io.mobimenu.web.request.CreateRestaurantRequest;
import io.mobimenu.web.response.GenericResponse;
import io.mobimenu.web.response.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import java.time.LocalDate;

@Path("restaurants")
@RequiredArgsConstructor
public class RestaurantResource {

    private final CreateRestaurantUseCase createRestaurantUseCase;
    private final ViewMealsUseCase viewMealsUseCase;
    private final ViewMenusUseCase viewMenusUseCase;
    private final ViewCategoriesUseCase viewCategoriesUseCase;
    private final ViewQrCodesUseCase viewQrCodesUseCase;
    private final ViewOrderUseCase viewOrderUseCase;
    private final ViewEmployeeUseCase viewEmployeeUseCase;
    private final ViewCustomerUseCase viewCustomerUseCase;
    private final ViewSettingUseCase viewSettingUseCase;
    private final ViewBankAccountUseCase viewBankAccountUseCase;

    private final UpdateRestaurantUseCase updateRestaurantUseCase;

    @POST
    @PermitAll
    @APIResponse(
        responseCode = "200",
        description = "Success response",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
        schema = @Schema(implementation = GenericResponse.class,
        properties = {
            @SchemaProperty(name = Constant.DATA, implementation = Restaurant.class)
        }))
    )
    public Uni<Response> create(@Valid CreateRestaurantRequest request) {
        return createRestaurantUseCase.createRestaurant(request.toCommand())
                .map(GenericResponse::from)
                .map(r -> Response.ok(r).build())
                .onFailure().recoverWithItem(Failure::toResponse);
    }

    @GET
    @PermitAll
    @Path("/{restaurantId}/meals")
    @APIResponse(
        responseCode = "200",
        description = "Success response",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
        schema = @Schema(implementation = PagedResponse.class,
        properties = {
            @SchemaProperty(name = Constant.DATA, implementation = Meal.class, type = SchemaType.ARRAY)
        }))
    )
    public Uni<Response> getMeals(@PathParam("restaurantId") String restaurantId,
                                  @QueryParam("categoryId") String categoryId,
                                  @QueryParam("pageSize") int pageSize,
                                  @QueryParam("pageNum") int pageNum) {
        return viewMealsUseCase.loadMealsByFilter(MealFilter.from(restaurantId, categoryId), Page.of(pageNum, pageSize))
                .map(tuple -> PagedResponse.of(tuple.getItem2(), tuple.getItem1()))
                .map(r -> Response.ok(r).build());
    }

    @GET
    @Path("/{restaurantId}/menus")
    @RolesAllowed("**")
    @APIResponse(
        responseCode = "200",
        description = "Success response",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
        schema = @Schema(implementation = PagedResponse.class,
        properties = {
            @SchemaProperty(name = Constant.DATA, implementation = Menu.class, type = SchemaType.ARRAY)
        }))
    )
    public Uni<Response> getMenus(@PathParam("restaurantId") String restaurantId,
                                  @QueryParam("pageSize") int pageSize,
                                  @QueryParam("pageNum") int pageNum) {
        return viewMenusUseCase.loadMenusByRestaurant(restaurantId, Page.of(pageNum, pageSize))
                .map(tuple -> PagedResponse.of(tuple.getItem2(), tuple.getItem1()))
                .map(r -> Response.ok(r).build());
    }

    @GET
    @Path("/{restaurantId}/categories")
    @PermitAll
    @APIResponse(
        responseCode = "200",
        description = "Success response",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
        schema = @Schema(implementation = PagedResponse.class,
        properties = {
            @SchemaProperty(name = Constant.DATA, implementation = Category.class, type = SchemaType.ARRAY)
        }))
    )
    public Uni<Response> getCategories(@PathParam("restaurantId") String restaurantId,
                                       @QueryParam("pageSize") int pageSize,
                                       @QueryParam("pageNum") int pageNum) {
        return viewCategoriesUseCase.loadCategoriesByRestaurant(restaurantId, Page.of(pageNum, pageSize))
                .map(tuple -> PagedResponse.of(tuple.getItem2(), tuple.getItem1()))
                .map(r -> Response.ok(r).build());
    }

    @GET
    @Path("/{restaurantId}/qrcodes")
    @RolesAllowed("**")
    @APIResponse(
        responseCode = "200",
        description = "Success response",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
        schema = @Schema(implementation = PagedResponse.class,
        properties = {
            @SchemaProperty(name = Constant.DATA, implementation = QrCode.class, type = SchemaType.ARRAY)
        }))
    )
    public Uni<Response> viewQrCodesByRestaurant(@PathParam("restaurantId") String restaurantId,
                                                 @QueryParam("pageSize") int pageSize,
                                                 @QueryParam("pageNum") int pageNum,
                                                 @QueryParam("status") QrCode.Status status) {
        return viewQrCodesUseCase.loadByFilter(QrCodeFilter.from(restaurantId, status), Page.of(pageNum, pageSize))
                .map(tuple -> PagedResponse.of(tuple.getItem2(), tuple.getItem1()))
                .map(r -> Response.ok(r).build());
    }

    @GET
    @Path("/{restaurantId}/orders")
    @RolesAllowed("**")
    @APIResponse(
        responseCode = "200",
        description = "Success response",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
        schema = @Schema(implementation = PagedResponse.class,
        properties = {
            @SchemaProperty(name = Constant.DATA, implementation = Order.class, type = SchemaType.ARRAY)
        }))
    )
    public Uni<Response> viewOrdersByRestaurant(@PathParam("restaurantId") String restaurantId,
                                                @QueryParam("status") String status,
                                                @QueryParam("paymentStatus") String paymentStatus,
                                                @QueryParam("customerId") String customerId,
                                                @QueryParam("from") LocalDate from,
                                                @QueryParam("to") LocalDate to,
                                                @QueryParam("pageSize") int pageSize,
                                                @QueryParam("pageNum") int pageNum) {
        return viewOrderUseCase.loadOrdersByFilter(OrderFilter.from(restaurantId, status, paymentStatus, customerId, new DateRange(from, to)), Page.of(pageNum, pageSize))
                .map(tuple -> PagedResponse.of(tuple.getItem2(), tuple.getItem1()))
                .map(r -> Response.ok(r).build());
    }

    @GET
    @RolesAllowed("**")
    @Path("/{restaurantId}/employees")
    @APIResponse(
        responseCode = "200",
        description = "Success response",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
        schema = @Schema(implementation = PagedResponse.class,
        properties = {
            @SchemaProperty(name = Constant.DATA, implementation = User.class, type = SchemaType.ARRAY)
        }))
    )
    public Uni<Response> viewAllEmployees(@PathParam("restaurantId") String restaurantId,
                                         @QueryParam("pageSize") int pageSize,
                                         @QueryParam("pageNum") int pageNum) {
        return viewEmployeeUseCase.loadAllEmployeesByRestaurant(restaurantId, Page.of(pageSize,pageNum))
                .map(tuple -> PagedResponse.of(tuple.getItem2(), tuple.getItem1()))
                .map(r -> Response.ok(r).build());
    }

    @GET
    @RolesAllowed("**")
    @Path("{restaurantId}/customers")
    @APIResponse(
        responseCode = "200",
        description = "Success response",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
        schema = @Schema(implementation = PagedResponse.class,
        properties = {
            @SchemaProperty(name = Constant.DATA, implementation = User.class, type = SchemaType.ARRAY)
        }))
    )
    public Uni<Response> viewCustomers(@QueryParam("pageSize") int pageSize,
                                       @QueryParam("pageNum") int pageNum,
                                       @PathParam("restaurantId") String restaurantId) {
        return viewCustomerUseCase.loadCustomersByRestaurant(restaurantId, Page.of(pageNum, pageSize))
                .map(tuple -> PagedResponse.of(tuple.getItem2(), tuple.getItem1()))
                .map(r -> Response.ok(r).build());
    }

    @GET
    @PermitAll
    @Path("{restaurantId}/settings/{type}")
    @APIResponse(
        responseCode = "200",
        description = "Success response",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
        schema = @Schema(implementation = GenericResponse.class,
        properties = {
            @SchemaProperty(name = Constant.DATA, implementation = Setting.class)
        }))
    )
    public Uni<Response> getSettings(@PathParam("restaurantId") String restaurantId, @PathParam("type") String type) {
        return viewSettingUseCase.loadByTypeAndRestaurant(SettingType.from(type), restaurantId)
                .map(GenericResponse::from)
                .map(r -> Response.ok(r).build())
                .onFailure().recoverWithItem(Failure::toResponse);
    }

    @GET
    @RolesAllowed("**")
    @Path("{restaurantId}/bank-accounts")
    @APIResponse(
        responseCode = "200",
        description = "Success response",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
        schema = @Schema(implementation = GenericResponse.class,
        properties = {
            @SchemaProperty(name = Constant.DATA, implementation = BankAccount.class, type = SchemaType.ARRAY)
        }))
    )
    public Uni<Response> getBankAccounts(@PathParam("restaurantId") String restaurantId) {
        return viewBankAccountUseCase.loadByRestaurant(restaurantId)
                .map(GenericResponse::from)
                .map(r -> Response.ok(r).build())
                .onFailure().recoverWithItem(Failure::toResponse);
    }

    @PUT
    @RolesAllowed("**")
    @Path("{restaurantId}")
    @APIResponse(
            responseCode = "200",
            description = "Success response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema =  @Schema(implementation = GenericResponse.class,
            properties = {
                      @SchemaProperty(name = Constant.DATA, implementation = Restaurant.class)
            }))
    )
    public Uni<Response> updateRestaurant(@PathParam("restaurantId") String restaurantId,
                                          @RequestBody @Valid UpdateRestaurantRequest request) {
        return updateRestaurantUseCase.updateRestaurant(restaurantId, request.toCommand())
                .map(GenericResponse::from)
                .map(restaurant -> Response.ok(restaurant).build())
                .onFailure().recoverWithItem(Failure::toResponse);
    }

}
