package com.nielsmasdorp.speculum.presenters;

import com.nielsmasdorp.speculum.models.Configuration;
import com.nielsmasdorp.speculum.models.CurrentWeather;
import com.nielsmasdorp.speculum.models.RedditPost;

import java.io.File;
import java.io.IOException;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public interface MainPresenter {

    void finish();

    void setConfiguration(Configuration configuration);

    void start(boolean hasAccessToCalendar);

    void processVoiceCommand(String command);

    void showError(String error);

    void displayLatestCalendarEvent(String event);

    void displayCurrentWeather(CurrentWeather weather);

    void displayTopRedditPost(RedditPost redditPost);

    void setListeningMode(String mode);

    void setupRecognizer(File assetDir) throws IOException;

    void talk(String sentence);
}
