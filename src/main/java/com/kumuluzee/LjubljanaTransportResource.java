package com.kumuluzee;

import com.kumuluzee.LjubljanaTransportResponse.BicyclePath;
import com.kumuluzee.LjubljanaTransportResponse.CarsharingPath;
import com.kumuluzee.LjubljanaTransportResponse.LjubljanaTransportResponse;
import com.kumuluzee.LjubljanaTransportResponse.TaxiProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/")
public class LjubljanaTransportResource {

    @Inject
    private LjubljanaTransportService transportBean;

    @Operation(description = "Returns list of all possible public transportations from point A to point B with their duration and distance.", summary = "Ljubljana pulic transport list", tags = "Transport", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Ljubljana pulic transport list",
                    content = @Content(schema = @Schema(implementation = LjubljanaTransportResponse.class)),
                    headers = {@Header(name = "X-Context", schema = @Schema(implementation = Context.class))}
            )})
    @Parameter(in = ParameterIn.HEADER, description = "Custom Header To be Pass", name = "X-Context", required = true, content = @Content(schema = @Schema(implementation = Context.class)))
    @SecurityRequirement(name = "openid-connect")
    @GET
    public Response getLjubljanaTransport() throws Exception {
        LjubljanaTransportResponse transportResponse = transportBean.getTransportInfo();
        return transportResponse != null
                ? Response.ok(transportResponse).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @Operation(description = "Returns list of all taxis in Ljubljana.", summary = "Ljubljana taxis list", tags = "Taxis", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Ljubljana taxis list",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaxiProvider.class)))
            )})
    @GET
    @Path("taxis")
    public Response getTaxis() throws Exception {
        List<TaxiProvider> taxiProviders = transportBean.getTaxis();
        return taxiProviders != null
                ? Response.ok(taxiProviders).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @Operation(description = "Returns the best path from point A to point B with stops at BicikeLJ (bike rentals) stations", summary = "Ljubljana BicikeLJ (bike rentals) path from A to B", tags = "Bicycle", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Ljubljana BicikeLJ path from A to B",
                    content = @Content(schema = @Schema(implementation = BicyclePath.class)),
                    headers = {@Header(name = "X-Context", schema = @Schema(implementation = Context.class))}
            )})
    @Parameter(in = ParameterIn.HEADER, description = "Custom Header To be Pass", name = "X-Context", required = true, content = @Content(schema = @Schema(implementation = Context.class)))
    @GET
    @Path("bicycle")
    public Response getBicycles() throws Exception {
        BicyclePath bicyclePath = transportBean.getBicyclePath();
        return bicyclePath != null
                ? Response.ok(bicyclePath).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @Operation(description = "Returns the best path from point A to point B with stops at Avant2Go (electric cars) stations", summary = "Ljubljana Avant2Go (electric cars) path from A to B", tags = "Carsharing", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Ljubljana Avant2Go (electric cars) path from A to B",
                    content = @Content(schema = @Schema(implementation = CarsharingPath.class)),
                    headers = {@Header(name = "X-Context", schema = @Schema(implementation = Context.class))}
            )})
    @Parameter(in = ParameterIn.HEADER, description = "Custom Header To be Pass", name = "X-Context", required = true, content = @Content(schema = @Schema(implementation = Context.class)))
    @GET
    @Path("carsharing")
    public Response getCarsharing() throws Exception {
        CarsharingPath carsharingPath = transportBean.getCarsharingPath();
        return carsharingPath != null
                ? Response.ok(carsharingPath).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }
}
