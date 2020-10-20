package com.example.taraffac;

public class SpeedBump {
    double latitude;
    double longitude;
    String type;
    String size;
    int deleteOption;

    public SpeedBump() {

    }

    public SpeedBump( double latitude, double longitude, String type, String size) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.size = size;
        this.deleteOption = 0;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setDeleteOption(int deleteOption) {
        this.deleteOption = deleteOption;
    }

    public double getLatitude() {
        return latitude;
    }


    public double getLongitude() {
        return longitude;
    }

    public String getType() {
        return type;
    }

    public String getSize() {
        return size;
    }

    public int getDeleteOption() {
        return deleteOption;
    }
}
