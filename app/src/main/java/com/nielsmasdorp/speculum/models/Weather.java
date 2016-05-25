package com.nielsmasdorp.speculum.models;

import com.nielsmasdorp.speculum.models.forecast.DayForecast;

import java.util.Date;
import java.util.List;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class Weather {

    private int iconId;
    private String temperature;
    private String summary;
    private String lastUpdated;
    private String windInfo;
    private String humidityInfo;
    private String pressureInfo;
    private String visibilityInfo;
    private List<ForecastDayWeather> forecast;

    public static class Builder {

        private int iconId;
        private String temperature;
        private String summary;
        private String lastUpdated;
        private String windInfo;
        private String humidityInfo;
        private String pressureInfo;
        private String visibilityInfo;
        private List<ForecastDayWeather> forecast;

        public Builder iconId(int iconId) { this.iconId = iconId; return this;}
        public Builder temperature(String temperature) { this.temperature = temperature; return this; }
        public Builder summary(String summary) { this.summary = summary; return this; }
        public Builder lastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; return this; }
        public Builder windInfo(String windInfo) { this.windInfo = windInfo; return this; }
        public Builder humidityInfo(String humidityInfo) { this.humidityInfo = humidityInfo; return this; }
        public Builder pressureInfo(String pressureInfo) { this.pressureInfo = pressureInfo; return this; }
        public Builder visibilityInfo(String visibilityInfo) { this.visibilityInfo = visibilityInfo; return this; }
        public Builder forecast(List<ForecastDayWeather> forecast) { this.forecast = forecast; return this; }

        public Weather build() {

            return new Weather(this);
        }
    }

    private Weather(Builder builder) {

        this.iconId = builder.iconId;
        this.lastUpdated = builder.lastUpdated;
        this.temperature = builder.temperature;
        this.summary = builder.summary;
        this.lastUpdated = builder.lastUpdated;
        this.windInfo = builder.windInfo;
        this.humidityInfo = builder.humidityInfo;
        this.pressureInfo = builder.pressureInfo;
        this.visibilityInfo = builder.visibilityInfo;
        this.forecast = builder.forecast;
        this.forecast = builder.forecast;
    }

    public int getIconId() {
        return iconId;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getSummary() {
        return summary;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public String getWindInfo() {
        return windInfo;
    }

    public String getHumidityInfo() {
        return humidityInfo;
    }

    public String getPressureInfo() {
        return pressureInfo;
    }

    public String getVisibilityInfo() {
        return visibilityInfo;
    }

    public List<ForecastDayWeather> getForecast() {
        return forecast;
    }
}

