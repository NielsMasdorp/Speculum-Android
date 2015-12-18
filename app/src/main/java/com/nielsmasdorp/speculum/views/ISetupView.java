package com.nielsmasdorp.speculum.views;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public interface ISetupView {

    void navigateToMainActivity(String location, String stock, String subreddit, int pollingDelay, boolean wind,
                                boolean atmosphere, boolean sun, boolean celsius, boolean forecast);
}
