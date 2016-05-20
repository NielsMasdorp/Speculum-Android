package com.nielsmasdorp.speculum.models.forecast;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
@SuppressWarnings("unused")
public class Currently {

    private Integer time;
    private String summary;
    private String icon;
    private Double temperature;
    private Double apparentTemperature;
    private Double humidity;
    private Double windSpeed;
    private Double windBearing;
    private Double visibility;
    private Double pressure;

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getSummary() {
        return summary;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Double getTemperature() {
        return temperature;
    }

    public Double getApparentTemperature() {
        return apparentTemperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public Double getWindBearing() {
        return windBearing;
    }

    public Double getVisibility() {
        return visibility;
    }

    public Double getPressure() {
        return pressure;
    }
}
