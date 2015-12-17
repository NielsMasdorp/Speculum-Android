package com.nielsmasdorp.speculum.presenters;

import com.nielsmasdorp.speculum.util.Constants;
import com.nielsmasdorp.speculum.views.ISetupView;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class SetupPresenterImpl implements ISetupPresenter {

    ISetupView mSetupView;

    public SetupPresenterImpl(ISetupView view) {

        mSetupView = view;
    }

    @Override
    public void launch(String location, String subreddit, String pollingDelay, boolean wind, boolean atmosphere, boolean sun, boolean celsius, boolean forecast) {

        if (pollingDelay.equals("") || pollingDelay.equals("0")) {
            pollingDelay = "30";
        }
        //TODO check validity of entered values
        mSetupView.navigateToMainActivity(location.length() == 0 ? Constants.LOCATION_DEFAULT : location,
                subreddit.length() == 0 ? Constants.SUBREDDIT_DEFAULT : subreddit, Integer.parseInt(pollingDelay),
                wind, atmosphere, sun, celsius, forecast);
    }
}
