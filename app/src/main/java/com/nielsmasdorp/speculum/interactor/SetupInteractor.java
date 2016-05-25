package com.nielsmasdorp.speculum.interactor;

import com.nielsmasdorp.speculum.models.Configuration;
import com.nielsmasdorp.speculum.presenters.SetupPresenter;
import com.nielsmasdorp.speculum.presenters.SetupPresenterImpl;

import rx.Subscriber;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public interface SetupInteractor {

    void validate(String location, String subreddit, String pollingDelay, boolean celsius, boolean voiceCommands, boolean rememberConfig, boolean simpleLayout, Subscriber<Configuration> configurationSubscriber);

    void start(Subscriber<Configuration> configurationSubscriber);
}
