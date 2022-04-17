package com.kumuluzee.OpenDataResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BicikeLJResponse {
    private Markers markers;

    public Markers getMarkers() {
        return markers;
    }

    public void setMarkers(Markers markers) {
        this.markers = markers;
    }
}
