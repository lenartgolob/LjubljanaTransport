package com.kumuluzee.OpenDataResponse;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.LinkedHashMap;
import java.util.Map;

public class Markers {
    Map<String, StationLocation> stationLocations = new LinkedHashMap<>();

    @JsonAnySetter
    void setStationLocation(String key, StationLocation value) {
        stationLocations.put(key, value);
    }

    public Map<String, StationLocation> getStationLocations() {
        return stationLocations;
    }
}
