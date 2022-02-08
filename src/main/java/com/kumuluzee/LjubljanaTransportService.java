package com.kumuluzee;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluzee.GoogleMapsResponse.DirectionResponse;
import com.kumuluzee.LjubljanaTransportResponse.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.math.RoundingMode;

@RequestScoped
public class LjubljanaTransportService {

    @Inject
    private GoogleMapsClient googleMapsBean;

    public LjubljanaTransportResponse getTransportInfo(){
        LjubljanaTransportResponse ljubljanaTransportResponse = new LjubljanaTransportResponse();
        ljubljanaTransportResponse.setWalking(getWalkingInfo());
        ljubljanaTransportResponse.setBicycling(getBicyclingInfo());
        ljubljanaTransportResponse.setTaxi(getTaxiInfo());
        ljubljanaTransportResponse.setBus(getTransitBusInfo());
        ljubljanaTransportResponse.setTrain(getTransitTrainInfo());
        return ljubljanaTransportResponse;
    }

    public Walking getWalkingInfo(){
        Walking walking = new Walking();
        try {
            DirectionResponse walkingDirections = googleMapsBean.getJsonDirection("walking");
            walking.setDuration(walkingDirections.getRoute().getLeg().getDuration());
            walking.setDistance(walkingDirections.getRoute().getLeg().getDistance());
            // Average human burns 0.065 kcal per meter while walking
            walking.setKcal((int)(walking.getDistance().getValue()*0.065));
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return walking;
    }

    public Bicycling getBicyclingInfo(){
        Bicycling bicycling = new Bicycling();
        try {
            DirectionResponse bicyclingDirections = googleMapsBean.getJsonDirection("bicycling");
            bicycling.setDuration(bicyclingDirections.getRoute().getLeg().getDuration());
            bicycling.setDistance(bicyclingDirections.getRoute().getLeg().getDistance());
            // Average human burns 0.029 kcal per meter while cicyling at moderate speed
            bicycling.setKcal((int)(bicycling.getDistance().getValue()*0.029));
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return bicycling;
    }

    public Taxi getTaxiInfo(){
        Taxi taxi = new Taxi();
        try {
            DirectionResponse taxiDirections = googleMapsBean.getJsonDirection("driving");
            taxi.setDuration(taxiDirections.getRoute().getLeg().getDuration());
            taxi.setDistance(taxiDirections.getRoute().getLeg().getDistance());
            // Starting price is 1 eur and the price for km is 1eur.
            taxi.setPrice(round(1+(taxi.getDistance().getValue()/1000.0), 2));
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return taxi;
    }

    public Transit getTransitBusInfo(){
        Transit transitBus = new Transit();
        try {
            DirectionResponse transitBusDirections = googleMapsBean.getJsonDirection("transit&transit_mode=bus");
            transitBus.setDuration(transitBusDirections.getRoute().getLeg().getDuration());
            transitBus.setDistance(transitBusDirections.getRoute().getLeg().getDistance());
            // LPP buses cost 1.3eur for 1 ride
            transitBus.setPrice(1.3);
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return transitBus;
    }

    public Transit getTransitTrainInfo(){
        Transit transitTrain = new Transit();
        try {
            DirectionResponse transitTrainDirections = googleMapsBean.getJsonDirection("transit&transit_mode=train");
            transitTrain.setDuration(transitTrainDirections.getRoute().getLeg().getDuration());
            transitTrain.setDistance(transitTrainDirections.getRoute().getLeg().getDistance());
            // Slovenske železnice train rides in Ljubljana cost 1.3eur
            transitTrain.setPrice(1.3);
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return transitTrain;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
