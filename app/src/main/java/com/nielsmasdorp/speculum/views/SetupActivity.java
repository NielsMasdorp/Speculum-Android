package com.nielsmasdorp.speculum.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.afollestad.assent.Assent;
import com.afollestad.assent.AssentCallback;
import com.afollestad.assent.PermissionResultSet;
import com.nielsmasdorp.speculum.R;
import com.nielsmasdorp.speculum.models.Configuration;
import com.nielsmasdorp.speculum.presenters.ISetupPresenter;
import com.nielsmasdorp.speculum.presenters.SetupPresenterImpl;
import com.nielsmasdorp.speculum.util.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class SetupActivity extends AppCompatActivity implements ISetupView, View.OnSystemUiVisibilityChangeListener {

    @Bind(R.id.et_location)
    EditText mEditTextLocation;

    @Bind(R.id.et_stock)
    EditText mEditTextStock;

    @Bind(R.id.et_subreddit)
    EditText mEditTextSubreddit;

    @Bind(R.id.et_polling_delay)
    EditText mEditTextPollingDelay;

    @Bind(R.id.cb_atmosphere)
    CheckBox mCbAtmosphere;

    @Bind(R.id.cb_wind)
    CheckBox mCbWind;

    @Bind(R.id.cb_sun)
    CheckBox mCbSun;

    @Bind(R.id.cb_forecast)
    CheckBox mCbForecast;

    @Bind(R.id.rb_celsius)
    RadioButton mRbCelsius;

    ISetupPresenter mSetupPresenter;
    View mDecorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        ButterKnife.bind(this);
        Assent.setActivity(this, this);

        mDecorView = getWindow().getDecorView();
        hideSystemUI();

        if (!Assent.isPermissionGranted(Assent.READ_CALENDAR)) {
            Assent.requestPermissions(new AssentCallback() {
                @Override
                public void onPermissionResult(PermissionResultSet result) {
                    // Permission granted or denied
                    if (!result.allPermissionsGranted()) {
                        Toast.makeText(SetupActivity.this, getString(R.string.no_permission_for_calendar), Toast.LENGTH_SHORT).show();
                    }
                }
            }, 1, Assent.READ_CALENDAR);
        }

        mDecorView.setOnSystemUiVisibilityChangeListener(this);
        mSetupPresenter = new SetupPresenterImpl(this);
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

    @OnClick(R.id.btn_launch)
    @SuppressWarnings("unused")
    public void launch() {

        mSetupPresenter.launch(mEditTextLocation.getText().toString(), mEditTextStock.getText().toString(), mEditTextSubreddit.getText().toString(),
                mEditTextPollingDelay.getText().toString(), mCbWind.isChecked(), mCbAtmosphere.isChecked(),
                mCbSun.isChecked(), mRbCelsius.isChecked(), mCbForecast.isChecked());
    }

    @Override
    public void navigateToMainActivity(String location, String stock, String subreddit, int pollingDelay, boolean wind, boolean atmosphere,
                          boolean sun, boolean celsius, boolean forecast) {

        //Create configuration and pass in Intent
        Configuration configuration = new Configuration.Builder()
                .sun(sun)
                .atmosphere(atmosphere)
                .wind(wind)
                .celsius(celsius)
                .forecast(forecast)
                .location(location)
                .stock(stock)
                .subreddit(subreddit)
                .pollingDelay(pollingDelay)
                .build();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.CONFIGURATION_IDENTIFIER, configuration);
        startActivity(intent);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Lets Assent handle permission results and contact your callbacks
        Assent.handleResult(permissions, grantResults);
    }

    @Override
    public void onSystemUiVisibilityChange(int visibility) {

        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
            hideSystemUI();
        }
    }
}
