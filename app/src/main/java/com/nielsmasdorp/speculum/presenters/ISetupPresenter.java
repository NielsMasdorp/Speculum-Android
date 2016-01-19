package com.nielsmasdorp.speculum.presenters;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public interface ISetupPresenter {

    void launch(String location, String subreddit, String pollingDelay, boolean celsius);
}
