package com.nielsmasdorp.speculum.views;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public interface ISetupView {

    void navigateToMainActivity(String location, String subreddit, int pollingDelay, boolean celsius, boolean voiceCommands, boolean rememberConfig, boolean simpleLayout);
}
