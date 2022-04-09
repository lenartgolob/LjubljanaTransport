package com.kumuluzee.GoogleMapsResponse.DirectionResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kumuluzee.GoogleMapsResponse.DirectionResponse.Distance;
import com.kumuluzee.GoogleMapsResponse.DirectionResponse.Duration;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Leg {
    private Duration duration;
    private Distance distance;

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
}
