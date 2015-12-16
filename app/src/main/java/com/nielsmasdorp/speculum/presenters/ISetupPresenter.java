package com.nielsmasdorp.speculum.presenters;

/**
 * Created by Niels on 12/16/2015.
 */
public interface ISetupPresenter {

    void launch(String location, String subreddit, int pollingDelay, boolean wind,
                boolean atmosphere, boolean sun, boolean celsius, boolean forecast);
}
