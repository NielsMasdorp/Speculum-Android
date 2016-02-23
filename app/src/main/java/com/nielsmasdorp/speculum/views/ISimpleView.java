package com.nielsmasdorp.speculum.views;

import com.nielsmasdorp.speculum.models.CurrentWeather;
import com.nielsmasdorp.speculum.models.RedditPost;

import java.io.File;
import java.io.IOException;

/**
 * Created by Gijs on 22-2-2016.
 */
public interface ISimpleView {


    void displayCurrentWeather(CurrentWeather currentWeather);

    void showContent(int which);

    void onError(String message);

    void setupRecognizer(File assetDir) throws IOException;

    void setListeningMode(String mode);

    void startPolling();

    void talk(String message);
}
