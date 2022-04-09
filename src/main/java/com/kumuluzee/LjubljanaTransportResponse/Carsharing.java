package com.kumuluzee.LjubljanaTransportResponse;

import com.kumuluzee.GoogleMapsResponse.DirectionResponse.Distance;
import com.kumuluzee.GoogleMapsResponse.DirectionResponse.Duration;

public class Carsharing {
    private Duration duration;
    private Distance distance;
    private double price;

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
