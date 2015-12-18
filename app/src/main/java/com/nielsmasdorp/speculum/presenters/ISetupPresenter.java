package com.nielsmasdorp.speculum.presenters;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public interface ISetupPresenter {

    void launch(String location, String stock, String subreddit, String pollingDelay, boolean wind,
                boolean atmosphere, boolean sun, boolean celsius, boolean forecast);
}
