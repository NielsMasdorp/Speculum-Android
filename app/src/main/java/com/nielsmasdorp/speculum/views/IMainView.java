package com.nielsmasdorp.speculum.views;

import com.nielsmasdorp.speculum.models.CurrentWeather;
import com.nielsmasdorp.speculum.models.RedditPost;
import com.nielsmasdorp.speculum.models.StockInformation;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public interface IMainView {

    void displayCurrentWeather(CurrentWeather currentWeather);

    void displayLatestCalendarEvent(String event);

    void displayTopRedditPost(RedditPost redditPost);

    void displayStockInformation(StockInformation stockInformation);

    void hideProgressbar();

    void onError(String message);
}
