package com.kumuluzee;

import com.kumuluzee.GoogleMapsResponse.DirectionResponse.DirectionResponse;
import com.kumuluzee.GoogleMapsResponse.DirectionResponse.Leg;
import com.kumuluzee.LjubljanaTransportResponse.*;
import com.kumuluzee.OpenChargeResponse.OpenChargeResponse;
import com.kumuluzee.OpenDataResponse.StationLocation;

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
    private OpenDataClient openDataBean;

    @Inject
    private TextTransform textTransformBean;

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
            StationLocation startingStation = openDataBean.getClosestStation(googleMapsBean.getCoordinates(xContext.getContext().getOrigin()).getResult().getGeometry().getLocation().getLat(), googleMapsBean.getCoordinates(xContext.getContext().getOrigin()).getResult().getGeometry().getLocation().getLng());
            StationLocation destinationStation = openDataBean.getClosestStation(googleMapsBean.getCoordinates(xContext.getContext().getDestination()).getResult().getGeometry().getLocation().getLat(), googleMapsBean.getCoordinates(xContext.getContext().getDestination()).getResult().getGeometry().getLocation().getLng());
            int bikeDistance = googleMapsBean.getJsonDirectionP2P("walking", startingStation.getLat(), startingStation.getLng(), destinationStation.getLat(), destinationStation.getLng()).getRoute().getLeg().getDistance().getValue();
            double bikeDuration = bikeDistance / 3.33; // 3.33 m/s is 12 km/h (average biking speed)

            DirectionResponse toStart = googleMapsBean.getJsonDirectionFromOrigin("walking", startingStation.getLat(), startingStation.getLng());
            DirectionResponse toEnd = googleMapsBean.getJsonDirectionToDestination("walking", destinationStation.getLat(), destinationStation.getLng());

            int distanceValue = (int) Math.round(bikeDistance + toStart.getRoute().getLeg().getDistance().getValue() + toEnd.getRoute().getLeg().getDistance().getValue());
            int durationValue = (int) Math.round(bikeDuration + toStart.getRoute().getLeg().getDuration().getValue() + toEnd.getRoute().getLeg().getDuration().getValue());

            bicycling.setDistance(textTransformBean.textTransformDistance(distanceValue));
            bicycling.setDuration(textTransformBean.textTransformeDuration(durationValue));
            // Average human burns 0.029 kcal per meter while cicyling at moderate speed and 0.062 per meter walking
            bicycling.setKcal((int) Math.round(bikeDistance*0.029 + (distanceValue-bikeDistance)*0.062));
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
        double lat = googleMapsBean.getCoordinates(xContext.getContext().getOrigin()).getResult().getGeometry().getLocation().getLat();
        double lon = googleMapsBean.getCoordinates(xContext.getContext().getOrigin()).getResult().getGeometry().getLocation().getLng();
        OpenChargeResponse startingStation = openChargeBean.getClosestStation(lat, lon);
        lat = googleMapsBean.getCoordinates(xContext.getContext().getDestination()).getResult().getGeometry().getLocation().getLat();
        lon = googleMapsBean.getCoordinates(xContext.getContext().getDestination()).getResult().getGeometry().getLocation().getLng();
        OpenChargeResponse finishStation = openChargeBean.getClosestStation(lat, lon);
        if(startingStation == null || finishStation == null) {
            return null;
        } else {
            Carsharing carsharing = new Carsharing();
            DirectionResponse walkingToPOI = googleMapsBean.getJsonDirectionFromOrigin("walking", startingStation.getAddressInfo().getLatitude(), startingStation.getAddressInfo().getLongitude());
            DirectionResponse walkingFromPOI = googleMapsBean.getJsonDirectionToDestination("walking", finishStation.getAddressInfo().getLatitude(), finishStation.getAddressInfo().getLongitude());
            DirectionResponse drivingPOI = googleMapsBean.getJsonDirectionP2P("driving", startingStation.getAddressInfo().getLatitude(), startingStation.getAddressInfo().getLongitude(), finishStation.getAddressInfo().getLatitude(), finishStation.getAddressInfo().getLongitude());
            // Calculate accumulative distance
            int distanceValue = walkingToPOI.getRoute().getLeg().getDistance().getValue() + drivingPOI.getRoute().getLeg().getDistance().getValue() + walkingFromPOI.getRoute().getLeg().getDistance().getValue();
            carsharing.setDistance(textTransformBean.textTransformDistance(distanceValue));

            // Calculate accumulative duration
            int durationValue = walkingToPOI.getRoute().getLeg().getDuration().getValue() + drivingPOI.getRoute().getLeg().getDuration().getValue() + walkingFromPOI.getRoute().getLeg().getDuration().getValue();
            carsharing.setDuration(textTransformBean.textTransformeDuration(durationValue));

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

    public BicyclePath getBicyclePath() {
        BicyclePath bicyclePath = new BicyclePath();
        try {
            StationLocation startingStation = openDataBean.getClosestStation(googleMapsBean.getCoordinates(xContext.getContext().getOrigin()).getResult().getGeometry().getLocation().getLat(), googleMapsBean.getCoordinates(xContext.getContext().getOrigin()).getResult().getGeometry().getLocation().getLng());
            StationLocation finishStation = openDataBean.getClosestStation(googleMapsBean.getCoordinates(xContext.getContext().getDestination()).getResult().getGeometry().getLocation().getLat(), googleMapsBean.getCoordinates(xContext.getContext().getDestination()).getResult().getGeometry().getLocation().getLng());
            int bikeDistance = googleMapsBean.getJsonDirectionP2P("walking", startingStation.getLat(), startingStation.getLng(), finishStation.getLat(), finishStation.getLng()).getRoute().getLeg().getDistance().getValue();
            int bikeDuration = (int) Math.round(bikeDistance / 3.33); // 3.33 m/s is 12 km/h (average biking speed)

            DirectionResponse toStart = googleMapsBean.getJsonDirectionFromOrigin("walking", startingStation.getLat(), startingStation.getLng());
            DirectionResponse toEnd = googleMapsBean.getJsonDirectionToDestination("walking", finishStation.getLat(), finishStation.getLng());

            bicyclePath.setStartingStation(startingStation);
            bicyclePath.setFinishStation(finishStation);
            bicyclePath.setOriginToStation(toStart.getRoute().getLeg());
            Leg stationToStation = new Leg();
            stationToStation.setDistance(textTransformBean.textTransformDistance(bikeDistance));
            stationToStation.setDuration(textTransformBean.textTransformeDuration(bikeDuration));
            bicyclePath.setStationToStation(stationToStation);
            bicyclePath.setStationToDestination(toEnd.getRoute().getLeg());
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return bicyclePath;
    }

    public CarsharingPath getCarsharingPath() {
        CarsharingPath carsharingPath = new CarsharingPath();
        try {
            OpenChargeResponse startingStation = openChargeBean.getClosestStation(googleMapsBean.getCoordinates(xContext.getContext().getOrigin()).getResult().getGeometry().getLocation().getLat(), googleMapsBean.getCoordinates(xContext.getContext().getOrigin()).getResult().getGeometry().getLocation().getLng());
            OpenChargeResponse finishStation = openChargeBean.getClosestStation(googleMapsBean.getCoordinates(xContext.getContext().getDestination()).getResult().getGeometry().getLocation().getLat(), googleMapsBean.getCoordinates(xContext.getContext().getDestination()).getResult().getGeometry().getLocation().getLng());
/*
            int bikeDistance = googleMapsBean.getJsonDirectionP2P("walking", startingStation.getAddressInfo().getLatitude(), startingStation.getAddressInfo().getLongitude(), finishStation.getAddressInfo().getLatitude(), finishStation.getAddressInfo().getLongitude()).getRoute().getLeg().getDistance().getValue();
*/
/*
            int bikeDuration = (int) Math.round(bikeDistance / 3.33); // 3.33 m/s is 12 km/h (average biking speed)
*/

            DirectionResponse toStart = googleMapsBean.getJsonDirectionFromOrigin("walking", startingStation.getAddressInfo().getLatitude(), startingStation.getAddressInfo().getLongitude());
            DirectionResponse stationToStation = googleMapsBean.getJsonDirectionP2P("driving", startingStation.getAddressInfo().getLatitude(), startingStation.getAddressInfo().getLongitude(), finishStation.getAddressInfo().getLatitude(), finishStation.getAddressInfo().getLongitude());
            DirectionResponse toEnd = googleMapsBean.getJsonDirectionToDestination("walking", finishStation.getAddressInfo().getLatitude(), finishStation.getAddressInfo().getLongitude());

            carsharingPath.setStartingStation(startingStation.getAddressInfo());
            carsharingPath.setFinishStation(finishStation.getAddressInfo());
            carsharingPath.setOriginToStation(toStart.getRoute().getLeg());
            carsharingPath.setStationToStation(stationToStation.getRoute().getLeg());
            carsharingPath.setStationToDestination(toEnd.getRoute().getLeg());

            int pricePerKM = (int) Math.round((stationToStation.getRoute().getLeg().getDistance().getValue()/1000)*0.23);
            int pricePerMin = (int) Math.round((stationToStation.getRoute().getLeg().getDuration().getValue()/60)*0.07);
            int price = Math.max(pricePerKM, pricePerMin);
            if(price < 4) {
                carsharingPath.setPrice(4);
            } else if(price > 26) {
                carsharingPath.setPrice(26);
            } else {
                carsharingPath.setPrice(price);
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return carsharingPath;
    }

}
