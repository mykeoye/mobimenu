package io.mobimenu.web;

import io.mobimenu.domain.Order;
import io.mobimenu.web.common.Constant;
import io.mobimenu.web.response.UpdateOrderPaymentStatusResponse;
import io.mobimenu.web.response.UpdateOrderStatusResponse;
import io.smallrye.mutiny.Uni;
import io.mobimenu.common.api.Failure;
import io.mobimenu.core.port.in.order.PlaceOrderUseCase;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import io.mobimenu.core.port.in.order.ProcessOrderUseCase;
import io.mobimenu.web.request.CreateOrderRequest;
import io.mobimenu.web.request.ProcessOrderRequest;
import io.mobimenu.web.request.UpdateOrderStatusRequest;
import io.mobimenu.web.request.UpdatePaymentRequest;
import io.mobimenu.web.response.GenericResponse;
import lombok.RequiredArgsConstructor;
import io.mobimenu.core.port.in.order.UpdateOrderStatusUseCase;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@Path("orders")
@RequiredArgsConstructor
public class OrderResource {

    private final ProcessOrderUseCase processOrderUseCase;
    private final PlaceOrderUseCase placeOrderUseCase;
    private final UpdateOrderStatusUseCase updateOrderStatusUseCase;

    @POST
    @PermitAll
    @APIResponse(
	    responseCode = "200",
	    description = "Success response",
	    content = @Content(mediaType = MediaType.APPLICATION_JSON,
        schema = @Schema(implementation = GenericResponse.class,
        properties = {
            @SchemaProperty(name = Constant.DATA, implementation = Order.class)
        }))
    )
    public Uni<Response> createOrder(@NotBlank(message = "Missing idempotency key from headers")
                                     @HeaderParam("X-Idempotency-key") String idempotencyKey,
                                     @Valid CreateOrderRequest request) {
        return placeOrderUseCase.placeOrder(request.toCommand(idempotencyKey))
                .map(GenericResponse::from)
                .map(r -> Response.ok(r).build())
                .onFailure().recoverWithItem(Failure::toResponse);
    }

    @PUT
    @RolesAllowed("**")
    @Path("{orderId}/process")
    @APIResponse(
        responseCode = "200",
        description = "Success response",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
        schema = @Schema(implementation = GenericResponse.class,
        properties = {
            @SchemaProperty(name = Constant.DATA, implementation = Order.class)
        }))
    )
    public Uni<Response> processOrder(@PathParam ("orderId") String orderId,
                                      @Valid ProcessOrderRequest request) {
        return processOrderUseCase.processOrder(request.toCommand(orderId))
                .map(GenericResponse::from)
                .map(r -> Response.ok(r).build())
                .onFailure().recoverWithItem(Failure::toResponse);
    }

    @PUT
    @RolesAllowed("**")
    @Path("{orderId}/update-status")
    @APIResponse(
        responseCode = "200",
        description = "Success response",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
        schema = @Schema(implementation = GenericResponse.class,
        properties = {
            @SchemaProperty(name = Constant.DATA, implementation = UpdateOrderStatusResponse.class)
        }))
    )
    public Uni<Response> updateOrderStatus(@PathParam ("orderId") String orderId,
                                           @Valid UpdateOrderStatusRequest request) {
        return updateOrderStatusUseCase.updateOrderStatus(request.toCommand())
                .map(status -> new UpdateOrderStatusResponse(status.name()))
                .map(GenericResponse::from)
                .map(r -> Response.ok(r).build())
                .onFailure().recoverWithItem(Failure::toResponse);
    }

    @PUT
    @RolesAllowed("**")
    @Path("{orderId}/update-payment")
    @APIResponse(
        responseCode = "200",
        description = "Success response",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
        schema = @Schema(implementation = GenericResponse.class,
        properties = {
            @SchemaProperty(name = Constant.DATA, implementation = UpdateOrderPaymentStatusResponse.class)
        }))
    )
    public Uni<Response> updatePaymentStatus(@PathParam ("orderId") String orderId,
                                             @Valid UpdatePaymentRequest request) {
        return updateOrderStatusUseCase.updatePaymentStatus(request.toCommand())
                .map(paymentStatus -> new UpdateOrderPaymentStatusResponse(paymentStatus.name()))
                .map(GenericResponse::from)
                .map(r -> Response.ok(r).build())
                .onFailure().recoverWithItem(Failure::toResponse);
    }

}
