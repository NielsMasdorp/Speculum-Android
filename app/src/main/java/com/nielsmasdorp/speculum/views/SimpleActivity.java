package com.nielsmasdorp.speculum.views;

/**
 * Created by Gijs on 22-2-2016.
 */

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.assent.Assent;
import com.nielsmasdorp.speculum.R;
import com.nielsmasdorp.speculum.models.Configuration;
import com.nielsmasdorp.speculum.models.CurrentWeather;
import com.nielsmasdorp.speculum.presenters.SimplePresenterImpl;
import com.nielsmasdorp.speculum.util.Constants;
import com.nielsmasdorp.speculum.util.WeatherIconGenerator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;

import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

public class SimpleActivity extends AppCompatActivity implements ISimpleView, View.OnSystemUiVisibilityChangeListener, RecognitionListener, TextToSpeech.OnInitListener {

    @Bind(R.id.weather_layout)
    LinearLayout mWeatherLayout;

    @Bind(R.id.iv_current_weather)
    ImageView mWeatherCondition;

    @Bind(R.id.tv_current_temp)
    TextView mWeatherTemp;


    SimplePresenterImpl mSimplePresenter;
    View mDecorView;
    Configuration mConfiguration;
    WeatherIconGenerator mIconGenerator;

    SpeechRecognizer recognizer;
    TextToSpeech mTts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        ButterKnife.bind(this);
        Assent.setActivity(this, this);

        //never sleep
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //get configuration from Intent
        mConfiguration = (Configuration) getIntent().getSerializableExtra(Constants.CONFIGURATION_IDENTIFIER);
        boolean didLoadOldConfig = getIntent().getBooleanExtra(Constants.SAVED_CONFIGURATION_IDENTIFIER, false);

        mSimplePresenter = new SimplePresenterImpl(this);
        mIconGenerator = WeatherIconGenerator.getInstance();

        if (didLoadOldConfig)
            showConfigurationSnackbar();
    }

    private void showConfigurationSnackbar() {
        Snackbar snackbar = Snackbar
                .make(mWeatherLayout, getString(R.string.old_config_found_snackbar), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.old_config_found_snackbar_back), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });

        snackbar.show();
    }

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
    public void displayCurrentWeather(CurrentWeather weather) {

        // Determine if user requested metric or imperial values and adjust units accordingly
        boolean metric = mConfiguration.isCelsius();
        String temperature = metric ? Constants.TEMPERATURE_METRIC : Constants.TEMPERATURE_IMPERIAL;

        this.mWeatherCondition.setImageResource(mIconGenerator.getIcon(Integer.parseInt(weather.getStatusCode())));
        this.mWeatherTemp.setText(weather.getTemperature() + "º" + temperature);

        showContent(0);
    }


    @Override
    public void showContent(int which) {
        if (this.mWeatherLayout.getVisibility() != View.VISIBLE) {
            this.mWeatherLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onError(String message) {
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
            mSimplePresenter.setupRecognitionService();
            //init TTS Service
            mTts = new TextToSpeech(this, this);
        }

        // Updates the activity every time the Activity becomes visible again
        Assent.setActivity(this, this);
    }

    @Override
    public void startPolling() {
        mSimplePresenter.loadWeather(mConfiguration.getLocation(), mConfiguration.isCelsius(), mConfiguration.getPollingDelay());
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
        mSimplePresenter.unSubscribe();

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
        mSimplePresenter.processCommand(command);
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
    }

    @Override
    public void onError(Exception e) {
        onError(e);
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
