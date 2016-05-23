package com.nielsmasdorp.speculum.presenters;

import com.nielsmasdorp.speculum.interactor.SetupInteractor;
import com.nielsmasdorp.speculum.models.Configuration;
import com.nielsmasdorp.speculum.views.SetupView;

import rx.Subscriber;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class SetupPresenterImpl implements SetupPresenter {

    private SetupView view;
    private SetupInteractor interactor;

    public SetupPresenterImpl(SetupView view, SetupInteractor interactor) {

        this.view = view;
        this.interactor = interactor;
        interactor.start(new ConfigurationSubscriber());
    }

    /**
     * Validate user settings
     *
     * @param location       could be city/address etc.
     * @param subreddit      subreddit string
     * @param pollingDelay   update UI every x minutes
     * @param celsius        metric data or imperial
     * @param voiceCommands  should listen for voice commands
     * @param rememberConfig should remember configuration for next app start
     * @param simpleLayout   simple or verbose layout
     */
    @Override
    public void validate(String location,
                         String subreddit,
                         String pollingDelay,
                         boolean celsius,
                         boolean voiceCommands,
                         boolean rememberConfig,
                         boolean simpleLayout) {

        interactor.validate(location,
                subreddit, pollingDelay,
                celsius, voiceCommands,
                rememberConfig,
                simpleLayout,
                new ConfigurationSubscriber());
    }

    /**
     * Show error message in toast
     *
     * @param error message to show
     */
    @Override
    public void showError(String error) {

        view.showError(error);
    }

    /**
     * Launch main activity with a configuration
     *
     * @param configuration all relevant settings
     */
    @Override
    public void launchMainActivity(Configuration configuration) {

        view.navigateToMainActivity(configuration);
    }

    /**
     * Callback for RxObservables emitted by interactor
     * this callback is used if this is a new configuration or
     * if the configuration was stored in preferences.
     */
    private final class ConfigurationSubscriber extends Subscriber<Configuration> {

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {

            view.showError(e.getMessage());
        }

        @Override
        public void onNext(Configuration configuration) {

            view.navigateToMainActivity(configuration);
        }
    }
}
