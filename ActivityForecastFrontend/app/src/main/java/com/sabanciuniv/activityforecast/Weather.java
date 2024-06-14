package com.sabanciuniv.activityforecast;

public class Weather {

    private String id;
    private double temperature;
    private String description;
    private String icon;

    public Weather(String id, double temperature, String description, String icon) {
        this.id = id;
        this.temperature = temperature;
        this.description = description;
        this.icon = icon;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public double getTemperature() {
        return temperature;
    }
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }




}