package com.nielsmasdorp.speculum.views;

/**
 * Created by Niels on 12/14/2015.
 */
public interface ISetupView {

    void onSuccess(String location, String subreddit, boolean wind, boolean atmosphere, boolean sun, boolean celsius);
}
