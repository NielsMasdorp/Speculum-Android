package com.nielsmasdorp.speculum.util;

import com.nielsmasdorp.speculum.R;

import java.util.HashMap;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class WeatherIconGenerator {

    private static WeatherIconGenerator _instance;

    private HashMap<Integer, Integer> iconMap;

    public synchronized static WeatherIconGenerator getInstance()
    {
        if (_instance == null) {
            _instance = new WeatherIconGenerator();
        }
        return _instance;
    }

    private WeatherIconGenerator() {
        iconMap = new HashMap<>();
        //iconMap.put(0, R.drawable.ic_weather_to);
        iconMap.put(1, R.drawable.ic_weather_lightning);
        iconMap.put(2, R.drawable.ic_weather_windy);
        iconMap.put(3, R.drawable.ic_weather_lightning);
        iconMap.put(4, R.drawable.ic_weather_lightning);
        iconMap.put(5, R.drawable.ic_weather_rainy);
        iconMap.put(6, R.drawable.ic_weather_rainy);
        iconMap.put(7, R.drawable.ic_weather_snowy);
        iconMap.put(8, R.drawable.ic_weather_snowy);
        iconMap.put(9, R.drawable.ic_weather_rainy);
        iconMap.put(10, R.drawable.ic_weather_rainy);
        iconMap.put(11, R.drawable.ic_weather_pouring);
        iconMap.put(12, R.drawable.ic_weather_pouring);
        iconMap.put(13, R.drawable.ic_weather_snowy);
        iconMap.put(14, R.drawable.ic_weather_snowy);
        iconMap.put(15, R.drawable.ic_weather_snowy);
        iconMap.put(16, R.drawable.ic_weather_snowy);
        iconMap.put(17, R.drawable.ic_weather_hail);
        iconMap.put(18, R.drawable.ic_weather_rainy);
        iconMap.put(19, R.drawable.ic_weather_fog);
        iconMap.put(20, R.drawable.ic_weather_fog);
        iconMap.put(21, R.drawable.ic_weather_fog);
        iconMap.put(22, R.drawable.ic_weather_fog);
        iconMap.put(23, R.drawable.ic_weather_windy);
        iconMap.put(24, R.drawable.ic_weather_windy_variant);
        iconMap.put(25, R.drawable.ic_weather_cold);
        iconMap.put(26, R.drawable.ic_weather_cloudy);
        iconMap.put(27, R.drawable.ic_weather_cloudy);
        iconMap.put(28, R.drawable.ic_weather_cloudy);
        iconMap.put(29, R.drawable.ic_weather_cloudy_night);
        iconMap.put(30, R.drawable.ic_weather_partlycloudy);
        iconMap.put(31, R.drawable.ic_weather_night);
        iconMap.put(32, R.drawable.ic_weather_sunny);
        iconMap.put(33, R.drawable.ic_weather_night);
        iconMap.put(34, R.drawable.ic_weather_sunny);
        iconMap.put(35, R.drawable.ic_weather_hail);
        iconMap.put(36, R.drawable.ic_weather_hot);
        iconMap.put(37, R.drawable.ic_weather_lightning);
        iconMap.put(38, R.drawable.ic_weather_lightning);
        iconMap.put(39, R.drawable.ic_weather_lightning);
        iconMap.put(40, R.drawable.ic_weather_pouring);
        iconMap.put(41, R.drawable.ic_weather_snowy);
        iconMap.put(42, R.drawable.ic_weather_snowy);
        iconMap.put(43, R.drawable.ic_weather_snowy);
        iconMap.put(44, R.drawable.ic_weather_partlycloudy);
        iconMap.put(45, R.drawable.ic_weather_lightning);
        iconMap.put(46, R.drawable.ic_weather_snowy);
        iconMap.put(47, R.drawable.ic_weather_pouring);
        iconMap.put(3200, R.drawable.ic_alarm);
    }

    public Integer getIcon(Integer weatherCode) {
        return iconMap.get(weatherCode);
    }
}
