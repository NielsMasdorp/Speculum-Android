package com.nielsmasdorp.speculum.presenters;

import android.nfc.Tag;
import android.util.Log;

import com.nielsmasdorp.speculum.models.CurrentWeather;
import com.nielsmasdorp.speculum.models.RedditPost;
import com.nielsmasdorp.speculum.models.YoMommaJoke;
import com.nielsmasdorp.speculum.services.GoogleCalendarService;
import com.nielsmasdorp.speculum.services.RedditService;
import com.nielsmasdorp.speculum.services.SharedPreferenceService;
import com.nielsmasdorp.speculum.services.YahooService;
import com.nielsmasdorp.speculum.services.YoMommaService;
import com.nielsmasdorp.speculum.util.Constants;
import com.nielsmasdorp.speculum.views.IMainView;
import com.nielsmasdorp.speculum.views.MainActivity;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import edu.cmu.pocketsphinx.Assets;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class MainPresenterImpl implements IMainPresenter {

    private static final String TAG = MainPresenterImpl.class.getSimpleName();

    private YahooService mYahooService;
    private GoogleCalendarService mGoogleCalendarService;
    private RedditService mRedditService;
    private YoMommaService mYomommaService;

    private WeakReference<IMainView> mMainView;

    private CompositeSubscription mCompositeSubscription;

    public MainPresenterImpl(IMainView view) {

        mMainView = new WeakReference<>(view);
        mYahooService = new YahooService();
        mRedditService = new RedditService();
        mYomommaService = new YoMommaService();
        mGoogleCalendarService = new GoogleCalendarService((MainActivity) mMainView.get());
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void loadLatestCalendarEvent(int updateDelay) {

        mCompositeSubscription.add(Observable.interval(0, updateDelay, TimeUnit.MINUTES)
                .flatMap(ignore -> mGoogleCalendarService.getLatestCalendarEvent())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mMainView.get() != null)
                            mMainView.get().showError(e.getLocalizedMessage());
                        Log.d(TAG, "CalendarSubscription", e);
                    }

                    @Override
                    public void onNext(String event) {

                        if (mMainView.get() != null)
                            mMainView.get().displayLatestCalendarEvent(event);
                    }
                }));
    }

    @Override
    public void loadWeather(final String location, boolean celsius, int updateDelay) {

        final String query = celsius ? Constants.WEATHER_QUERY_SECOND_CELSIUS : Constants.WEATHER_QUERY_SECOND_FAHRENHEIT;

        mCompositeSubscription.add(Observable.interval(0, updateDelay, TimeUnit.MINUTES)
                .flatMap(ignore -> mYahooService.getApi().getCurrentWeatherConditions(Constants.WEATHER_QUERY_FIRST +
                        location + query, Constants.YAHOO_QUERY_FORMAT))
                .flatMap(mYahooService::getCurrentWeather)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<CurrentWeather>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mMainView.get() != null)
                            mMainView.get().showError(e.getLocalizedMessage());
                        Log.d(TAG, "WeatherSubscription", e);
                    }

                    @Override
                    public void onNext(CurrentWeather weather) {

                        if (mMainView.get() != null) {
                            mMainView.get().displayCurrentWeather(weather);
                        }
                    }
                }));
    }

    @Override
    public void loadTopRedditPost(final String subreddit, int updateDelay) {

        mCompositeSubscription.add(Observable.interval(0, updateDelay, TimeUnit.MINUTES)
                .flatMap(ignore -> mRedditService.getApi().getTopRedditPostForSubreddit(subreddit, Constants.REDDIT_LIMIT))
                .flatMap(mRedditService::getRedditPost)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<RedditPost>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mMainView.get() != null)
                            mMainView.get().showError(e.getLocalizedMessage());
                        Log.d(TAG, "RedditSubscription", e);
                    }

                    @Override
                    public void onNext(RedditPost redditPost) {

                        if (mMainView.get() != null)
                            mMainView.get().displayTopRedditPost(redditPost);
                    }
                }));
    }

    @Override
    public void loadJoke() {

        mYomommaService.getApi().getJoke()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<YoMommaJoke>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mMainView.get() != null)
                            mMainView.get().showError(e.getLocalizedMessage());
                        Log.d(TAG, "YoMommaJokeService", e);
                    }

                    @Override
                    public void onNext(YoMommaJoke yoMommaJoke) {
                        if (mMainView.get() != null)
                            mMainView.get().talk(yoMommaJoke.getJoke());
                    }
                });
    }

    @Override
    public void setupRecognitionService() {

        if (mMainView.get() != null) {

            prepareAssetsForRecognizer()
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Void>() {
                        @Override
                        public void onCompleted() {
                            mMainView.get().setListeningMode(Constants.KWS_SEARCH);
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (mMainView.get() != null)
                                mMainView.get().showError(e.getLocalizedMessage());
                            Log.e(TAG, "RecognitionService: ", e);
                        }

                        @Override
                        public void onNext(Void aVoid) {
                        }
                    });
        }
    }

    @Override
    public void processCommand(String command) {

        if (mMainView.get() != null) {

            switch (command) {
                case Constants.KEYPHRASE:
                    // wake up and listen for commands
                    mMainView.get().talk(Constants.WAKE_NOTIFICATION);
                    mMainView.get().setListeningMode(Constants.COMMANDS_SEARCH);
                    break;
                case Constants.SLEEP_PHRASE:
                    // go to sleep
                    mMainView.get().talk(Constants.SLEEP_NOTIFICATION);
                    mMainView.get().setListeningMode(Constants.KWS_SEARCH);
                    break;
                case Constants.UPDATE_PHRASE:
                    updatePhrase();
                    break;
                case Constants.NEWS_PHRASE:
                    //TODO implement news API
                    // go to sleep again and wait for activation phrase
                    mMainView.get().setListeningMode(Constants.KWS_SEARCH);
                    break;
                case Constants.JOKE_PHRASE:
                    loadJoke();
                    // go to sleep again and wait for activation phrase
                    mMainView.get().setListeningMode(Constants.KWS_SEARCH);
                    break;
                default:
                    break;

            }

        }
    }

    private void updatePhrase() {
        // update data
        mMainView.get().talk(Constants.UPDATE_NOTIFICATION);
        unSubscribe();
        mMainView.get().startPolling();
        // go to sleep again and wait for activation phrase
        mMainView.get().setListeningMode(Constants.KWS_SEARCH);
    }

    private Observable<Void> prepareAssetsForRecognizer() {
        return Observable.defer(() -> {
            try {
                Assets assets = new Assets((MainActivity) mMainView.get());
                File assetDir = assets.syncAssets();
                mMainView.get().setupRecognizer(assetDir);
            } catch (IOException e) {
                throw new RuntimeException("IOException: " + e.getLocalizedMessage());
            }
            return Observable.empty();
        });
    }

    @Override
    public void unSubscribe() {
        mCompositeSubscription.unsubscribe();
        mCompositeSubscription = new CompositeSubscription();
    }
}
