package com.kumuluzee.LjubljanaTransportResponse;

import com.kumuluzee.GoogleMapsResponse.DirectionResponse.Leg;
import com.kumuluzee.OpenChargeResponse.AddressInfo;

public class CarsharingPath {
    private AddressInfo startingStation;
    private AddressInfo finishStation;
    private Leg originToStation;
    private Leg stationToStation;
    private Leg stationToDestination;
    private double price;

    public AddressInfo getStartingStation() {
        return startingStation;
    }

    public void setStartingStation(AddressInfo startingStation) {
        this.startingStation = startingStation;
    }

    public AddressInfo getFinishStation() {
        return finishStation;
    }

    public void setFinishStation(AddressInfo finishStation) {
        this.finishStation = finishStation;
    }

    public Leg getOriginToStation() {
        return originToStation;
    }

    public void setOriginToStation(Leg originToStation) {
        this.originToStation = originToStation;
    }

    public Leg getStationToStation() {
        return stationToStation;
    }

    public void setStationToStation(Leg stationToStation) {
        this.stationToStation = stationToStation;
    }

    public Leg getStationToDestination() {
        return stationToDestination;
    }

    public void setStationToDestination(Leg stationToDestination) {
        this.stationToDestination = stationToDestination;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
