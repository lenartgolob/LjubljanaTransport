package com.kumuluzee;

import com.kumuluzee.GoogleMapsResponse.DirectionResponse.DirectionResponse;
import com.kumuluzee.GoogleMapsResponse.DirectionResponse.Distance;
import com.kumuluzee.GoogleMapsResponse.DirectionResponse.Duration;
import com.kumuluzee.LjubljanaTransportResponse.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RequestScoped
public class LjubljanaTransportService {

    @Inject
    private GoogleMapsClient googleMapsBean;

    @Inject
    private Taxis taxisBean;

    @Inject
    private OpenChargeClient openChargeBean;

    @Inject
    private XContext xContext;

    public LjubljanaTransportResponse getTransportInfo(){
        LjubljanaTransportResponse ljubljanaTransportResponse = new LjubljanaTransportResponse();
        ljubljanaTransportResponse.setWalking(getWalkingInfo());
        ljubljanaTransportResponse.setBicycling(getBicyclingInfo());
        ljubljanaTransportResponse.setTaxi(getTaxiInfo());
        ljubljanaTransportResponse.setBus(getTransitBusInfo());
        ljubljanaTransportResponse.setTrain(getTransitTrainInfo());
        ljubljanaTransportResponse.setCarsharing(getCarsharingInfo());
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
            // Slovenske železnice train rides in Ljubljana cost 1.3eur if < 10km else 1.9eur < 20km
            if(transitTrainDirections.getRoute().getLeg().getDistance().getValue()<10000)
                transitTrain.setPrice(1.3);
            else
                transitTrain.setPrice(1.9);
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

    public List<TaxiProvider> getTaxis() {
        List<TaxiProvider> taxisProviders = taxisBean.getTaxisProviders();
        return taxisProviders;
    }

    public Carsharing getCarsharingInfo() {
        String lat = Double.toString(googleMapsBean.getCoordinates(xContext.getContext().getOrigin()).getResult().getGeometry().getLocation().getLat());
        String lon = Double.toString(googleMapsBean.getCoordinates(xContext.getContext().getOrigin()).getResult().getGeometry().getLocation().getLng());
        double[] coordinatesStartPOI = openChargeBean.getNearestPOI(lat, lon);
        lat = Double.toString(googleMapsBean.getCoordinates(xContext.getContext().getDestination()).getResult().getGeometry().getLocation().getLat());
        lon = Double.toString(googleMapsBean.getCoordinates(xContext.getContext().getDestination()).getResult().getGeometry().getLocation().getLng());
        double[] coordinatesEndPOI = openChargeBean.getNearestPOI(lat, lon);

        Carsharing carsharing = new Carsharing();
        DirectionResponse walkingToPOI = googleMapsBean.getJsonDirectionFromOrigin("walking", coordinatesStartPOI[0], coordinatesStartPOI[1]);
        DirectionResponse walkingFromPOI = googleMapsBean.getJsonDirectionToDestination("walking", coordinatesEndPOI[0], coordinatesEndPOI[1]);
        DirectionResponse drivingPOI = googleMapsBean.getJsonDirectionP2P("driving", coordinatesStartPOI[0], coordinatesStartPOI[1], coordinatesEndPOI[0], coordinatesEndPOI[1]);
        // Calculate accumulative distance
        int distanceValue = walkingToPOI.getRoute().getLeg().getDistance().getValue() + drivingPOI.getRoute().getLeg().getDistance().getValue() + walkingFromPOI.getRoute().getLeg().getDistance().getValue();
        String distanceText;
        if(distanceValue > 1000) {
            distanceText = String.format("%.1f km", distanceValue/1000.0);
        } else {
            distanceText = distanceValue + " m";
        }
        Distance distance = new Distance();
        distance.setValue(distanceValue);
        distance.setText(distanceText);
        carsharing.setDistance(distance);

        // Calculate accumulative duration
        int durationValue = walkingToPOI.getRoute().getLeg().getDuration().getValue() + drivingPOI.getRoute().getLeg().getDuration().getValue() + walkingFromPOI.getRoute().getLeg().getDuration().getValue();
        String durationText;
        if(durationValue >= 3600) {
            int hours = (int) Math.floor(durationValue/3600);
            int mins = Math.round((durationValue - hours*3600)/60);
            if(mins == 0) {
                if(hours == 1) {
                    durationText = hours + " hour";
                } else {
                    durationText = hours + " hours";
                }
            } else {
                if(hours == 1) {
                    if(mins == 1) {
                        durationText = hours + " hour " + mins + " min";
                    } else {
                        durationText = hours + " hour " + mins + " mins";
                    }
                } else {
                    if(mins == 1) {
                        durationText = hours + " hours" + mins + " min";
                    } else {
                        durationText = hours + " hours" + mins + " mins";
                    }
                }
            }
        } else {
            durationText = String.format("%d mins", Math.round(durationValue/60));
        }
        Duration duration = new Duration();
        duration.setValue(durationValue);
        duration.setText(durationText);
        carsharing.setDuration(duration);

        int distanceCar = drivingPOI.getRoute().getLeg().getDistance().getValue();
        int durationCar = drivingPOI.getRoute().getLeg().getDuration().getValue()/60;
        int pricePerKM = (int) Math.round((distanceCar/1000)*0.23);
        int pricePerMin = (int) Math.round(durationCar*0.07);
        int price = Math.max(pricePerKM, pricePerMin);
        if(price < 4) {
            carsharing.setPrice(4);
        } else if(price > 26) {
            carsharing.setPrice(26);
        } else {
            carsharing.setPrice(price);
        }
        return carsharing;
    }

}
