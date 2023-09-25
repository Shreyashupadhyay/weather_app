package com.example.waetherapp;

public class Weathermodel {
    private String time;
    private String temperature;
    private String icon;
    private String windspeed;

    public Weathermodel(String time, String temperature, String icon, String windspeed) {
        this.time = time;
        this.temperature = temperature;
        this.icon = icon;
        this.windspeed = windspeed;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(String windspeed) {
        this.windspeed = windspeed;
    }
}
