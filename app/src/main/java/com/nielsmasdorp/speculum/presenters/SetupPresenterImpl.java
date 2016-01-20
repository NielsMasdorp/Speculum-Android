package com.nielsmasdorp.speculum.presenters;

import com.nielsmasdorp.speculum.util.Constants;
import com.nielsmasdorp.speculum.views.ISetupView;

import java.lang.ref.WeakReference;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class SetupPresenterImpl implements ISetupPresenter {

    private WeakReference<ISetupView> mSetupView;

    public SetupPresenterImpl(ISetupView view) {

        mSetupView = new WeakReference<>(view);
    }

    @Override
    public void launch(String location, String subreddit, String pollingDelay, boolean celsius) {

        if (pollingDelay.equals("") || pollingDelay.equals("0")) pollingDelay = Constants.POLLING_DELAY_DEFAULT;

        if (mSetupView.get() != null) mSetupView.get().navigateToMainActivity(location.length() == 0 ? Constants.LOCATION_DEFAULT : location,
                subreddit.length() == 0 ? Constants.SUBREDDIT_DEFAULT : subreddit, Integer.parseInt(pollingDelay), celsius);
    }
}
