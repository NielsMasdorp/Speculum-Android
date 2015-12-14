package com.nielsmasdorp.speculum.presenters;

import com.nielsmasdorp.speculum.views.ISetupView;

/**
 * Created by Niels on 12/14/2015.
 */
public class SetupPresenter {

    ISetupView mSetupView;

    public SetupPresenter(ISetupView view) {

        mSetupView = view;
    }

    public void launch(String location) {
        mSetupView.onSuccess(location);
    }
}
