package com.kumuluzee;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluzee.GoogleMapsResponse.DirectionResponse.DirectionResponse;
import com.kumuluzee.GoogleMapsResponse.GeocodeResponse.GeocodeResponse;

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

    private String apiKey = ConfigurationUtil.getInstance().get("kumuluzee.google-api-key").orElse(null);

    public DirectionResponse getJsonDirection(String mode) {
        String REST_URI = "https://maps.googleapis.com/maps/api/directions/json?&destination=place_id:" + xContext.getContext().getDestination() + "&mode=" + mode + "&origin=place_id:" + xContext.getContext().getOrigin() + "&key=" + apiKey;
        return client
                .target(REST_URI)
                .request(MediaType.APPLICATION_JSON)
                .get(DirectionResponse.class);
    }

    public DirectionResponse getJsonDirectionFromOrigin(String mode, double lat, double lon) {
        String REST_URI = "https://maps.googleapis.com/maps/api/directions/json?&destination=" + lat + "," + lon + "&mode=" + mode + "&origin=place_id:" + xContext.getContext().getOrigin() + "&key=" + apiKey;
        return client
                .target(REST_URI)
                .request(MediaType.APPLICATION_JSON)
                .get(DirectionResponse.class);
    }

    public DirectionResponse getJsonDirectionToDestination(String mode, double lat, double lon) {
        String REST_URI = "https://maps.googleapis.com/maps/api/directions/json?&destination=place_id:" + xContext.getContext().getDestination() + "&mode=" + mode + "&origin=" + lat + "," + lon + "&key=" + apiKey;
        return client
                .target(REST_URI)
                .request(MediaType.APPLICATION_JSON)
                .get(DirectionResponse.class);
    }

    public DirectionResponse getJsonDirectionP2P(String mode, double lat1, double lon1, double lat2, double lon2) {
        String REST_URI = "https://maps.googleapis.com/maps/api/directions/json?&destination=" + lat2 + "," + lon2 + "&mode=" + mode + "&origin=" + lat1 + "," + lon1 + "&key=" + apiKey;
        return client
                .target(REST_URI)
                .request(MediaType.APPLICATION_JSON)
                .get(DirectionResponse.class);
    }

    public GeocodeResponse getCoordinates(String placeID) {
        String REST_URI = "https://maps.googleapis.com/maps/api/geocode/json?place_id=" + placeID + "&key=" + apiKey;
        return client
                .target(REST_URI)
                .request(MediaType.APPLICATION_JSON)
                .get(GeocodeResponse.class);
    }
}
