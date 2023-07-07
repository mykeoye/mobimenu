package io.mobimenu.web;

import io.mobimenu.domain.Datum;
import io.mobimenu.web.common.Constant;
import io.mobimenu.web.response.GenericResponse;
import  io.smallrye.mutiny.Uni;
import io.mobimenu.core.port.in.analytics.ViewDashboardAnalyticsUseCase;
import io.mobimenu.domain.filters.DateRange;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;

@Path("analytics")
@RequiredArgsConstructor
public class AnalyticsResource {

    private final ViewDashboardAnalyticsUseCase viewDashboardCanvasUseCase;

    @GET
    @Path("{restaurantId}/order-summary")
    @RolesAllowed("**")
    @APIResponse(
        responseCode = "200",
        description = "Success response",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
        schema = @Schema(implementation = GenericResponse.class,
        properties = {
            @SchemaProperty(name = Constant.DATA, type = SchemaType.ARRAY, implementation = Datum.class)
        }))
    )
    public Uni<Response> orderSummary(@PathParam("restaurantId") String restaurantId,
                                      @QueryParam("from") LocalDate from,
                                      @QueryParam("to") LocalDate to) {
        return viewDashboardCanvasUseCase.loadOrderSummaryByRange(restaurantId, new DateRange(from, to))
                .map(GenericResponse::from)
                .map(r -> Response.ok(r).build());
    }

    @GET
    @Path("{restaurantId}/dashboard-summary")
    @RolesAllowed("**")
    @APIResponse(
        responseCode = "200",
        description = "Success response",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
        schema = @Schema(implementation = GenericResponse.class,
        properties = {
            @SchemaProperty(name = Constant.DATA, type = SchemaType.ARRAY, implementation = Datum.class)
        }))
    )
    public Uni<Response> dashboardSummary(@PathParam("restaurantId") String restaurantId,
                                      @QueryParam("from") LocalDate from,
                                      @QueryParam("to") LocalDate to) {
        return viewDashboardCanvasUseCase.loadDashboardSummaryByRange(restaurantId, new DateRange(from, to))
                .map(GenericResponse::from)
                .map(r -> Response.ok(r).build());
    }
}
