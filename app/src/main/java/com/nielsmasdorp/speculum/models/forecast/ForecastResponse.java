package com.nielsmasdorp.speculum.models.forecast;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
@SuppressWarnings("unused")
public class ForecastResponse {

    private Currently currently;
    private Forecast daily;

    public Currently getCurrently() {
        return currently;
    }

    public Forecast getForecast() {
        return daily;
    }
}
