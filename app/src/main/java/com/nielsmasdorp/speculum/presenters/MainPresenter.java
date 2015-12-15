package com.nielsmasdorp.speculum.presenters;

import com.nielsmasdorp.speculum.models.yahoo_weather.CurrentWeatherConditions;
import com.nielsmasdorp.speculum.services.GoogleCalendarService;
import com.nielsmasdorp.speculum.views.IMainView;
import com.nielsmasdorp.speculum.services.YahooWeatherService;
import com.nielsmasdorp.speculum.views.MainActivity;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Niels on 12/14/2015.
 */
public class MainPresenter {

    YahooWeatherService mYahooWeatherService;
    IMainView mMainView;

    public MainPresenter(IMainView view) {

        mMainView = view;
        mYahooWeatherService = new YahooWeatherService();
    }

    public void loadLatestCalendarEvent() {
        GoogleCalendarService.getCalendarEvents((MainActivity) mMainView, mCalendarListener);
    }

    private GoogleCalendarService.CalendarListener mCalendarListener = new GoogleCalendarService.CalendarListener() {
        @Override
        public void onCalendarUpdate(String title, String details) {

            mMainView.displayLatestCalendarEvent(title, details);
        }
    };

    public void loadWeather(String location) {

        String query = "select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"" +
                location + "\") and u=\"c\"";

        Observable<CurrentWeatherConditions> observable = mYahooWeatherService.getApi().getCurrentWeatherConditions(query, "json");
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<CurrentWeatherConditions>() {
                    @Override
                    public void onCompleted() {
                        // handle completed
                    }

                    @Override
                    public void onError(Throwable e) {
                        mMainView.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(CurrentWeatherConditions conditions) {

                        mMainView.displayCurrentWeather(conditions);
                    }
                });
    }
}
