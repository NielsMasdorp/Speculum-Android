package com.nielsmasdorp.speculum.presenters;

import com.nielsmasdorp.speculum.models.Configuration;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public interface MainPresenter {

    void finish();

    void setConfiguration(Configuration configuration);

    void start(boolean hasAccessToCalendar);

    void showError(String error);
}
