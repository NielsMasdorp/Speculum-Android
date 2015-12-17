package com.nielsmasdorp.speculum.presenters;

import com.nielsmasdorp.speculum.models.CurrentWeather;
import com.nielsmasdorp.speculum.models.reddit.RedditResponse;
import com.nielsmasdorp.speculum.models.yahoo_weather.YahooWeatherResponse;
import com.nielsmasdorp.speculum.services.GoogleCalendarService;
import com.nielsmasdorp.speculum.services.RedditService;
import com.nielsmasdorp.speculum.util.Constants;
import com.nielsmasdorp.speculum.views.IMainView;
import com.nielsmasdorp.speculum.services.YahooWeatherService;
import com.nielsmasdorp.speculum.views.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Niels on 12/14/2015.
 */
public class MainPresenterImpl implements IMainPresenter {

    YahooWeatherService mYahooWeatherService;
    GoogleCalendarService mGoogleCalendarService;
    RedditService mRedditService;

    IMainView mMainView;

    List<Subscription> mSubscriptions;

    public MainPresenterImpl(IMainView view) {

        mMainView = view;
        mYahooWeatherService = new YahooWeatherService();
        mRedditService = new RedditService();
        mGoogleCalendarService = new GoogleCalendarService((MainActivity) mMainView);
        mSubscriptions = new ArrayList<>();
    }

    @Override
    public void loadLatestCalendarEvent(int updateDelay) {

        mSubscriptions.add(Observable.interval(0, updateDelay, TimeUnit.MINUTES)
                .flatMap(new Func1<Long, Observable<String>>() {
                    @Override
                    public Observable<String> call(Long ignore) {
                        return mGoogleCalendarService.getLatestCalendarEvent();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mMainView.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(String event) {

                        mMainView.displayLatestCalendarEvent(event);
                    }
                }));
    }

    @Override
    public void loadWeather(final String location, boolean celsius, int updateDelay) {

        final String query = celsius ? Constants.WEATHER_QUERY_SECOND_CELSIUS : Constants.WEATHER_QUERY_SECOND_FAHRENHEIT;

        mSubscriptions.add(Observable.interval(0, updateDelay, TimeUnit.MINUTES)
                .flatMap(new Func1<Long, Observable<YahooWeatherResponse>>() {
                    @Override
                    public Observable<YahooWeatherResponse> call(Long ignore) {
                        return mYahooWeatherService.getApi().getCurrentWeatherConditions(Constants.WEATHER_QUERY_FIRST +
                                location + query, Constants.WEATHER_QUERY_FORMAT);
                    }
                })
                .flatMap(new Func1<YahooWeatherResponse, Observable<CurrentWeather>>() {
                    @Override
                    public Observable<CurrentWeather> call(YahooWeatherResponse response) {
                        return mYahooWeatherService.getCurrentWeather(response);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<CurrentWeather>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mMainView.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(CurrentWeather weather) {

                        mMainView.displayCurrentWeather(weather);
                    }
                }));
    }

    @Override
    public void loadTopRedditPost(final String subreddit, int updateDelay) {

        mSubscriptions.add(Observable.interval(0, updateDelay, TimeUnit.MINUTES)
                .flatMap(new Func1<Long, Observable<RedditResponse>>() {
                    @Override
                    public Observable<RedditResponse> call(Long ignore) {
                        return mRedditService.getApi().getTopRedditPostForSubreddit(subreddit, Constants.REDDIT_LIMIT);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<RedditResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mMainView.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(RedditResponse redditResponse) {

                        mMainView.displayTopRedditPost(redditResponse);
                    }
                }));
    }

    @Override
    public void unSubscribe() {
        for (Subscription subscription : mSubscriptions) {
            if (!subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
        }
        mSubscriptions.clear();
    }
}
