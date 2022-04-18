package com.kumuluzee;

import com.kumuluzee.LjubljanaTransportResponse.BicyclePath;
import com.kumuluzee.LjubljanaTransportResponse.CarsharingPath;
import com.kumuluzee.LjubljanaTransportResponse.LjubljanaTransportResponse;
import com.kumuluzee.LjubljanaTransportResponse.TaxiProvider;

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

    @GET
    public Response getLjubljanaTransport() throws Exception {
        LjubljanaTransportResponse transportResponse = transportBean.getTransportInfo();
        return transportResponse != null
                ? Response.ok(transportResponse).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("taxis")
    public Response getTaxis() throws Exception {
        List<TaxiProvider> taxiProviders = transportBean.getTaxis();
        return taxiProviders != null
                ? Response.ok(taxiProviders).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("bicycle")
    public Response getBicycles() throws Exception {
        BicyclePath bicyclePath = transportBean.getBicyclePath();
        return bicyclePath != null
                ? Response.ok(bicyclePath).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("carsharing")
    public Response getCarsharing() throws Exception {
        CarsharingPath carsharingPath = transportBean.getCarsharingPath();
        return carsharingPath != null
                ? Response.ok(carsharingPath).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

}
