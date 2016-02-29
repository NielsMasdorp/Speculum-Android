package com.nielsmasdorp.speculum.presenters;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public interface IMainPresenter {

    void loadLatestCalendarEvent(int updateDelay);

    void loadWeather(final String location, boolean celsius, int updateDelay);

    void loadTopRedditPost(final String subreddit, int updateDelay);

    void loadJoke();

    void unSubscribe();

    void setupRecognitionService();

    void processCommand(String command);
}
