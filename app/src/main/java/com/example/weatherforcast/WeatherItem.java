package com.example.weatherforcast;

public class WeatherItem {
    String date, condition, iconUrl;
    double maxTemp, minTemp;

    public WeatherItem(String date, String condition, String iconUrl, double maxTemp, double minTemp) {
        this.date = date;
        this.condition = condition;
        this.iconUrl = iconUrl;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
    }
}
