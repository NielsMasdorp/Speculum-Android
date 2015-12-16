package com.nielsmasdorp.speculum.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.assent.Assent;
import com.nielsmasdorp.speculum.R;
import com.nielsmasdorp.speculum.models.reddit.RedditResponse;
import com.nielsmasdorp.speculum.models.yahoo_weather.CurrentWeatherConditions;
import com.nielsmasdorp.speculum.models.yahoo_weather.Forecast;
import com.nielsmasdorp.speculum.presenters.IMainPresenter;
import com.nielsmasdorp.speculum.presenters.MainPresenterImpl;
import com.nielsmasdorp.speculum.util.Constants;

import java.util.List;

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

    @Bind(R.id.tv_forecast_day_one_date)
    TextView mDayOneDate;

    @Bind(R.id.tv_forecast_day_one_condition)
    TextView mDayOneCondition;

    @Bind(R.id.tv_forecast_day_two_date)
    TextView mDayTwoDate;

    @Bind(R.id.tv_forecast_day_two_condition)
    TextView mDayTwoCondition;

    @Bind(R.id.tv_forecast_day_three_date)
    TextView mDayThreeDate;

    @Bind(R.id.tv_forecast_day_three_condition)
    TextView mDayThreeCondition;

    @Bind(R.id.tv_forecast_day_four_date)
    TextView mDayFourDate;

    @Bind(R.id.tv_forecast_day_four_condition)
    TextView mDayFourCondition;

    @Bind(R.id.tv_next_event)
    TextView mNextEvent;

    @Bind(R.id.pb_loading_spinner)
    ProgressBar mProgressLoading;

    IMainPresenter mMainPresenter;

    private View mDecorView;

    // Configuration variables
    private boolean mShowSun, mShowAtmosphere, mShowWind, mCelsius, mShowForecast;
    private String mLocation, mSubreddit;
    private int mPollingDelay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Assent.setActivity(this, this);

        mDecorView = getWindow().getDecorView();
        hideSystemUI();

        //never sleep
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent = getIntent();
        mLocation = intent.getExtras().getString(Constants.LOCATION_IDENTIFIER);
        mSubreddit = intent.getExtras().getString(Constants.SUBREDDIT_IDENTIFIER);
        mCelsius = intent.getExtras().getBoolean(Constants.CELSIUS_IDENTIFIER);
        mShowAtmosphere = intent.getExtras().getBoolean(Constants.ATMOSPHERE_IDENTIFIER);
        mShowSun = intent.getExtras().getBoolean(Constants.SUN_IDENTIFIER);
        mShowWind = intent.getExtras().getBoolean(Constants.WIND_IDENTIFIER);
        mShowForecast = intent.getExtras().getBoolean(Constants.FORECAST_IDENTIFIER);
        mPollingDelay = intent.getExtras().getInt(Constants.POLLING_IDENTIFIER);

        mDecorView.setOnSystemUiVisibilityChangeListener(this);
        mMainPresenter = new MainPresenterImpl(this);
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

        //TODO string formatting from strings.xml
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

        if (mShowForecast) {

            List<Forecast> forecast = currentConditions.query.results.channel.item.forecast;

            this.mDayOneDate.setText(forecast.get(0).date);
            this.mDayOneCondition.setText(forecast.get(0).text + " " + forecast.get(0).low + "/" + forecast.get(0).high + "º" + temperature);
            this.mDayTwoDate.setText(forecast.get(1).date);
            this.mDayTwoCondition.setText(forecast.get(1).text + " " + forecast.get(1).low + "/" + forecast.get(1).high + "º" + temperature);
            this.mDayThreeDate.setText(forecast.get(2).date);
            this.mDayThreeCondition.setText(forecast.get(2).text + " " + forecast.get(2).low + "/" + forecast.get(2).high + "º" + temperature);
            this.mDayFourDate.setText(forecast.get(3).date);
            this.mDayFourCondition.setText(forecast.get(3).text + " " + forecast.get(3).low + "/" + forecast.get(3).high + "º" + temperature);
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

        this.mNextEvent.setText(getString(R.string.next_event) + ": " + event);
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

        startPolling();

        // Updates the activity every time the Activity becomes visible again
        Assent.setActivity(this, this);
    }

    private void startPolling() {

        mMainPresenter.loadWeather(mLocation, mCelsius, mPollingDelay);
        mMainPresenter.loadTopRedditPost(mSubreddit, mPollingDelay);

        if (Assent.isPermissionGranted(Assent.READ_CALENDAR)) {
            mMainPresenter.loadLatestCalendarEvent(mPollingDelay);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        //stop polling
        mMainPresenter.unSubscribe();

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
