package com.nielsmasdorp.speculum.util;

import com.nielsmasdorp.speculum.R;

import java.util.HashMap;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class WeatherIconGenerator {

    private static WeatherIconGenerator _instance;

    private HashMap<String, Integer> iconMap;

    public synchronized static WeatherIconGenerator getInstance() {
        if (_instance == null) {
            _instance = new WeatherIconGenerator();
        }
        return _instance;
    }

    private WeatherIconGenerator() {
        iconMap = new HashMap<>();
        iconMap.put("tornado", R.drawable.ic_weather_tornado);
        iconMap.put("thunderstorm", R.drawable.ic_weather_lightning);
        iconMap.put("wind", R.drawable.ic_weather_windy);
        iconMap.put("rain", R.drawable.ic_weather_rainy);
        iconMap.put("snow", R.drawable.ic_weather_snowy);
        iconMap.put("sleet", R.drawable.ic_weather_snowy);
        iconMap.put("fog", R.drawable.ic_weather_fog);
        iconMap.put("partly-cloudy-night", R.drawable.ic_weather_cloudy_night);
        iconMap.put("clear-night", R.drawable.ic_weather_night);
        iconMap.put("clear-day", R.drawable.ic_weather_sunny);
        iconMap.put("partly-cloudy-day", R.drawable.ic_weather_partlycloudy);
    }

    public Integer getIcon(String icon) {
        return iconMap.get(icon);
    }
}
