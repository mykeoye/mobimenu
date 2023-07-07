package io.mobimenu.web;

import io.mobimenu.common.api.Failure;
import io.mobimenu.common.api.Page;
import io.mobimenu.core.port.in.user.CreateCustomerUseCase;
import io.mobimenu.core.port.in.user.ViewCustomerUseCase;
import io.mobimenu.core.port.in.order.ViewOrderUseCase;
import io.mobimenu.domain.Order;
import io.mobimenu.domain.User;
import io.mobimenu.domain.filters.DateRange;
import io.mobimenu.domain.filters.OrderFilter;
import io.mobimenu.web.common.Constant;
import io.mobimenu.web.request.CreateCustomerRequest;
import io.mobimenu.web.response.GenericResponse;
import io.mobimenu.web.response.PagedResponse;
import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
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
import java.time.LocalDate;

@Path("customers")
@RequiredArgsConstructor
public class CustomerResource {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final ViewOrderUseCase viewOrderUseCase;
    private final ViewCustomerUseCase viewCustomerUseCase;

    @POST
    @PermitAll
    @APIResponse(
        responseCode = "200",
        description = "Success response",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
        schema = @Schema(implementation = GenericResponse.class,
        properties = {
            @SchemaProperty(name = Constant.DATA, implementation = User.class)
        }))
    )
    public Uni<Response> createCustomer(@Valid CreateCustomerRequest request) {
        return createCustomerUseCase.createCustomer(request.toCommand())
                .map(GenericResponse::from)
                .map(r -> Response.ok(r).build())
                .onFailure().recoverWithItem(Failure::toResponse);
    }

    @GET
    @Path("{customerId}")
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
    public Uni<Response> viewCustomer(@PathParam("customerId") String customerId) {
        return viewCustomerUseCase.loadCustomerById(customerId)
                .map(GenericResponse::from)
                .map(r -> Response.ok(r).build())
                .onFailure().recoverWithItem(Failure::toResponse);
    }

    @GET
    @Path("{customerId}/orders")
    @RolesAllowed("**")
    @APIResponse(
        responseCode = "200",
        description = "Success response",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
        schema = @Schema(implementation = GenericResponse.class,
        properties = {
            @SchemaProperty(name = Constant.DATA, implementation = Order.class, type = SchemaType.ARRAY)
        }))
    )
    public Uni<Response> viewOrders(@PathParam("customerId") String customerId,
                                    @QueryParam("from") LocalDate from,
                                    @QueryParam("to") LocalDate to,
                                    @QueryParam("pageSize") int pageSize,
                                    @QueryParam("pageNum") int pageNum) {
        return viewOrderUseCase.loadOrdersByFilter(OrderFilter.from(null, null, null, customerId, new DateRange(from, to)), Page.of(pageNum, pageSize))
                .map(tuple -> PagedResponse.of(tuple.getItem2(), tuple.getItem1()))
                .map(r -> Response.ok(r).build());
    }

}
