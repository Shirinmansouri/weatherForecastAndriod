package com.ms.forecastweather.Models;

import java.util.UUID;

public class WeatherModel {
    private String searchDate;
    private String cityName;
    private String time;
    private String temperature;
    private String icon;
    private String windSpeed;
    private String   Id;

    public WeatherModel() {
    }

    public WeatherModel(String searchDate, String cityName, String time, String temperature, String icon, String windSpeed)
    {
        this.searchDate = searchDate;
        this.cityName = cityName;
        this.time = time;
        this.temperature = temperature;
        this.icon = icon;
        this.windSpeed = windSpeed;
        Id = UUID.randomUUID().toString(); ;
    }

    public String getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(String searchDate) {
        this.searchDate = searchDate;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
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

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getId() {
        return Id;
    }


}
