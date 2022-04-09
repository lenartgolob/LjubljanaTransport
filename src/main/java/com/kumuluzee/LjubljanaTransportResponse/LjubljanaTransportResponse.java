package com.kumuluzee.LjubljanaTransportResponse;

public class LjubljanaTransportResponse {
    private Walking walking;
    private Bicycling bicycling;
    private Taxi taxi;
    private Transit bus;
    private Transit train;
    private Carsharing carsharing;

    public Walking getWalking() {
        return walking;
    }

    public void setWalking(Walking walking) {
        this.walking = walking;
    }

    public Bicycling getBicycling() {
        return bicycling;
    }

    public void setBicycling(Bicycling bicycling) {
        this.bicycling = bicycling;
    }

    public Taxi getTaxi() {
        return taxi;
    }

    public void setTaxi(Taxi taxi) {
        this.taxi = taxi;
    }

    public Transit getBus() {
        return bus;
    }

    public void setBus(Transit bus) {
        this.bus = bus;
    }

    public Transit getTrain() {
        return train;
    }

    public void setTrain(Transit train) {
        this.train = train;
    }

    public Carsharing getCarsharing() {
        return carsharing;
    }

    public void setCarsharing(Carsharing carsharing) {
        this.carsharing = carsharing;
    }
}
