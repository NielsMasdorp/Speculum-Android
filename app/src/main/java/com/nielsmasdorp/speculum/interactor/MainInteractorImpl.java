package com.nielsmasdorp.speculum.interactor;

import android.app.Application;

import com.nielsmasdorp.speculum.models.RedditPost;
import com.nielsmasdorp.speculum.models.Weather;
import com.nielsmasdorp.speculum.models.YoMommaJoke;
import com.nielsmasdorp.speculum.util.Observables;
import com.nielsmasdorp.speculum.services.ForecastIOService;
import com.nielsmasdorp.speculum.services.GoogleCalendarService;
import com.nielsmasdorp.speculum.services.RedditService;
import com.nielsmasdorp.speculum.services.YoMommaService;
import com.nielsmasdorp.speculum.util.Constants;
import com.nielsmasdorp.speculum.util.WeatherIconGenerator;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import edu.cmu.pocketsphinx.Assets;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class MainInteractorImpl implements MainInteractor {

    private static int AMOUNT_OF_RETRIES = 10;
    private static int DELAY_IN_SECONDS = 1;

    private Application application;
    private ForecastIOService forecastIOService;
    private GoogleCalendarService googleCalendarService;
    private RedditService redditService;
    private YoMommaService yoMommaService;
    private WeatherIconGenerator weatherIconGenerator;
    private CompositeSubscription compositeSubscription;

    public MainInteractorImpl(Application application, ForecastIOService forecastIOService,
                              GoogleCalendarService googleCalendarService, RedditService redditService,
                              YoMommaService yoMommaService, WeatherIconGenerator weatherIconGenerator) {

        this.application = application;
        this.forecastIOService = forecastIOService;
        this.googleCalendarService = googleCalendarService;
        this.redditService = redditService;
        this.yoMommaService = yoMommaService;
        this.weatherIconGenerator = weatherIconGenerator;
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void loadCalendarEvents(int updateDelay, Subscriber<String> subscriber) {

        compositeSubscription.add(Observable.interval(0, updateDelay, TimeUnit.MINUTES)
                .flatMap(ignore -> googleCalendarService.getCalendarEvents())
                .retryWhen(Observables.exponentialBackoff(AMOUNT_OF_RETRIES, DELAY_IN_SECONDS, TimeUnit.SECONDS))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber));
    }

    @Override
    public void loadTopRedditPost(String subreddit, int updateDelay, Subscriber<RedditPost> subscriber) {

        compositeSubscription.add(Observable.interval(0, updateDelay, TimeUnit.MINUTES)
                .flatMap(ignore -> redditService.getApi().getTopRedditPostForSubreddit(subreddit, Constants.REDDIT_LIMIT))
                .flatMap(redditService::getRedditPost)
                .retryWhen(Observables.exponentialBackoff(AMOUNT_OF_RETRIES, DELAY_IN_SECONDS, TimeUnit.SECONDS))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber));
    }

    @Override
    public void loadWeather(String location, boolean celsius, int updateDelay, String apiKey, Subscriber<Weather> subscriber) {

        final String query = celsius ? Constants.WEATHER_QUERY_SECOND_CELSIUS : Constants.WEATHER_QUERY_SECOND_FAHRENHEIT;

        compositeSubscription.add(Observable.interval(0, updateDelay, TimeUnit.MINUTES)
                .flatMap(ignore -> forecastIOService.getApi().getCurrentWeatherConditions(apiKey, location, query))
                .flatMap(response -> forecastIOService.getCurrentWeather(response, weatherIconGenerator, application, celsius))
                .retryWhen(Observables.exponentialBackoff(AMOUNT_OF_RETRIES, DELAY_IN_SECONDS, TimeUnit.SECONDS))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber));
    }

    @Override
    public void loadYoMommaJoke(Subscriber<YoMommaJoke> subscriber) {

        yoMommaService.getApi().getJoke()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    @Override
    public void getAssetsDirForSpeechRecognizer(Subscriber<File> subscriber) {

        Observable.defer(() -> {
            try {
                Assets assets = new Assets(application);
                File assetDir = assets.syncAssets();
                return Observable.just(assetDir);
            } catch (IOException e) {
                throw new RuntimeException("IOException: " + e.getLocalizedMessage());
            }
        })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    @Override
    public void unSubscribe() {
        compositeSubscription.clear();
    }
}
