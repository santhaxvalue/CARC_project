package com.sd.carc;

public class GpsCoordinates {

    private double latValue, longValue;

    public GpsCoordinates(double latitude, double longitude) {
        latValue = latitude;
        longValue = longitude;

    }

    public String getCoordinateString() {
        return "&lat=" + latValue + "&long=" + longValue;
    }

    public static String getOfflineCoordinates() {
        return "&lat=0.0&long=0.0";
    }

    public String getLatValue() {
        return "" + latValue;
    }

    public String getLongValue() {
        return "" + longValue;
    }
}

