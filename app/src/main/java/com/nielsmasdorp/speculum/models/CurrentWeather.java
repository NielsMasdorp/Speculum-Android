package com.nielsmasdorp.speculum.models;

import com.nielsmasdorp.speculum.models.forecast.Datum_;
import com.nielsmasdorp.speculum.models.forecast.Datum__;

import java.util.List;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class CurrentWeather {

    private String title;
    private String icon;
    private int temperature;
    private int humidity;
    private String pressure;
    private String visibility;
    private String windSpeed;
    private int windTemperature;
    private String windDirection;
    private List<Datum__> forecast;

    public static class Builder {

        private String title;
        private String icon;
        private int temperature;
        private int humidity;
        private String pressure;
        private String visibility;
        private String windSpeed;
        private int windTemperature;
        private String windDirection;
        private List<Datum__> forecast;

        public Builder title(String title) { this.title = title; return this; }
        public Builder icon(String icon) { this.icon = icon; return this; }
        public Builder temperature(int temperature) { this.temperature = temperature; return this; }
        public Builder humidity(int humidity) { this.humidity = humidity; return this; }
        public Builder pressure(String pressure) { this.pressure = pressure; return this; }
        public Builder visibility(String visibility) { this.visibility = visibility; return this; }
        public Builder windSpeed(String windSpeed) { this.windSpeed = windSpeed; return this; }
        public Builder windTemperature(int windTemperature) { this.windTemperature = windTemperature; return this; }
        public Builder windDirection(String windDirection) { this.windDirection = windDirection; return this; }
        public Builder forecast(List<Datum__> forecast) { this.forecast = forecast; return this; }

        public CurrentWeather build() {

            return new CurrentWeather(this);
        }
    }

    private CurrentWeather(Builder builder) {

        this.title = builder.title;
        this.icon = builder.icon;
        this.temperature = builder.temperature;
        this.humidity = builder.humidity;
        this.pressure = builder.pressure;
        this.visibility = builder.visibility;
        this.windSpeed = builder.windSpeed;
        this.windTemperature = builder.windTemperature;
        this.windDirection = builder.windDirection;
        this.forecast = builder.forecast;
    }

    public String getTitle() {
        return title;
    }

    public String getIcon() {
        return icon;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public String getVisibility() {
        return visibility;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public int getWindTemperature() {
        return windTemperature;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public List<Datum__> getForecast() {
        return forecast;
    }
}

