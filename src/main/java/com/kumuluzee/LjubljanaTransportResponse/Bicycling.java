package com.kumuluzee.LjubljanaTransportResponse;

import com.kumuluzee.GoogleMapsResponse.Distance;
import com.kumuluzee.GoogleMapsResponse.Duration;

public class Bicycling {
    private Duration duration;
    private Distance distance;
    private int kcal;

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

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }
}
