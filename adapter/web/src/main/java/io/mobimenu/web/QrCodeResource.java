package io.mobimenu.web;

import io.mobimenu.core.port.in.qrcode.UpdateQrCodeUseCase;
import io.mobimenu.domain.structs.QrMenu;
import io.mobimenu.web.common.Constant;
import io.mobimenu.web.request.UpdateQrCodeRequest;
import io.smallrye.mutiny.Uni;
import io.mobimenu.common.api.Failure;
import io.mobimenu.common.api.Page;
import io.mobimenu.core.port.in.qrcode.CreateQrCodeUseCase;
import io.mobimenu.core.port.in.qrcode.ViewQrCodesUseCase;
import io.mobimenu.domain.QrCode;
import io.mobimenu.web.request.CreateQrCodeRequest;
import io.mobimenu.web.request.UpdateQrCodeStatusRequest;
import io.mobimenu.web.response.GenericResponse;
import io.mobimenu.web.response.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
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

@Path("qrcodes")
@RequiredArgsConstructor
public class QrCodeResource {

    private final CreateQrCodeUseCase createQrCodeUseCase;
    private final ViewQrCodesUseCase viewQrCodesUseCase;
    private final UpdateQrCodeUseCase updateQrCodeUseCase;

    @POST
    @RolesAllowed("**")
    @APIResponse(
        responseCode = "200",
        description = "Success response",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
        schema = @Schema(implementation = GenericResponse.class,
        properties = {
            @SchemaProperty(name = Constant.DATA, implementation = QrCode.class)
        }))
    )
    public Uni<Response> createQrCode(@Valid @RequestBody CreateQrCodeRequest request) {
        return request.validate().chain(() -> createQrCodeUseCase.createQrCode(request.toCommand()))
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
            @SchemaProperty(name = Constant.DATA, implementation = QrCode.class, type = SchemaType.ARRAY)
        }))
    )
    public Uni<Response> viewQrCodesByRestaurant(@QueryParam("pageSize") int pageSize,
                                                 @QueryParam("pageNum") int pageNum,
                                                 @QueryParam("restaurantId") String restaurantId,
                                                 @QueryParam("status") QrCode.Status status) {
        return viewQrCodesUseCase.loadByRestaurant(restaurantId, Page.of(pageNum, pageSize))
                .map(tuple -> PagedResponse.of(tuple.getItem2(), tuple.getItem1()))
                .map(r -> Response.ok(r).build());
    }

    @PUT
    @RolesAllowed("**")
    @Path("{qrcodeId}/status-update")
    @APIResponse(
        responseCode = "200",
        description = "Success response",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
        schema = @Schema(implementation = GenericResponse.class,
        properties = {
            @SchemaProperty(name = Constant.DATA, implementation = QrCode.class)
        }))
    )
    public Uni<Response> updateQrCodeStatus(@Valid @RequestBody UpdateQrCodeStatusRequest request, @PathParam("qrcodeId") String qrCodeId) {
        return updateQrCodeUseCase.updateStatus(request.toCommand(qrCodeId))
                .map(GenericResponse::from)
                .map(r -> Response.ok(r).build())
                .onFailure().recoverWithItem(Failure::toResponse);
    }

    @PUT
    @RolesAllowed("**")
    @Path("{qrcodeId}")
    @APIResponse(
        responseCode = "200",
        description = "Success response",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
        schema = @Schema(implementation = GenericResponse.class,
        properties = {
            @SchemaProperty(name = Constant.DATA, implementation = QrCode.class)
        }))
    )
    public Uni<Response> updateQrCode(@Valid UpdateQrCodeRequest request, @PathParam("qrcodeId") String qrCodeId) {
        return updateQrCodeUseCase.updateQrCode(request.toCommand(qrCodeId))
                .map(GenericResponse::from)
                .map(r -> Response.ok(r).build())
                .onFailure().recoverWithItem(Failure::toResponse);
    }

    @GET
    @Path("{qrcodeId}/menu")
    @PermitAll
    @APIResponse(
        responseCode = "200",
        description = "Success response",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
        schema = @Schema(implementation = GenericResponse.class,
        properties = {
            @SchemaProperty(name = Constant.DATA, implementation = QrMenu.class)
        }))
    )
    public Uni<Response> viewMenuForQrCode(@PathParam("qrcodeId") String qrCodeId) {
        return viewQrCodesUseCase.loadMenuByQrId(qrCodeId)
                .map(GenericResponse::from)
                .map(r -> Response.ok(r).build())
                .onFailure().recoverWithItem(Failure::toResponse);
    }

}
