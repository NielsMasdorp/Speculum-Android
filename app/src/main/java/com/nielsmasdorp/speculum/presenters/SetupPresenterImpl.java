package com.nielsmasdorp.speculum.presenters;

import android.util.Log;

import com.nielsmasdorp.speculum.models.CurrentWeather;
import com.nielsmasdorp.speculum.services.GoogleMapService;
import com.nielsmasdorp.speculum.services.SharedPreferenceService;
import com.nielsmasdorp.speculum.util.Constants;
import com.nielsmasdorp.speculum.views.ISetupView;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class SetupPresenterImpl implements ISetupPresenter {

    private static final String TAG = SetupPresenterImpl.class.getSimpleName();

    private WeakReference<ISetupView> mSetupView;

    private SharedPreferenceService mPreferenceService;
    private GoogleMapService mGoogleMapService;

    public SetupPresenterImpl(ISetupView view) {

        mSetupView = new WeakReference<>(view);
        mPreferenceService = SharedPreferenceService.instance();
        mGoogleMapService = new GoogleMapService();

        if (mPreferenceService.getRememberConfiguration())
            if (mSetupView.get() != null)
                mSetupView.get().navigateToMainActivity(mPreferenceService.getLocation(),
                        mPreferenceService.getSubreddit(),
                        mPreferenceService.getPollingDelay(),
                        mPreferenceService.getCelsius(),
                        mPreferenceService.getVoiceCommands(),
                        true,
                        mPreferenceService.getSimpleLayout());
    }

    @Override
    public void launch(String location, String subreddit, String pollingDelay, boolean celsius, boolean voiceCommands, boolean rememberConfig, boolean simpleLayout) {

        if (pollingDelay.equals("") || pollingDelay.equals("0"))
            pollingDelay = Constants.POLLING_DELAY_DEFAULT;

        if (location.isEmpty()) location = Constants.LOCATION_DEFAULT;

        if (subreddit.isEmpty()) subreddit = Constants.SUBREDDIT_DEFAULT;

        // temp variables
        final String finalSubreddit = subreddit;
        final String finalPollingDelay = pollingDelay;

        mGoogleMapService.getApi().getLatLongForAddress(location, "false")
                .flatMap(mGoogleMapService::getLatLong)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "GoogleMapService: ", e);
                        if (mSetupView.get() != null)
                            mSetupView.get().showError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(String latLng) {
                        if (rememberConfig) {
                            mPreferenceService.storeConfiguration(latLng, finalSubreddit, Integer.parseInt(finalPollingDelay), celsius, voiceCommands, rememberConfig, simpleLayout);
                        } else {
                            mPreferenceService.removeConfiguration();
                        }
                        if (mSetupView.get() != null)
                            mSetupView.get().navigateToMainActivity(latLng, finalSubreddit, Integer.parseInt(finalPollingDelay), celsius, voiceCommands, false, simpleLayout);
                    }
                });
    }
}
