package com.nielsmasdorp.speculum.util;

import android.support.annotation.DrawableRes;

import com.nielsmasdorp.speculum.R;

import java.util.HashMap;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class WeatherIconGenerator {

    private HashMap<String, Integer> iconMap;

    public WeatherIconGenerator() {

        initMap();
    }

    /**
     * Initialize the weather icon map.
     * Every icon has a String identifier mapped to a resource id.
     */
    private void initMap() {

        iconMap = new HashMap<>();
        iconMap.put("tornado", R.drawable.ic_weather_tornado);
        iconMap.put("thunderstorm", R.drawable.ic_weather_lightning);
        iconMap.put("wind", R.drawable.ic_weather_windy);
        iconMap.put("rain", R.drawable.ic_weather_pouring);
        iconMap.put("hail", R.drawable.ic_weather_hail);
        iconMap.put("snow", R.drawable.ic_weather_snowy);
        iconMap.put("sleet", R.drawable.ic_weather_sleet);
        iconMap.put("fog", R.drawable.ic_weather_fog);
        iconMap.put("partly-cloudy-night", R.drawable.ic_weather_cloudy_night);
        iconMap.put("clear-night", R.drawable.ic_weather_night);
        iconMap.put("clear-day", R.drawable.ic_weather_sunny);
        iconMap.put("partly-cloudy-day", R.drawable.ic_weather_partlycloudy);
        iconMap.put("cloudy", R.drawable.ic_weather_cloudy);
    }

    /**
     * Get an icon for a weather type.
     *
     * @param icon weather type (e.g "tornado")
     * @return icon resource
     */
    @DrawableRes
    public Integer getIcon(String icon) {
        if (icon.contains(icon)) {
            return iconMap.get(icon);
        }
        return R.drawable.ic_weather_sunny;
    }
}
