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
    public void launch(String location, String stock, String subreddit, String pollingDelay,
                       boolean wind, boolean atmosphere, boolean sun, boolean celsius, boolean forecast) {

        if (pollingDelay.equals("") || pollingDelay.equals("0")) {
            pollingDelay = "30";
        }

        if (mSetupView.get() != null) mSetupView.get().navigateToMainActivity(location.length() == 0 ? Constants.LOCATION_DEFAULT : location,
                stock.length() == 0 ? Constants.STOCK_DEFAULT : stock,
                subreddit.length() == 0 ? Constants.SUBREDDIT_DEFAULT : subreddit, Integer.parseInt(pollingDelay),
                wind, atmosphere, sun, celsius, forecast);
    }
}
