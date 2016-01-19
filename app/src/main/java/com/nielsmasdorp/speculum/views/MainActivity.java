package com.nielsmasdorp.speculum.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.assent.Assent;
import com.nielsmasdorp.speculum.R;
import com.nielsmasdorp.speculum.models.Configuration;
import com.nielsmasdorp.speculum.models.CurrentWeather;
import com.nielsmasdorp.speculum.models.RedditPost;
import com.nielsmasdorp.speculum.models.yahoo_weather.Forecast;
import com.nielsmasdorp.speculum.presenters.IMainPresenter;
import com.nielsmasdorp.speculum.presenters.MainPresenterImpl;
import com.nielsmasdorp.speculum.util.Constants;
import com.nielsmasdorp.speculum.util.WeatherIconGenerator;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class MainActivity extends AppCompatActivity implements IMainView, View.OnSystemUiVisibilityChangeListener {

    @Bind(R.id.weather_layout)
    LinearLayout mWeatherLayout;

    @Bind(R.id.weather_stats_layout)
    LinearLayout mWeatherStatsLayout;

    @Bind(R.id.calendar_layout)
    LinearLayout mCalendarLayout;

    @Bind(R.id.reddit_layout)
    RelativeLayout mRedditLayout;

    @Bind(R.id.iv_current_weather)
    ImageView mWeatherCondition;

    @Bind(R.id.tv_current_temp)
    TextView mWeatherTemp;

    @Bind(R.id.iv_forecast_weather1)
    ImageView mDayOneCondition;

    @Bind(R.id.tv_forecast_temp1)
    TextView mDayOneTemp;

    @Bind(R.id.tv_forecast_date1)
    TextView mDayOneDate;

    @Bind(R.id.iv_forecast_weather2)
    ImageView mDayTwoCondition;

    @Bind(R.id.tv_forecast_temp2)
    TextView mDayTwoTemp;

    @Bind(R.id.tv_forecast_date2)
    TextView mDayTwoDate;

    @Bind(R.id.iv_forecast_weather3)
    ImageView mDayThreeCondition;

    @Bind(R.id.tv_forecast_temp3)
    TextView mDayThreeTemp;

    @Bind(R.id.tv_forecast_date3)
    TextView mDayThreeDate;

    @Bind(R.id.iv_forecast_weather4)
    ImageView mDayFourCondition;

    @Bind(R.id.tv_forecast_temp4)
    TextView mDayFourTemp;

    @Bind(R.id.tv_forecast_date4)
    TextView mDayFourDate;

    @Bind(R.id.tv_stats_wind)
    TextView mWeatherWind;

    @Bind(R.id.tv_stats_humidity)
    TextView mWeatherHumidity;

    @Bind(R.id.tv_stats_pressure)
    TextView mWeatherPressure;

    @Bind(R.id.tv_stats_visibility)
    TextView mWeatherVisibility;

    @Bind(R.id.tv_sunrise_time)
    TextView mSunriseTime;

    @Bind(R.id.tv_sunset_time)
    TextView mSunsetTime;

    @Bind(R.id.tv_calendar_event)
    TextView mCalendarEvent;

    @Bind(R.id.tv_reddit_post_title)
    TextView mRedditPostTitle;

    @Bind(R.id.tv_reddit_post_votes)
    TextView mRedditPostVotes;

    IMainPresenter mMainPresenter;
    View mDecorView;
    Configuration mConfiguration;
    WeatherIconGenerator mIconGenerator;

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

        //get configuration from Intent
        mConfiguration = (Configuration) getIntent().getSerializableExtra(Constants.CONFIGURATION_IDENTIFIER);

        mDecorView.setOnSystemUiVisibilityChangeListener(this);
        mMainPresenter = new MainPresenterImpl(this);
        mIconGenerator = WeatherIconGenerator.getInstance();
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
    public void displayCurrentWeather(CurrentWeather weather) {

        // Determine if user requested metric or imperial values and adjust units accordingly
        boolean metric = mConfiguration.isCelsius();
        String distance = metric ? Constants.DISTANCE_METRIC : Constants.DISTANCE_IMPERIAL;
        String pressure = metric ? Constants.PRESSURE_METRIC : Constants.PRESSURE_IMPERIAL;
        String speed = metric ? Constants.SPEED_METRIC : Constants.SPEED_IMPERIAL;
        String temperature = metric ? Constants.TEMPERATURE_METRIC : Constants.TEMPERATURE_IMPERIAL;

        this.mWeatherCondition.setImageResource(mIconGenerator.getIcon(Integer.parseInt(weather.getStatusCode())));
        this.mWeatherTemp.setText(weather.getTemperature() + "º" + temperature);
        this.mWeatherWind.setText(weather.getWindSpeed() + speed + " " + weather.getWindDirection() + " | " + weather.getWindTemperature() + "º" + temperature);
        this.mWeatherHumidity.setText(weather.getHumidity() + "%");
        this.mWeatherPressure.setText(weather.getPressure() + pressure);
        this.mWeatherVisibility.setText(weather.getVisibility() + distance);
        this.mSunriseTime.setText(weather.getSunrise());
        this.mSunsetTime.setText(weather.getSunset());

        List<Forecast> forecast = weather.getForecast();
        this.mDayOneDate.setText(forecast.get(0).getDate().substring(0, forecast.get(0).getDate().length() - 5));
        this.mDayOneTemp.setText(forecast.get(0).getLow() + "/" + forecast.get(0).getHigh() + "º" + temperature);
        this.mDayOneCondition.setImageResource(mIconGenerator.getIcon(Integer.parseInt(forecast.get(0).getCode())));
        this.mDayTwoDate.setText(forecast.get(1).getDate().substring(0, forecast.get(1).getDate().length() - 5));
        this.mDayTwoTemp.setText(forecast.get(1).getLow() + "/" + forecast.get(1).getHigh() + "º" + temperature);
        this.mDayTwoCondition.setImageResource(mIconGenerator.getIcon(Integer.parseInt(forecast.get(1).getCode())));
        this.mDayThreeDate.setText(forecast.get(2).getDate().substring(0, forecast.get(2).getDate().length() - 5));
        this.mDayThreeTemp.setText(forecast.get(2).getLow() + "/" + forecast.get(2).getHigh() + "º" + temperature);
        this.mDayThreeCondition.setImageResource(mIconGenerator.getIcon(Integer.parseInt(forecast.get(2).getCode())));
        this.mDayFourDate.setText(forecast.get(3).getDate().substring(0, forecast.get(3).getDate().length() - 5));
        this.mDayFourTemp.setText(forecast.get(3).getLow() + "/" + forecast.get(3).getHigh() + "º" + temperature);
        this.mDayFourCondition.setImageResource(mIconGenerator.getIcon(Integer.parseInt(forecast.get(3).getCode())));

        showContent(0);
    }

    @Override
    public void displayTopRedditPost(RedditPost redditPost) {
        mRedditPostTitle.setText(redditPost.getTitle());
        mRedditPostVotes.setText(redditPost.getUps() + "");
        showContent(1);
    }

    @Override
    public void displayLatestCalendarEvent(String event) {
        this.mCalendarEvent.setText(event);
        showContent(2);
    }

    @Override
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
    public void onError(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Start polling
        startPolling();

        // Updates the activity every time the Activity becomes visible again
        Assent.setActivity(this, this);
    }

    private void startPolling() {

        mMainPresenter.loadWeather(mConfiguration.getLocation(), mConfiguration.isCelsius(), mConfiguration.getPollingDelay());
        mMainPresenter.loadTopRedditPost(mConfiguration.getSubreddit(), mConfiguration.getPollingDelay());

        if (Assent.isPermissionGranted(Assent.READ_CALENDAR)) {
            mMainPresenter.loadLatestCalendarEvent(mConfiguration.getPollingDelay());
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
