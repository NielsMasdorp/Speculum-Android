package com.nielsmasdorp.speculum.interactor;

import com.nielsmasdorp.speculum.models.CurrentWeather;
import com.nielsmasdorp.speculum.models.RedditPost;
import com.nielsmasdorp.speculum.models.YoMommaJoke;

import java.io.File;

import rx.Subscriber;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public interface MainInteractor {

    void loadLatestCalendarEvent(int updateDelay, Subscriber<String> subscriber);

    void loadTopRedditPost(String subreddit, int updateDelay, Subscriber<RedditPost> subscriber);

    void loadWeather(String location, boolean celsius, int updateDelay, String apiKey, Subscriber<CurrentWeather> subscriber);

    void loadYoMommaJoke(Subscriber<YoMommaJoke> subscriber);

    void setupRecognitionService(Subscriber<File> subscriber);

    void unSubscribe();
}
