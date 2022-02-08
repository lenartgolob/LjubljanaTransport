package com.kumuluzee;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluzee.GoogleMapsResponse.DirectionResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

@RequestScoped
public class GoogleMapsClient {

    @Inject
    private XContext xContext;

    private Client client = ClientBuilder.newClient();

    private String apiKey = ConfigurationUtil.getInstance().get("kumuluzee.api-key").orElse(null);

    public DirectionResponse getJsonDirection(String mode) {
        String REST_URI = "https://maps.googleapis.com/maps/api/directions/json?&destination=place_id:" + xContext.getContext().getDestination() + "&mode=" + mode + "&origin=place_id:" + xContext.getContext().getOrigin() + "&key=" + apiKey;
        return client
                .target(REST_URI)
                .request(MediaType.APPLICATION_JSON)
                .get(DirectionResponse.class);
    }
}
