package com.sabanciuniv.activityforecast;

public class Location {


    private String id;
    private Weather weather;
    private String name;


    public Location(String id, String name, Weather weather) {
        this.id = id;
        this.name = name;
        this.weather = weather;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Weather getWeather() {
        return weather;
    }
    public void setWeather(Weather weather) {
        this.weather = weather;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


}

