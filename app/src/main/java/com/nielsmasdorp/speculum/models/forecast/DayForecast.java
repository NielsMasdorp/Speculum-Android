package com.nielsmasdorp.speculum.models.forecast;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
@SuppressWarnings("unused")
public class DayForecast {

    private Integer time;
    private String icon;
    private Double temperatureMin;
    private Double temperatureMax;

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Double getTemperatureMin() {
        return temperatureMin;
    }

    public Double getTemperatureMax() {
        return temperatureMax;
    }
}
