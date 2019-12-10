package com.mtsealove.github.boxlinker_driver.Restful;

public class ReqLatLng {
    String DriverID;
    double Latitude, Longitude;

    public ReqLatLng(String driverID, double latitude, double longitude) {
        DriverID = driverID;
        Latitude = latitude;
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getDriverID() {
        return DriverID;
    }

    public void setDriverID(String driverID) {
        DriverID = driverID;
    }
}
