package com.kumuluzee;

import com.kumuluzee.LjubljanaTransportResponse.LjubljanaTransportResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/")
public class LjubljanaTransportResource {

    @Inject
    private LjubljanaTransportService transportBean;

    @GET
    public Response getXContext() throws Exception {
        LjubljanaTransportResponse transportResponse = transportBean.getTransportInfo();
        return transportResponse != null
                ? Response.ok(transportResponse).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }
}
