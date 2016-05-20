package com.nielsmasdorp.speculum.views;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.assent.Assent;
import com.nielsmasdorp.speculum.R;
import com.nielsmasdorp.speculum.models.Configuration;
import com.nielsmasdorp.speculum.models.CurrentWeather;
import com.nielsmasdorp.speculum.models.RedditPost;
import com.nielsmasdorp.speculum.models.forecast.DayForecast;
import com.nielsmasdorp.speculum.presenters.IMainPresenter;
import com.nielsmasdorp.speculum.presenters.MainPresenterImpl;
import com.nielsmasdorp.speculum.util.Constants;
import com.nielsmasdorp.speculum.util.WeatherIconGenerator;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;

import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class MainActivity extends AppCompatActivity implements IMainView, View.OnSystemUiVisibilityChangeListener, RecognitionListener, TextToSpeech.OnInitListener {

    @BindView(R.id.iv_current_weather)
    ImageView mWeatherCondition;
    @BindView(R.id.tv_current_temp)
    TextView mWeatherTemp;
    @BindView(R.id.weather_layout)
    LinearLayout mWeatherLayout;
    @BindView(R.id.tv_last_updated)
    TextView mWeatherLastUpdated;
    @Nullable
    @BindView(R.id.tv_summary)
    TextView mWeatherSummary;
    @Nullable
    @BindView(R.id.weather_stats_layout)
    LinearLayout mWeatherStatsLayout;
    @Nullable
    @BindView(R.id.calendar_layout)
    LinearLayout mCalendarLayout;
    @Nullable
    @BindView(R.id.reddit_layout)
    RelativeLayout mRedditLayout;
    @Nullable
    @BindView(R.id.iv_forecast_weather1)
    ImageView mDayOneCondition;
    @Nullable
    @BindView(R.id.tv_forecast_temp1)
    TextView mDayOneTemp;
    @Nullable
    @BindView(R.id.tv_forecast_date1)
    TextView mDayOneDate;
    @Nullable
    @BindView(R.id.iv_forecast_weather2)
    ImageView mDayTwoCondition;
    @Nullable
    @BindView(R.id.tv_forecast_temp2)
    TextView mDayTwoTemp;
    @Nullable
    @BindView(R.id.tv_forecast_date2)
    TextView mDayTwoDate;
    @Nullable
    @BindView(R.id.iv_forecast_weather3)
    ImageView mDayThreeCondition;
    @Nullable
    @BindView(R.id.tv_forecast_temp3)
    TextView mDayThreeTemp;
    @Nullable
    @BindView(R.id.tv_forecast_date3)
    TextView mDayThreeDate;
    @Nullable
    @BindView(R.id.iv_forecast_weather4)
    ImageView mDayFourCondition;
    @Nullable
    @BindView(R.id.tv_forecast_temp4)
    TextView mDayFourTemp;
    @Nullable
    @BindView(R.id.tv_forecast_date4)
    TextView mDayFourDate;
    @Nullable
    @BindView(R.id.tv_stats_wind)
    TextView mWeatherWind;
    @Nullable
    @BindView(R.id.tv_stats_humidity)
    TextView mWeatherHumidity;
    @Nullable
    @BindView(R.id.tv_stats_pressure)
    TextView mWeatherPressure;
    @Nullable
    @BindView(R.id.tv_stats_visibility)
    TextView mWeatherVisibility;
    @Nullable
    @BindView(R.id.tv_calendar_event)
    TextView mCalendarEvent;
    @Nullable
    @BindView(R.id.tv_reddit_post_title)
    TextView mRedditPostTitle;
    @Nullable
    @BindView(R.id.tv_reddit_post_votes)
    TextView mRedditPostVotes;

    IMainPresenter mMainPresenter;
    View mDecorView;
    Configuration mConfiguration;
    WeatherIconGenerator mIconGenerator;

    SpeechRecognizer recognizer;
    TextToSpeech mTts;

    ViewStub view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Assent.setActivity(this, this);

        //get configuration from Intent
        mConfiguration = (Configuration) getIntent().getSerializableExtra(Constants.CONFIGURATION_IDENTIFIER);
        boolean didLoadOldConfig = getIntent().getBooleanExtra(Constants.SAVED_CONFIGURATION_IDENTIFIER, false);

        if (mConfiguration.isSimpleLayout()) {
            view = (ViewStub) findViewById(R.id.stub_simple);
        } else {
            view = (ViewStub) findViewById(R.id.stub_verbose);
        }
        if (null != view) view.inflate();

        ButterKnife.bind(this);

        //never sleep
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mMainPresenter = new MainPresenterImpl(this);
        mIconGenerator = WeatherIconGenerator.getInstance();

        if (didLoadOldConfig)
            showConfigurationSnackbar();
    }

    private void showConfigurationSnackbar() {
        Snackbar snackbar = Snackbar
                .make(mWeatherLayout, getString(R.string.old_config_found_snackbar), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.old_config_found_snackbar_back), view -> {
                    onBackPressed();
                });

        snackbar.show();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void hideSystemUI() {

        mDecorView = getWindow().getDecorView();

        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        mDecorView.setOnSystemUiVisibilityChangeListener(this);
    }

    @Override
    @SuppressWarnings("all")
    public void displayCurrentWeather(CurrentWeather weather) {

        // Determine if user requested metric or imperial values and adjust units accordingly
        boolean metric = mConfiguration.isCelsius();
        String distance = metric ? Constants.DISTANCE_METRIC : Constants.DISTANCE_IMPERIAL;
        String pressure = metric ? Constants.PRESSURE_METRIC : Constants.PRESSURE_IMPERIAL;
        String speed = metric ? Constants.SPEED_METRIC : Constants.SPEED_IMPERIAL;
        String temperature = metric ? Constants.TEMPERATURE_METRIC : Constants.TEMPERATURE_IMPERIAL;

        this.mWeatherCondition.setImageResource(mIconGenerator.getIcon(weather.getIcon()));
        this.mWeatherTemp.setText(weather.getTemperature() + "º" + temperature);
        this.mWeatherLastUpdated.setText(getString(R.string.last_updated) + getLastUpdated(weather.getLastUpdated()));

        if (!mConfiguration.isSimpleLayout()) {
            this.mWeatherWind.setText(weather.getWindSpeed() + speed + " " + weather.getWindDirection() + " | " + weather.getWindTemperature() + "º" + temperature);
            this.mWeatherHumidity.setText(weather.getHumidity() + "%");
            this.mWeatherPressure.setText(weather.getPressure() + pressure);
            this.mWeatherVisibility.setText(weather.getVisibility() + distance);

            List<DayForecast> forecast = weather.getForecast();

            SimpleDateFormat formatter = new SimpleDateFormat(metric ? "d MMM" : "MMM d", Locale.getDefault());

            this.mDayOneDate.setText(formatter.format(new Date((long) forecast.get(1).getTime() * 1000)));
            this.mDayOneTemp.setText((forecast.get(1).getTemperatureMin().intValue() + forecast.get(1).getTemperatureMax().intValue()) / 2 + "º" + temperature);
            this.mDayOneCondition.setImageResource(mIconGenerator.getIcon(forecast.get(0).getIcon()));
            this.mDayTwoDate.setText(formatter.format(new Date((long) forecast.get(2).getTime() * 1000)));
            this.mDayTwoTemp.setText((forecast.get(2).getTemperatureMin().intValue() + forecast.get(2).getTemperatureMax().intValue()) / 2 + "º" + temperature);
            this.mDayTwoCondition.setImageResource(mIconGenerator.getIcon(forecast.get(2).getIcon()));
            this.mDayThreeDate.setText(formatter.format(new Date((long) forecast.get(3).getTime() * 1000)));
            this.mDayThreeTemp.setText((forecast.get(3).getTemperatureMin().intValue() + forecast.get(3).getTemperatureMax().intValue()) / 2 + "º" + temperature);
            this.mDayThreeCondition.setImageResource(mIconGenerator.getIcon(forecast.get(3).getIcon()));
            this.mDayFourDate.setText(formatter.format(new Date((long) forecast.get(4).getTime() * 1000)));
            this.mDayFourTemp.setText((forecast.get(4).getTemperatureMin().intValue() + forecast.get(4).getTemperatureMax().intValue()) / 2 + "º" + temperature);
            this.mDayFourCondition.setImageResource(mIconGenerator.getIcon(forecast.get(4).getIcon()));
        } else {
            this.mWeatherSummary.setText(weather.getSummary());
        }

        showContent(0);
    }

    private String getLastUpdated(Date lastUpdated) {

        SimpleDateFormat formatter = new SimpleDateFormat("h:mm", Locale.getDefault());

        return " " + formatter.format(lastUpdated);
    }

    @Override
    @SuppressWarnings("all")
    public void displayTopRedditPost(RedditPost redditPost) {
        mRedditPostTitle.setText(redditPost.getTitle());
        mRedditPostVotes.setText(redditPost.getUps() + "");
        showContent(1);
    }

    @Override
    @SuppressWarnings("all")
    public void displayLatestCalendarEvent(String event) {
        this.mCalendarEvent.setText(event);
        showContent(2);
    }

    @Override
    @SuppressWarnings("all")
    public void showContent(int which) {
        switch (which) {
            case 0:
                if (this.mWeatherLayout.getVisibility() != View.VISIBLE) {
                    this.mWeatherLayout.setVisibility(View.VISIBLE);
                    this.mWeatherStatsLayout.setVisibility(View.VISIBLE);
                }
                break;
            case 1:
                if (this.mRedditLayout.getVisibility() != View.VISIBLE) {
                    this.mRedditLayout.setVisibility(View.VISIBLE);
                }
                break;
            case 2:
                if (this.mCalendarLayout.getVisibility() != View.VISIBLE) {
                    this.mCalendarLayout.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void showError(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        hideSystemUI();

        //Start polling
        startPolling();

        if (mConfiguration.getVoiceCommands()) {
            //start listening
            mMainPresenter.setupRecognitionService();
            //init TTS Service
            mTts = new TextToSpeech(this, this);
        }

        // Updates the activity every time the Activity becomes visible again
        Assent.setActivity(this, this);
    }

    @Override
    public void startPolling() {

        mMainPresenter.loadWeather(mConfiguration.getLocation(), mConfiguration.isCelsius(), mConfiguration.getPollingDelay(), getString(R.string.forecast_api_key));

        if (!mConfiguration.isSimpleLayout()) {
            mMainPresenter.loadTopRedditPost(mConfiguration.getSubreddit(), mConfiguration.getPollingDelay());

            if (Assent.isPermissionGranted(Assent.READ_CALENDAR)) {
                mMainPresenter.loadLatestCalendarEvent(mConfiguration.getPollingDelay());
            }
        }
    }

    @Override
    public void setListeningMode(String mode) {

        recognizer.stop();
        if (mode.equals(Constants.KWS_SEARCH)) {
            recognizer.startListening(mode);
        } else {
            recognizer.startListening(mode, 5000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        //stop polling
        mMainPresenter.unSubscribe();

        if (mConfiguration.getVoiceCommands()) {
            //shutdown recognition service
            if (recognizer != null) {
                recognizer.cancel();
                recognizer.shutdown();
            }

            //close the TTS Engine
            if (mTts != null) {
                mTts.stop();
                mTts.shutdown();
            }
        }

        if (isFinishing())
            Assent.setActivity(this, null);
    }

    @Override
    public void onSystemUiVisibilityChange(int visibility) {

        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
            hideSystemUI();
        }
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null)
            return;

        String command = hypothesis.getHypstr();
        mMainPresenter.processCommand(command);
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
        talk(Constants.SLEEP_NOTIFICATION);
        setListeningMode(Constants.KWS_SEARCH);
    }

    @Override
    public void onBeginningOfSpeech() {
    }

    @Override
    public void onEndOfSpeech() {
    }

    @Override
    public void setupRecognizer(File assetsDir) throws IOException {

        recognizer = defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))
                .setKeywordThreshold(1e-45f)
                .getRecognizer();
        recognizer.addListener(this);

        // the activation keyword
        recognizer.addKeyphraseSearch(Constants.KWS_SEARCH, Constants.KEYPHRASE);

        // Create grammar-based search for command recognition
        File commands = new File(assetsDir, "commands.gram");
        recognizer.addKeywordSearch(Constants.COMMANDS_SEARCH, commands);
    }

    /**
     * TTS onInit
     *
     * @param status
     */
    @Override
    public void onInit(int status) {
    }

    /**
     * Use the TTS engine to speak a message to the user
     *
     * @param message to speak
     */
    @Override
    public void talk(String message) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ttsGreater21(message);
        } else {
            ttsUnder20(message);
        }
    }

    /**
     * Respective methods for TTS on pre SK 20 and Lollipop
     *
     * @param text
     */
    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        mTts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text) {
        String utteranceId = this.hashCode() + "";
        mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }
}
