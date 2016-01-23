package com.nielsmasdorp.speculum.views;

import com.nielsmasdorp.speculum.models.CurrentWeather;
import com.nielsmasdorp.speculum.models.RedditPost;

import java.io.File;
import java.io.IOException;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public interface IMainView {

    void displayCurrentWeather(CurrentWeather currentWeather);

    void displayLatestCalendarEvent(String event);

    void displayTopRedditPost(RedditPost redditPost);

    void showContent(int which);

    void onError(String message);

    void setupRecognizer(File assetDir) throws IOException;

    void setListeningMode(String mode);

    void startPolling();

    void talk(String message);
}
