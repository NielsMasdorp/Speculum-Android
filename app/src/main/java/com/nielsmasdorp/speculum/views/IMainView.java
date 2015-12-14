package com.nielsmasdorp.speculum.views;

import com.nielsmasdorp.speculum.models.yahoo_weather.CurrentWeatherConditions;

/**
 * Created by Niels on 12/14/2015.
 */
public interface IMainView {

    void displayCurrentWeather(CurrentWeatherConditions conditions);

    void displayLatestCalendarEvent(String title, String details);

    void setProgressBarVisibility(int visibility);

    void onError(String message);
}
