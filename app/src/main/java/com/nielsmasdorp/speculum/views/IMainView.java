package com.nielsmasdorp.speculum.views;

import com.nielsmasdorp.speculum.models.yahoo_weather.CurrentWeatherConditions;

/**
 * Created by Niels on 12/14/2015.
 */
public interface IMainView {

    void displayCurrentWeather(CurrentWeatherConditions conditions);

    void showLoading();

    void hideLoading();

    void onError(String message);
}
