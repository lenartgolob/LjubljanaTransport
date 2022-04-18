package com.kumuluzee.LjubljanaTransportResponse;

import com.kumuluzee.GoogleMapsResponse.DirectionResponse.Leg;
import com.kumuluzee.OpenDataResponse.StationLocation;

public class BicyclePath {
    private StationLocation startingStation;
    private StationLocation finishStation;
    private Leg originToStation;
    private Leg stationToStation;
    private Leg stationToDestination;

    public StationLocation getStartingStation() {
        return startingStation;
    }

    public void setStartingStation(StationLocation startingStation) {
        this.startingStation = startingStation;
    }

    public StationLocation getFinishStation() {
        return finishStation;
    }

    public void setFinishStation(StationLocation finishStation) {
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
}
