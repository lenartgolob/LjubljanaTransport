package com.kumuluzee.LjubljanaTransportResponse;

import com.kumuluzee.GoogleMapsResponse.Distance;
import com.kumuluzee.GoogleMapsResponse.Duration;

public class Transit {
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
