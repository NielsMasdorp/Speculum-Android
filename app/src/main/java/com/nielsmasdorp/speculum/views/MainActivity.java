package com.nielsmasdorp.speculum.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.nielsmasdorp.speculum.R;
import com.nielsmasdorp.speculum.models.yahoo_weather.CurrentWeatherConditions;
import com.nielsmasdorp.speculum.presenters.MainPresenter;
import com.nielsmasdorp.speculum.services.YahooWeatherService;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements IMainView {

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

    @Bind(R.id.pb_loading_spinner)
    ProgressBar mProgressLoading;

    MainPresenter mMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mMainPresenter = new MainPresenter(this);
        mMainPresenter.loadWeather();

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void displayCurrentWeather(CurrentWeatherConditions currentConditions) {
        hideLoading();
        this.mWeatherTitle.setText(currentConditions.query.results.channel.item.title);

        this.mWeatherCondition.setText(currentConditions.query.results.channel.item.condition.temp + "℃, " +
                currentConditions.query.results.channel.item.condition.text);

        this.mWeatherAtmosphere.setText("humidity: " + currentConditions.query.results.channel.atmosphere.humidity + "%, pressure: " +
                currentConditions.query.results.channel.atmosphere.pressure + "mb, visibility: " +
                currentConditions.query.results.channel.atmosphere.visibility + "km");

        this.mWeatherAstronomy.setText("sunrise: " + currentConditions.query.results.channel.astronomy.sunrise + ", sunset: " +
                currentConditions.query.results.channel.astronomy.sunset);

        this.mWeatherWind.setText("wind temp: " + currentConditions.query.results.channel.wind.chill + "℃, wind speed: " +
                currentConditions.query.results.channel.wind.speed + "km/h");

        this.mWeatherLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading() {

        this.mProgressLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {

        this.mProgressLoading.setVisibility(View.GONE);
    }

    @Override
    public void onError(String message) {

        hideLoading();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
