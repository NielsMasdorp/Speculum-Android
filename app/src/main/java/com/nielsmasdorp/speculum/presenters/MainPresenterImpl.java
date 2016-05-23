package com.nielsmasdorp.speculum.presenters;

import com.nielsmasdorp.speculum.R;
import com.nielsmasdorp.speculum.activity.MainActivity;
import com.nielsmasdorp.speculum.interactor.MainInteractor;
import com.nielsmasdorp.speculum.models.Configuration;
import com.nielsmasdorp.speculum.models.CurrentWeather;
import com.nielsmasdorp.speculum.models.RedditPost;
import com.nielsmasdorp.speculum.models.YoMommaJoke;
import com.nielsmasdorp.speculum.util.Constants;
import com.nielsmasdorp.speculum.views.MainView;

import java.io.File;
import java.io.IOException;

import rx.Subscriber;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class MainPresenterImpl implements MainPresenter {

    private MainView view;
    private MainInteractor interactor;
    private Configuration configuration;

    public MainPresenterImpl(MainView view, MainInteractor interactor) {

        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void setConfiguration(Configuration configuration) {

        this.configuration = configuration;
    }

    /**
     * Start the UI, interactor will query all API's.
     * @param hasAccessToCalendar do we have calendar permissions?
     */
    @Override
    public void start(boolean hasAccessToCalendar) {

        if (null != configuration) {

            interactor.loadWeather(configuration.getLocation(), configuration.isCelsius(), configuration.getPollingDelay(), ((MainActivity) view).getString(R.string.forecast_api_key), new WeatherSubscriber());

            if (configuration.isVoiceCommands()) {
                interactor.setupRecognitionService(new AssetSubscriber());
            }
            if (!configuration.isSimpleLayout()) {
                interactor.loadTopRedditPost(configuration.getSubreddit(), configuration.getPollingDelay(), new RedditSubscriber());

                if (hasAccessToCalendar) {
                    interactor.loadLatestCalendarEvent(configuration.getPollingDelay(), new CalendarEventSubscriber());
                }
            }
        }
    }

    /**
     * Process a voice command picked up by the speech recognition service
     * @param command the command
     */
    @Override
    public void processVoiceCommand(String command) {

        switch (command) {
            case Constants.KEYPHRASE:
                // wake up and listen for commands
                view.talk(Constants.WAKE_NOTIFICATION);
                view.setListeningMode(Constants.COMMANDS_SEARCH);
                break;
            case Constants.SLEEP_PHRASE:
                // go to sleep
                view.talk(Constants.SLEEP_NOTIFICATION);
                view.setListeningMode(Constants.KWS_SEARCH);
                break;
            case Constants.UPDATE_PHRASE:
                // update data
                view.talk(Constants.UPDATE_NOTIFICATION);
                forceRefresh();
                // go to sleep again and wait for activation phrase
                view.setListeningMode(Constants.KWS_SEARCH);
                break;
            case Constants.JOKE_PHRASE:
                interactor.loadYoMommaJoke(new YoMammaJokeSubscriber());
                // go to sleep again and wait for activation phrase
                view.setListeningMode(Constants.KWS_SEARCH);
                break;
            default:
                break;
        }
    }

    /**
     * Force refresh the interactor to get new data
     */
    private void forceRefresh() {

        interactor.unSubscribe();
        start(true);
    }

    /**
     * Show error in view.
     * @param error message
     */
    @Override
    public void showError(String error) {

        view.showError(error);
    }

    /**
     * Display the latest calendar event.
     * @param event to display
     */
    @Override
    public void displayLatestCalendarEvent(String event) {

        view.displayLatestCalendarEvent(event);
    }

    /**
     * Display current weather
     * @param weather to display
     */
    @Override
    public void displayCurrentWeather(CurrentWeather weather) {

        view.displayCurrentWeather(configuration, weather);
    }

    /**
     * Display top reddit post.
     * @param redditPost to display
     */
    @Override
    public void displayTopRedditPost(RedditPost redditPost) {

        view.displayTopRedditPost(redditPost);
    }

    /**
     * Set listening mode in the speech recognition service
     * @param mode the mode to set
     */
    @Override
    public void setListeningMode(String mode) {

        view.setListeningMode(mode);
    }

    /**
     * Setup the speech recognition service.
     * @param assetDir the asset directory
     * @throws IOException
     */
    @Override
    public void setupRecognizer(File assetDir) throws IOException {

        view.setupRecognizer(assetDir);
    }

    /**
     * Give speech feedback to user.
     * @param sentence to speak
     */
    @Override
    public void talk(String sentence) {

        view.talk(sentence);
    }

    /**
     * Stop the interactor.
     */
    @Override
    public void finish() {

        interactor.unSubscribe();
    }

    /**
     * Callback for RxObservables emitted by interactor,
     * this callback is used for the weather observable.
     */
    private final class WeatherSubscriber extends Subscriber<CurrentWeather> {

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {

            view.showError(e.getMessage());
        }

        @Override
        public void onNext(CurrentWeather currentWeather) {

            view.displayCurrentWeather(configuration, currentWeather);
        }
    }

    /**
     * Callback for RxObservables emitted by interactor,
     * this callback is used for the reddit observable.
     */
    private final class RedditSubscriber extends Subscriber<RedditPost> {

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {

            view.showError(e.getMessage());
        }

        @Override
        public void onNext(RedditPost redditPost) {

            view.displayTopRedditPost(redditPost);
        }
    }

    /**
     * Callback for RxObservables emitted by interactor,
     * this callback is used for the calendar observable.
     */
    private final class CalendarEventSubscriber extends Subscriber<String> {

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {

            view.showError(e.getMessage());
        }

        @Override
        public void onNext(String event) {

            view.displayLatestCalendarEvent(event);
        }
    }

    /**
     * Callback for RxObservables emitted by interactor,
     * this callback is used for the joke observable.
     */
    private final class YoMammaJokeSubscriber extends Subscriber<YoMommaJoke> {

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {

            view.showError(e.getMessage());
        }

        @Override
        public void onNext(YoMommaJoke joke) {

            view.talk(joke.getJoke());
        }
    }

    /**
     * Callback for RxObservables emitted by interactor,
     * this callback is used for the asset observable.
     */
    private final class AssetSubscriber extends Subscriber<File> {

        @Override
        public void onCompleted() {

            view.setListeningMode(Constants.KWS_SEARCH);
        }

        @Override
        public void onError(Throwable e) {

            view.showError(e.getMessage());
        }

        @Override
        public void onNext(File assetDir) {
            try {
                view.setupRecognizer(assetDir);
            } catch (IOException e) {
                throw new RuntimeException("IOException: " + e.getLocalizedMessage());
            }
        }
    }
}
