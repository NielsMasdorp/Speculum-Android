package com.nielsmasdorp.speculum.presenters;

import com.nielsmasdorp.speculum.services.SharedPreferenceService;
import com.nielsmasdorp.speculum.util.Constants;
import com.nielsmasdorp.speculum.views.ISetupView;
import com.nielsmasdorp.speculum.views.SetupActivity;

import java.lang.ref.WeakReference;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class SetupPresenterImpl implements ISetupPresenter {

    private WeakReference<ISetupView> mSetupView;

    private SharedPreferenceService mPreferenceService;

    public SetupPresenterImpl(ISetupView view) {

        mSetupView = new WeakReference<>(view);
        mPreferenceService = SharedPreferenceService.instance();

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

        if (rememberConfig) {
            mPreferenceService.storeConfiguration(location, subreddit, Integer.parseInt(pollingDelay), celsius, voiceCommands, rememberConfig, simpleLayout);
        } else {
            mPreferenceService.removeConfiguration();
        }

        if (mSetupView.get() != null)
            mSetupView.get().navigateToMainActivity(location, subreddit, Integer.parseInt(pollingDelay), celsius, voiceCommands, false, simpleLayout);
    }
}
