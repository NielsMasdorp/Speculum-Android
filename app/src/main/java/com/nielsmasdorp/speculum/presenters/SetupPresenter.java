package com.nielsmasdorp.speculum.presenters;

import com.nielsmasdorp.speculum.util.Constants;
import com.nielsmasdorp.speculum.views.ISetupView;

/**
 * Created by Niels on 12/14/2015.
 */
public class SetupPresenter {

    ISetupView mSetupView;

    public SetupPresenter(ISetupView view) {

        mSetupView = view;
    }

    public void launch(String location, String subreddit, int pollingDelay, boolean wind, boolean atmosphere, boolean sun, boolean celsius) {
        mSetupView.onSuccess(location.length() == 0 ? Constants.LOCATION_DEFAULT : location,
                subreddit.length() == 0 ? Constants.SUBREDDIT_DEFAULT : subreddit, pollingDelay == 0 ? Constants.POLLING_DEFAULT : pollingDelay,
                wind, atmosphere, sun, celsius);
    }
}
