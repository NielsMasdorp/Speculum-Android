package com.nielsmasdorp.speculum.presenters;

import com.nielsmasdorp.speculum.models.yahoo_weather.CurrentWeatherConditions;
import com.nielsmasdorp.speculum.views.IMainView;
import com.nielsmasdorp.speculum.services.YahooWeatherService;

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

    public void loadWeather() {

        Observable<CurrentWeatherConditions> observable = mYahooWeatherService.getApi().getCurrentWeatherConditions();
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
