package io.mobimenu.web;

import io.mobimenu.core.port.in.payment.ReQueryPaymentUseCase;
import io.mobimenu.core.port.in.payment.VerifyPaymentUseCase;
import io.mobimenu.domain.Payment;
import io.mobimenu.web.common.Constant;
import io.mobimenu.web.response.GeneratePaymentRefResponse;
import io.mobimenu.web.response.PaymentStatusResponse;
import io.smallrye.mutiny.Uni;
import io.mobimenu.common.api.Failure;
import io.mobimenu.core.port.in.payment.InitiatePaymentUseCase;
import io.mobimenu.web.request.GeneratePaymentRefRequest;
import io.mobimenu.web.response.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("payments")
@RequiredArgsConstructor
public class PaymentResource {

    private final InitiatePaymentUseCase initiatePaymentUseCase;
    private final ReQueryPaymentUseCase reQueryPaymentUseCase;
    private final VerifyPaymentUseCase verifyPaymentUseCase;

    @POST
    @RolesAllowed("**")
    @APIResponse(
            responseCode = "200",
            description = "Success response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = GenericResponse.class,
            properties = {
                    @SchemaProperty(name = Constant.DATA, implementation = GeneratePaymentRefResponse.class)
            }))
    )
    public Uni<Response> generatePaymentRef(@Valid GeneratePaymentRefRequest request) {
        return initiatePaymentUseCase.generateTransactionRef(request.toCommand())
                .map(GeneratePaymentRefResponse::new)
                .map(GenericResponse::from)
                .map(r -> Response.ok(r).build())
                .onFailure().recoverWithItem(Failure::toResponse);
    }

    @PUT
    @Path("{paymentId}/requery")
    @RolesAllowed("**")
    @APIResponse(
            responseCode = "200",
            description = "Success response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = GenericResponse.class,
            properties = {
                    @SchemaProperty(name = Constant.DATA, implementation = PaymentStatusResponse.class)
            }))
    )
    public Uni<Response> requeryPayment(@PathParam("paymentId") String paymentId) {
        return reQueryPaymentUseCase.requeryPayment(new Payment.PaymentId(paymentId))
                .map(status -> new PaymentStatusResponse(status.name()))
                .map(GenericResponse::from)
                .map(r -> Response.ok(r).build())
                .onFailure().recoverWithItem(Failure::toResponse);
    }

    @PUT
    @Path("{paymentId}/verify")
    @RolesAllowed("**")
    @APIResponse(
            responseCode = "200",
            description = "Success response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = GenericResponse.class,
            properties = {
                    @SchemaProperty(name = Constant.DATA, implementation = PaymentStatusResponse.class)
            }))
    )
    public Uni<Response> verifyPayment(@PathParam("paymentId") String paymentId) {
        return verifyPaymentUseCase.verifyPayment(new Payment.PaymentId(paymentId))
                .map(status -> new PaymentStatusResponse(status.name()))
                .map(GenericResponse::from)
                .map(r -> Response.ok(r).build())
                .onFailure().recoverWithItem(Failure::toResponse);
    }

}
