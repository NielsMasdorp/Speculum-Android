package com.nielsmasdorp.speculum.views;

import com.nielsmasdorp.speculum.models.reddit.RedditResponse;
import com.nielsmasdorp.speculum.models.yahoo_weather.CurrentWeatherConditions;

/**
 * Created by Niels on 12/14/2015.
 */
public interface IMainView {

    void displayCurrentWeather(CurrentWeatherConditions conditions);

    void displayLatestCalendarEvent(String event);

    void displayTopRedditPost(RedditResponse redditResponse);

    void hideProgressbar();

    void onError(String message);
}
