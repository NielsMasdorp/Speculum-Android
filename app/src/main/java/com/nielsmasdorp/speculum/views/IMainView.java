package com.nielsmasdorp.speculum.views;

import com.nielsmasdorp.speculum.models.CurrentWeather;
import com.nielsmasdorp.speculum.models.RedditPost;
import com.nielsmasdorp.speculum.models.reddit.RedditResponse;
import com.nielsmasdorp.speculum.models.yahoo_weather.YahooWeatherResponse;

/**
 * Created by Niels on 12/14/2015.
 */
public interface IMainView {

    void displayCurrentWeather(CurrentWeather currentWeather);

    void displayLatestCalendarEvent(String event);

    void displayTopRedditPost(RedditPost redditPost);

    void hideProgressbar();

    void onError(String message);
}
