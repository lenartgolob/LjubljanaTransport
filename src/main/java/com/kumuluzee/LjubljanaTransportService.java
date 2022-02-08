package com.kumuluzee;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluzee.GoogleMapsResponse.DirectionResponse;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

@RequestScoped
public class LjubljanaTransportService {

    private Client client = ClientBuilder.newClient();

    private String apiKey = ConfigurationUtil.getInstance().get("kumuluzee.api-key").orElse(null);

    public String getTransportInfo(){
        DirectionResponse direction = getJsonDirection("driving");
        System.out.println(direction.getRoute().getLeg().getDuration().getText());
        return "neki";
    }

    public DirectionResponse getJsonDirection(String mode) {
        String REST_URI = "https://maps.googleapis.com/maps/api/directions/json?&destination=place_id:ChIJA0bfEDthZUcRYPI_ghz4AAQ&mode=" + mode + "&origin=place_id:ChIJf0EQqAxeZUcRuUCOOHsclf8&key=" + apiKey;
        return client
                .target(REST_URI)
                .request(MediaType.APPLICATION_JSON)
                .get(DirectionResponse.class);
    }
}
