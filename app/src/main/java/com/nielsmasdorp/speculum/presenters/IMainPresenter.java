package com.nielsmasdorp.speculum.presenters;

/**
 * Created by Niels on 12/16/2015.
 */
public interface IMainPresenter {

    void loadLatestCalendarEvent(int updateDelay);

    void loadWeather(final String location, boolean celsius, int updateDelay);

    void loadTopRedditPost(final String subreddit, int updateDelay);

    void unSubscribe();
}
