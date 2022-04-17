package com.kumuluzee;

import com.kumuluzee.GoogleMapsResponse.DirectionResponse.DirectionResponse;
import com.kumuluzee.OpenDataResponse.BicikeLJResponse;
import com.kumuluzee.OpenDataResponse.StationLocation;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@RequestScoped
public class OpenDataClient {

    @Inject
    private XContext xContext;

    private Client client = ClientBuilder.newClient();

    public BicikeLJResponse getAllStations() {
        String REST_URI = "https://opendata.si/promet/bicikelj/list/";
        return client
                .target(REST_URI)
                .request(MediaType.APPLICATION_JSON)
                .get(BicikeLJResponse.class);
    }

    public StationLocation getClosestStation(double lat, double lng) {
        double minDistance = Integer.MAX_VALUE;
        StationLocation minStation = null;
        double distance = 0;
        Map<String, StationLocation> stations = getAllStations().getMarkers().getStationLocations();
        for(String station : stations.keySet()) {
            distance = distance(lat, stations.get(station).getLat(), lng, stations.get(station).getLng(), 0, 0);
            if(distance < minDistance) {
                minDistance = distance;
                minStation = stations.get(station);
            }
        }
        return minStation;
    }

    public double distance(double lat1, double lat2, double lon1, double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

}
