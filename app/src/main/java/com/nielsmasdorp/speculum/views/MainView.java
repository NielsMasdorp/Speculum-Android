package com.nielsmasdorp.speculum.views;

import com.nielsmasdorp.speculum.models.Configuration;
import com.nielsmasdorp.speculum.models.CurrentWeather;
import com.nielsmasdorp.speculum.models.RedditPost;

import java.io.File;
import java.io.IOException;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public interface MainView extends BaseView {

    void displayCurrentWeather(Configuration configuration, CurrentWeather currentWeather);

    void displayLatestCalendarEvent(String event);

    void displayTopRedditPost(RedditPost redditPost);

    void showContent(int which);

    void setupRecognizer(File assetDir) throws IOException;

    void setListeningMode(String mode);

    void talk(String message);
}
