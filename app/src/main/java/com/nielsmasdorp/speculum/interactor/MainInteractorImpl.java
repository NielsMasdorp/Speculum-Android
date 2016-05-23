package com.nielsmasdorp.speculum.interactor;

import android.app.Application;

import com.nielsmasdorp.speculum.models.CurrentWeather;
import com.nielsmasdorp.speculum.models.RedditPost;
import com.nielsmasdorp.speculum.models.YoMommaJoke;
import com.nielsmasdorp.speculum.services.ForecastIOService;
import com.nielsmasdorp.speculum.services.GoogleCalendarService;
import com.nielsmasdorp.speculum.services.RedditService;
import com.nielsmasdorp.speculum.services.YoMommaService;
import com.nielsmasdorp.speculum.util.Constants;

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

    private Application application;
    private ForecastIOService forecastIOService;
    private GoogleCalendarService googleCalendarService;
    private RedditService redditService;
    private YoMommaService yoMommaService;
    private CompositeSubscription compositeSubscription;

    public MainInteractorImpl(Application application, ForecastIOService forecastIOService,
                              GoogleCalendarService googleCalendarService, RedditService redditService,
                              YoMommaService yoMommaService) {

        this.application = application;
        this.forecastIOService = forecastIOService;
        this.googleCalendarService = googleCalendarService;
        this.redditService = redditService;
        this.yoMommaService = yoMommaService;
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void loadLatestCalendarEvent(int updateDelay, Subscriber<String> subscriber) {

        compositeSubscription.add(Observable.interval(0, updateDelay, TimeUnit.MINUTES)
                .flatMap(ignore -> googleCalendarService.getLatestCalendarEvent())
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber));
    }

    @Override
    public void loadWeather(String location, boolean celsius, int updateDelay, String apiKey, Subscriber<CurrentWeather> subscriber) {

        final String query = celsius ? Constants.WEATHER_QUERY_SECOND_CELSIUS : Constants.WEATHER_QUERY_SECOND_FAHRENHEIT;

        compositeSubscription.add(Observable.interval(0, updateDelay, TimeUnit.MINUTES)
                .flatMap(ignore -> forecastIOService.getApi().getCurrentWeatherConditions(apiKey, location, query))
                .flatMap(forecastIOService::getCurrentWeather)
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
    public void setupRecognitionService(Subscriber<File> subscriber) {

        prepareAssetsForRecognizer()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    private Observable<File> prepareAssetsForRecognizer() {
        return Observable.defer(() -> {
            try {
                Assets assets = new Assets(application);
                File assetDir = assets.syncAssets();
                return Observable.just(assetDir);
            } catch (IOException e) {
                throw new RuntimeException("IOException: " + e.getLocalizedMessage());
            }
        });
    }


    @Override
    public void unSubscribe() {

        compositeSubscription.unsubscribe();
        compositeSubscription = new CompositeSubscription();
    }
}
