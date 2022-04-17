package com.kumuluzee;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.inject.Inject;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Context {
    private String origin;
    private String destination;
/*    private double originLat;
    private double originLng;
    private double destinationLat;
    private double destinationLng;*/

    @Inject
    private GoogleMapsClient googleMapsBean;


    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
/*        this.originLat = googleMapsBean.getCoordinates(origin).getResult().getGeometry().getLocation().getLat();
        this.originLng = googleMapsBean.getCoordinates(origin).getResult().getGeometry().getLocation().getLng();*/
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
/*        this.destinationLat = googleMapsBean.getCoordinates(destination).getResult().getGeometry().getLocation().getLat();
        this.destinationLng = googleMapsBean.getCoordinates(destination).getResult().getGeometry().getLocation().getLng();*/
    }

/*    public double getOriginLat() {
        return originLat;
    }

    public double getOriginLng() {
        return originLng;
    }

    public double getDestinationLat() {
        return destinationLat;
    }

    public double getDestinationLng() {
        return destinationLng;
    }*/
}
