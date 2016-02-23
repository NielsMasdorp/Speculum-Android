package com.nielsmasdorp.speculum.presenters;

/**
 * Created by Gijs on 22-2-2016.
 */
public interface ISimplePresenter {

    void loadWeather(final String location, boolean celsius, int updateDelay);

    void unSubscribe();

    void setupRecognitionService();

    void processCommand(String command);
}
