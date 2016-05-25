package com.nielsmasdorp.speculum.presenters;

import com.nielsmasdorp.speculum.models.Configuration;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public interface SetupPresenter {

    void showError(String error);

    void validate(String location, String subreddit, String pollingDelay, boolean celsius, boolean voiceCommands, boolean rememberConfig, boolean simpleLayout);

    void launchMainActivity(Configuration configuration);
}

