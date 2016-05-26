package com.nielsmasdorp.speculum.presenters;

import android.app.Application;
import android.os.Build;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.nielsmasdorp.speculum.R;
import com.nielsmasdorp.speculum.activity.MainActivity;
import com.nielsmasdorp.speculum.interactor.MainInteractor;
import com.nielsmasdorp.speculum.models.Configuration;
import com.nielsmasdorp.speculum.models.RedditPost;
import com.nielsmasdorp.speculum.models.Weather;
import com.nielsmasdorp.speculum.models.YoMommaJoke;
import com.nielsmasdorp.speculum.util.Constants;
import com.nielsmasdorp.speculum.views.MainView;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class MainPresenterImpl implements MainPresenter, RecognitionListener, TextToSpeech.OnInitListener {

    private MainView view;
    private MainInteractor interactor;
    private Application application;
    private Configuration configuration;
    private SpeechRecognizer recognizer;
    private TextToSpeech textToSpeech;

    public MainPresenterImpl(MainView view, MainInteractor interactor, Application application) {

        this.view = view;
        this.interactor = interactor;
        this.application = application;
    }

    /*
    Begin presenter methods
     */
    @Override
    public void setConfiguration(Configuration configuration) {

        this.configuration = configuration;
    }

    @Override
    public void start(boolean hasAccessToCalendar) {
        if (null != configuration) {
            startWeather();
            if (configuration.isVoiceCommands()) {
                initSpeechRecognitionService();
                setupTts();
            }
            if (!configuration.isSimpleLayout()) {
                startReddit();
                if (hasAccessToCalendar) {
                    startCalendar();
                }
            }
        }
    }

    private void updateData() {
        interactor.unSubscribe();
        if (null != configuration) {
            startWeather();
            if (!configuration.isSimpleLayout()) {
                startReddit();
                startCalendar();
            }
        }
    }

    @Override
    public void showError(String error) {
        view.showError(error);
    }

    @Override
    public void finish() {
        tearDownSpeechService()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        interactor.unSubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(Void aVoid) {

                    }
                });
    }

    private Observable<Void> tearDownSpeechService() {
        return Observable.defer(() -> {
            if (recognizer != null) {
                recognizer.cancel();
                recognizer.shutdown();
            }
            if (textToSpeech != null) {
                textToSpeech.stop();
                textToSpeech.shutdown();
            }
            return Observable.empty();
        });
    }

    /*
    End presenter methods
     */

    /*
    Begin start background data methods
     */
    private void startWeather() {
        interactor.loadWeather(configuration.getLocation(), configuration.isCelsius(), configuration.getPollingDelay(), ((MainActivity) view).getString(R.string.forecast_api_key), new WeatherSubscriber());
    }

    private void startReddit() {
        interactor.loadTopRedditPost(configuration.getSubreddit(), configuration.getPollingDelay(), new RedditSubscriber());
    }

    private void startCalendar() {
        interactor.loadLatestCalendarEvent(configuration.getPollingDelay(), new CalendarEventSubscriber());
    }
    /*
    End start background data methods
     */

    /*
    Begin speech recognition related initialisation
     */
    private void initSpeechRecognitionService() {
        interactor.getAssetsDirForSpeechRecognizer(new AssetSubscriber());
    }

    private void setupTts() {
        initTts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(Void aVoid) {

                    }
                });
    }

    public void setupRecognizer(File assetDir) {
        initRecognizer(assetDir)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        setListeningMode(Constants.KWS_SEARCH);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(Void aVoid) {
                    }
                });
    }

    private Observable<Void> initTts() {
        return Observable.defer(() -> {
            textToSpeech = new TextToSpeech(application, this);
            return Observable.empty();
        });
    }

    private Observable<Void> initRecognizer(File assetDir) {
        return Observable.defer(() -> {
            try {
                recognizer = defaultSetup()
                        .setAcousticModel(new File(assetDir, "en-us-ptm"))
                        .setDictionary(new File(assetDir, "cmudict-en-us.dict"))
                        .setKeywordThreshold(1e-45f)
                        .getRecognizer();
                recognizer.addListener(this);
                recognizer.addKeyphraseSearch(Constants.KWS_SEARCH, Constants.KEYPHRASE);
                recognizer.addKeywordSearch(Constants.COMMANDS_SEARCH, new File(assetDir, "commands.gram"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Observable.empty();
        });
    }
    /*
     End speech recognition related initialisation
     */


    /*
    Begin speech recognition logic methods
     */
    public void setListeningMode(String mode) {
        recognizer.stop();
        if (mode.equals(Constants.KWS_SEARCH)) {
            recognizer.startListening(mode);
        } else {
            recognizer.startListening(mode, 5000);
        }
    }

    private void processVoiceCommand(String command) {
        switch (command) {
            case Constants.KEYPHRASE:
                // wake up and listen for commands
                speak(Constants.WAKE_NOTIFICATION);
                setListeningMode(Constants.COMMANDS_SEARCH);
                view.showListening();
                break;
            case Constants.SLEEP_PHRASE:
                // go to sleep
                speak(Constants.SLEEP_NOTIFICATION);
                setListeningMode(Constants.KWS_SEARCH);
                view.hideListening();
                break;
            case Constants.UPDATE_PHRASE:
                // update data
                speak(Constants.UPDATE_NOTIFICATION);
                updateData();
                // go to sleep again and wait for activation phrase
                setListeningMode(Constants.KWS_SEARCH);
                commandExecuting();
                break;
            case Constants.JOKE_PHRASE:
                interactor.loadYoMommaJoke(new YoMammaJokeSubscriber());
                // go to sleep again and wait for activation phrase
                setListeningMode(Constants.KWS_SEARCH);
                commandExecuting();
                break;
        }
    }

    private void commandExecuting() {

        notifyCommandExecuting()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {

                    @Override
                    public void onStart() {
                        view.showCommandExecuting();
                    }

                    @Override
                    public void onCompleted() {
                        view.hideListening();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(Void aVoid) {
                    }
                });
    }

    private Observable<Void> notifyCommandExecuting() {
        return Observable.defer(() -> {
            SystemClock.sleep(TimeUnit.SECONDS.toMillis(1));
            return Observable.empty();
        });
    }

    /*
    End speech recognition logic methods
     */

    /*
    Begin text to speech methods
     */
    @SuppressWarnings("deprecation")
    public void speak(String sentence) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String utteranceId = this.hashCode() + "";
            textToSpeech.speak(sentence, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        } else {
            HashMap<String, String> map = new HashMap<>();
            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
            textToSpeech.speak(sentence, TextToSpeech.QUEUE_FLUSH, map);
        }
    }
    /*
    End text to speech methods
     */


    /*
    Begin speech recognition logic methods
     */
    @Override
    public void onBeginningOfSpeech() {
    }

    @Override
    public void onEndOfSpeech() {
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null) return;
        String command = hypothesis.getHypstr();
        processVoiceCommand(command);
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
    }

    @Override
    public void onError(Exception e) {
        showError(e.getLocalizedMessage());
        Log.e(MainActivity.class.getSimpleName(), e.toString());
    }

    @Override
    public void onTimeout() {
        speak(Constants.SLEEP_NOTIFICATION);
        setListeningMode(Constants.KWS_SEARCH);
        view.hideListening();
    }
     /*
    End speech recognition logic methods
     */

    /*
   Begin tts lifecycle methods
    */
    @Override
    public void onInit(int status) {
    }
    /*
   End tts lifecycle methods
    */

    private final class WeatherSubscriber extends Subscriber<Weather> {

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            view.showError(e.getMessage());
        }

        @Override
        public void onNext(Weather weather) {
            view.displayCurrentWeather(weather, configuration.isSimpleLayout());
        }
    }

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
            speak(joke.getJoke());
        }
    }

    private final class AssetSubscriber extends Subscriber<File> {

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            view.showError(e.getMessage());
        }

        @Override
        public void onNext(File assetDir) {
            setupRecognizer(assetDir);
        }
    }
}
