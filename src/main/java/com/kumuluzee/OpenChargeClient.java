package com.kumuluzee;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RequestScoped
public class OpenChargeClient {

    @Inject
    private XContext xContext;

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
}
