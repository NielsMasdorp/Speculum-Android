package com.nielsmasdorp.speculum.models;

import com.nielsmasdorp.speculum.models.yahoo_weather.Forecast;

import java.util.List;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class CurrentWeather {

    private String title;
    private String condition;
    private String temperature;
    private String humidity;
    private String pressure;
    private String visibility;
    private String sunrise;
    private String sunset;
    private String windSpeed;
    private String windTemperature;
    private List<Forecast> forecast;

    public static class Builder {

        private String title;
        private String condition;
        private String temperature;
        private String humidity;
        private String pressure;
        private String visibility;
        private String sunrise;
        private String sunset;
        private String windSpeed;
        private String windTemperature;
        private List<Forecast> forecast;

        public Builder title(String title) { this.title = title; return this; }
        public Builder condition(String condition) { this.condition = condition; return this; }
        public Builder temperature(String temperature) { this.temperature = temperature; return this; }
        public Builder humidity(String humidity) { this.humidity = humidity; return this; }
        public Builder pressure(String pressure) { this.pressure = pressure; return this; }
        public Builder visibility(String visibility) { this.visibility = visibility; return this; }
        public Builder sunrise(String sunrise) { this.sunrise = sunrise; return this; }
        public Builder sunset(String sunset) { this.sunset = sunset; return this; }
        public Builder windSpeed(String windSpeed) { this.windSpeed = windSpeed; return this; }
        public Builder windTemperature(String windTemperature) { this.windTemperature = windTemperature; return this; }
        public Builder forecast(List<Forecast> forecast) { this.forecast = forecast; return this; }

        public CurrentWeather build() {

            return new CurrentWeather(this);
        }
    }

    private CurrentWeather(Builder builder) {

        this.title = builder.title;
        this.condition = builder.condition;
        this.temperature = builder.temperature;
        this.humidity = builder.humidity;
        this.pressure = builder.pressure;
        this.visibility = builder.visibility;
        this.sunrise = builder.sunrise;
        this.sunset = builder.sunset;
        this.windSpeed = builder.windSpeed;
        this.windTemperature = builder.windTemperature;
        this.forecast = builder.forecast;
    }

    public String getTitle() {
        return title;
    }

    public String getCondition() {
        return condition;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public String getVisibility() {
        return visibility;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public String getWindTemperature() {
        return windTemperature;
    }

    public List<Forecast> getForecast() {
        return forecast;
    }
}

