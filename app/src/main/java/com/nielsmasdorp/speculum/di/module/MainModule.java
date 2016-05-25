package com.nielsmasdorp.speculum.di.module;

import android.app.Application;

import com.nielsmasdorp.speculum.di.PerActivity;
import com.nielsmasdorp.speculum.interactor.MainInteractor;
import com.nielsmasdorp.speculum.interactor.MainInteractorImpl;
import com.nielsmasdorp.speculum.presenters.MainPresenter;
import com.nielsmasdorp.speculum.presenters.MainPresenterImpl;
import com.nielsmasdorp.speculum.services.ForecastIOService;
import com.nielsmasdorp.speculum.services.GoogleCalendarService;
import com.nielsmasdorp.speculum.services.RedditService;
import com.nielsmasdorp.speculum.services.YoMommaService;
import com.nielsmasdorp.speculum.util.WeatherIconGenerator;
import com.nielsmasdorp.speculum.views.MainView;

import dagger.Module;
import dagger.Provides;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
@Module
public class MainModule {

    private MainView mainView;

    public MainModule(MainView mainView) {

        this.mainView = mainView;
    }

    @Provides
    @PerActivity
    public MainPresenter provideMainPresenter(MainInteractor interactor, Application application) {

        return new MainPresenterImpl(mainView, interactor, application);
    }

    @Provides
    @PerActivity
    public MainInteractor provideMainInteractor(Application application,
                                                ForecastIOService forecastIOService,
                                                GoogleCalendarService googleMapService,
                                                RedditService redditService,
                                                YoMommaService yoMommaService, WeatherIconGenerator iconGenerator) {

        return new MainInteractorImpl(application, forecastIOService, googleMapService, redditService, yoMommaService, iconGenerator);
    }
}
