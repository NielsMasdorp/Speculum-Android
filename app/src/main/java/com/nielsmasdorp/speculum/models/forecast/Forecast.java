package com.nielsmasdorp.speculum.models.forecast;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
@SuppressWarnings("unused")
public class Forecast {

    private String icon;
    private List<DayForecast> data = new ArrayList<>();

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<DayForecast> getData() {
        return data;
    }

    public void setData(List<DayForecast> data) {
        this.data = data;
    }
}
