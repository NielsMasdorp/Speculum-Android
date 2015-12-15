package com.nielsmasdorp.speculum.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.assent.Assent;
import com.nielsmasdorp.speculum.R;
import com.nielsmasdorp.speculum.models.reddit.RedditResponse;
import com.nielsmasdorp.speculum.models.yahoo_weather.CurrentWeatherConditions;
import com.nielsmasdorp.speculum.presenters.MainPresenter;
import com.nielsmasdorp.speculum.util.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements IMainView, View.OnSystemUiVisibilityChangeListener {

    @Bind(R.id.ll_weather_layout)
    LinearLayout mWeatherLayout;

    @Bind(R.id.tv_weather_title)
    TextView mWeatherTitle;

    @Bind(R.id.tv_weather_condition)
    TextView mWeatherCondition;

    @Bind(R.id.tv_weather_astronomy)
    TextView mWeatherAstronomy;

    @Bind(R.id.tv_weather_atmosphere)
    TextView mWeatherAtmosphere;

    @Bind(R.id.tv_weather_wind)
    TextView mWeatherWind;

    @Bind(R.id.tv_next_event)
    TextView mNextEvent;

    @Bind(R.id.pb_loading_spinner)
    ProgressBar mProgressLoading;

    MainPresenter mMainPresenter;

    private View mDecorView;

    private boolean mShowSun, mShowAtmosphere, mShowWind, mCelsius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Assent.setActivity(this, this);

        mDecorView = getWindow().getDecorView();
        hideSystemUI();

        Intent intent = getIntent();
        String location = intent.getExtras().getString(Constants.LOCATION_IDENTIFIER);
        String subreddit = intent.getExtras().getString(Constants.SUBREDDIT_IDENTIFIER);
        mShowAtmosphere = intent.getExtras().getBoolean(Constants.ATMOSPHERE_IDENTIFIER);
        mShowSun = intent.getExtras().getBoolean(Constants.SUN_IDENTIFIER);
        mShowWind = intent.getExtras().getBoolean(Constants.WIND_IDENTIFIER);
        mCelsius = intent.getExtras().getBoolean(Constants.CELSIUS_IDENTIFIER);

        mMainPresenter = new MainPresenter(this);
        mMainPresenter.loadWeather(location, mCelsius);
        mMainPresenter.loadTopRedditPost(subreddit);

        if (Assent.isPermissionGranted(Assent.READ_CALENDAR)) {
            mMainPresenter.loadLatestCalendarEvent();
        }

        mDecorView.setOnSystemUiVisibilityChangeListener(this);
    }

    private void hideSystemUI() {
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
    }

    @Override
    public void displayCurrentWeather(CurrentWeatherConditions currentConditions) {
        
        //TODO proper string formatting

        String distance = mCelsius ? Constants.DISTANCE_METRIC : Constants.DISTANCE_IMPERIAL;
        String pressure = mCelsius ? Constants.PRESSURE_METRIC : Constants.PRESSURE_IMPERIAL;
        String speed = mCelsius ? Constants.SPEED_METRIC : Constants.SPEED_IMPERIAL;
        String temperature = mCelsius ? Constants.TEMPERATURE_METRIC : Constants.TEMPERATURE_IMPERIAL;

        this.mWeatherTitle.setText(currentConditions.query.results.channel.item.title);

        this.mWeatherCondition.setText(currentConditions.query.results.channel.item.condition.temp + "º" + temperature + ", " +
                currentConditions.query.results.channel.item.condition.text);
        if (mShowAtmosphere) {
            this.mWeatherAtmosphere.setText(getString(R.string.humidity) + " : " + currentConditions.query.results.channel.atmosphere.humidity + "%, " +
                    getString(R.string.pressure) + ": " +
                    currentConditions.query.results.channel.atmosphere.pressure + pressure + ", " + getString(R.string.visibility) + ": " +
                    currentConditions.query.results.channel.atmosphere.visibility + distance);
        }
        if (mShowSun) {
            this.mWeatherAstronomy.setText(getString(R.string.sunrise) + ": " + currentConditions.query.results.channel.astronomy.sunrise +
                    ", " + getString(R.string.sunset) + ": " +
                    currentConditions.query.results.channel.astronomy.sunset);
        }

        if (mShowWind) {
            this.mWeatherWind.setText(getString(R.string.wind_temp) + ": " + currentConditions.query.results.channel.wind.chill +
                    "º" + temperature + ", " + getString(R.string.wind_speed) + ": " +
                    currentConditions.query.results.channel.wind.speed + speed);
        }

        setProgressBarVisibility(View.GONE);
        this.mWeatherLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayTopRedditPost(RedditResponse redditResponse) {
        Toast.makeText(this, redditResponse.data.children.get(0).data.title, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayLatestCalendarEvent(String event) {
        this.mNextEvent.setText("Next event: " + event);
    }

    @Override
    public void setProgressBarVisibility(int visibility) {

        if (this.mProgressLoading.getVisibility() != visibility) {
            this.mProgressLoading.setVisibility(visibility);
        }
    }

    @Override
    public void onError(String message) {

        setProgressBarVisibility(View.GONE);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Updates the activity every time the Activity becomes visible again
        Assent.setActivity(this, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Cleans up references of the Activity to avoid memory leaks
        if (isFinishing())
            Assent.setActivity(this, null);
    }

    @Override
    public void onSystemUiVisibilityChange(int visibility) {
        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
            hideSystemUI();
        }
    }
}
