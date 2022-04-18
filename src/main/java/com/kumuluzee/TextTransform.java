package com.kumuluzee;

import com.kumuluzee.GoogleMapsResponse.DirectionResponse.Distance;
import com.kumuluzee.GoogleMapsResponse.DirectionResponse.Duration;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class TextTransform {

    public Distance textTransformDistance(int distanceValue) {
        Distance distance = new Distance();
        String distanceText;
        if(distanceValue > 1000) {
            distanceText = String.format("%.1f km", distanceValue/1000.0);
        } else {
            distanceText = distanceValue + " m";
        }
        distance.setValue(distanceValue);
        distance.setText(distanceText);
        return distance;
    }

    public Duration textTransformeDuration(int durationValue) {
        Duration duration = new Duration();
        String durationText;
        if(durationValue >= 3600) {
            int hours = (int) Math.floor(durationValue/3600);
            int mins = Math.round((durationValue - hours*3600)/60);
            if(mins == 0) {
                if(hours == 1) {
                    durationText = hours + " hour";
                } else {
                    durationText = hours + " hours";
                }
            } else {
                if(hours == 1) {
                    if(mins == 1) {
                        durationText = hours + " hour " + mins + " min";
                    } else {
                        durationText = hours + " hour " + mins + " mins";
                    }
                } else {
                    if(mins == 1) {
                        durationText = hours + " hours" + mins + " min";
                    } else {
                        durationText = hours + " hours" + mins + " mins";
                    }
                }
            }
        } else {
            durationText = String.format("%d mins", Math.round(durationValue/60));
        }
        duration.setValue(durationValue);
        duration.setText(durationText);
        return duration;
    }
}
