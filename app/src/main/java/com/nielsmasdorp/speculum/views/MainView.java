package com.nielsmasdorp.speculum.views;

import com.nielsmasdorp.speculum.models.Weather;
import com.nielsmasdorp.speculum.models.RedditPost;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public interface MainView extends BaseView {

    void showListening();

    void hideListening();

    void showMap(String location);

    void hideMap();

    void displayCurrentWeather(Weather weather, boolean isSimpleLayout);

    void displayLatestCalendarEvent(String event);

    void displayTopRedditPost(RedditPost redditPost);
}
