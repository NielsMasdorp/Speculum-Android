package com.nielsmasdorp.speculum.presenters;

import com.nielsmasdorp.speculum.models.CurrentWeather;
import com.nielsmasdorp.speculum.services.YahooService;
import com.nielsmasdorp.speculum.util.Constants;
import com.nielsmasdorp.speculum.views.ISimpleView;
import com.nielsmasdorp.speculum.views.MainActivity;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import edu.cmu.pocketsphinx.Assets;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Gijs on 22-2-2016.
 */
public class SimplePresenterImpl implements ISimplePresenter {

    private YahooService mYahooService;

    private WeakReference<ISimpleView> mMainView;

    private List<Subscription> mSubscriptions;

    public SimplePresenterImpl(ISimpleView view) {

        mMainView = new WeakReference<>(view);
        mYahooService = new YahooService();
        mSubscriptions = new ArrayList<>();
    }



    @Override
    public void loadWeather(final String location, boolean celsius, int updateDelay) {

        final String query = celsius ? Constants.WEATHER_QUERY_SECOND_CELSIUS : Constants.WEATHER_QUERY_SECOND_FAHRENHEIT;

        mSubscriptions.add(Observable.interval(0, updateDelay, TimeUnit.MINUTES)
                .flatMap(ignore -> mYahooService.getApi().getCurrentWeatherConditions(Constants.WEATHER_QUERY_FIRST +
                        location + query, Constants.YAHOO_QUERY_FORMAT))
                .flatMap(response -> mYahooService.getCurrentWeather(response))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<CurrentWeather>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mMainView.get() != null)
                            mMainView.get().onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(CurrentWeather weather) {

                        if (mMainView.get() != null) mMainView.get().displayCurrentWeather(weather);
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

    @Override
    public void setupRecognitionService() {

        if (mMainView.get() != null) {

            prepareAssetsForRecognizer()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Void>() {
                        @Override
                        public void onCompleted() {
                            mMainView.get().setListeningMode(Constants.KWS_SEARCH);
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (mMainView.get() != null)
                                mMainView.get().onError(e.getLocalizedMessage());
                        }

                        @Override
                        public void onNext(Void aVoid) {
                        }
                    });
        }
    }

    @Override
    public void processCommand(String command) {

        if (mMainView.get() != null) {

            switch (command) {
                case Constants.KEYPHRASE:
                    // wake up and listen for commands
                    mMainView.get().talk(Constants.WAKE_NOTIFICATION);
                    mMainView.get().setListeningMode(Constants.COMMANDS_SEARCH);
                    break;
                case Constants.SLEEP_PHRASE:
                    // go to sleep
                    mMainView.get().talk(Constants.SLEEP_NOTIFICATION);
                    mMainView.get().setListeningMode(Constants.KWS_SEARCH);
                    break;
                case Constants.UPDATE_PHRASE:
                    // update data
                    mMainView.get().talk(Constants.UPDATE_NOTIFICATION);
                    unSubscribe();
                    mMainView.get().startPolling();
                    // go to sleep again and wait for activation phrase
                    mMainView.get().setListeningMode(Constants.KWS_SEARCH);
                    break;
            }

        }
    }

    private Observable<Void> prepareAssetsForRecognizer() {
        return Observable.defer(() -> {
            try {
                Assets assets = new Assets((MainActivity) mMainView.get());
                File assetDir = assets.syncAssets();
                mMainView.get().setupRecognizer(assetDir);
            } catch (IOException e) {
                throw new RuntimeException("IOException: " + e.getLocalizedMessage());
            }
            return Observable.empty();
        });
    }
}
