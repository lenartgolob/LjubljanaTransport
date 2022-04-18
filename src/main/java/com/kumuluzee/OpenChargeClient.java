package com.kumuluzee;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluzee.OpenChargeResponse.AddressInfo;
import com.kumuluzee.OpenChargeResponse.OpenChargeResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestScoped
public class OpenChargeClient {

    @Inject
    private XContext xContext;

    @Inject
    private OpenDataClient distanceBean;

    private Client client = ClientBuilder.newClient();

    private String apiKey = ConfigurationUtil.getInstance().get("kumuluzee.open-maps-api-key").orElse(null);

    public double[] getNearestPOI(String lat, String lon) {
        double[] coordinates = new double[2];
        try {
            URL url = new URL("https://api.openchargemap.io/v3/poi/?output=json&latitude=" + lat + "&longitude=" + lon + "&maxresults=1&key=" + apiKey);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int status = con.getResponseCode();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            int i = content.indexOf("Latitude");
            double latitude = Double.parseDouble(content.subSequence(i+11, i+20).toString());
            i = content.indexOf("Longitude");
            double longitude = Double.parseDouble(content.subSequence(i+12, i+21).toString());
            coordinates[0] = latitude;
            coordinates[1] = longitude;
            con.disconnect();
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return coordinates;
    }
    public List<OpenChargeResponse> getAllStations(double lat, double lon) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<OpenChargeResponse> stations = null;
        try {
            URL url = new URL("https://api.openchargemap.io/v3/poi/?output=json&latitude=" + lat + "&longitude=" + lon + "&distance=0.62&camelcase=true&key=" + apiKey);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            String response = content.toString();
            response = response.replaceAll("Ĺľ", "ž");
            response = response.replaceAll("Ĺˇ", "š");
            response = response.replaceAll("ÄŤ", "č");
            CollectionType listType =
                    objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, OpenChargeResponse.class);
            stations = objectMapper.readValue(response, listType);
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return stations;
    }

    public OpenChargeResponse getClosestStation(double lat, double lng) {
        double minDistance = Integer.MAX_VALUE;
        OpenChargeResponse minStation = null;
        double distance = 0;
        List<OpenChargeResponse> stations = getAllStations(lat, lng);
        for(OpenChargeResponse station : stations) {
            distance = distanceBean.distance(lat, station.getAddressInfo().getLatitude(), lng, station.getAddressInfo().getLongitude(), 0, 0);
            if(distance < minDistance) {
                minDistance = distance;
                minStation = station;
            }
        }
        return minStation;
    }
}
