package com.nielsmasdorp.speculum.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.afollestad.assent.Assent;
import com.afollestad.assent.AssentCallback;
import com.afollestad.assent.PermissionResultSet;
import com.nielsmasdorp.speculum.BuildConfig;
import com.nielsmasdorp.speculum.R;
import com.nielsmasdorp.speculum.presenters.ISetupPresenter;
import com.nielsmasdorp.speculum.presenters.SetupPresenterImpl;
import com.nielsmasdorp.speculum.util.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class SetupActivity extends AppCompatActivity implements ISetupView {

    @Bind(R.id.et_location)
    EditText mEditTextLocation;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        ButterKnife.bind(this);
        Assent.setActivity(this, this);

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

        if (BuildConfig.DEBUG) {
            mEditTextLocation.setText(Constants.LOCATION_DEFAULT);
            mEditTextSubreddit.setText(Constants.SUBREDDIT_DEFAULT);
        }

        mSetupPresenter = new SetupPresenterImpl(this);
    }

    @OnClick(R.id.btn_launch)
    @SuppressWarnings("unused")
    public void launch() {

        mSetupPresenter.launch(mEditTextLocation.getText().toString(), mEditTextSubreddit.getText().toString(),
                Integer.parseInt(mEditTextPollingDelay.getText().toString()), mCbWind.isChecked(), mCbAtmosphere.isChecked(),
                mCbSun.isChecked(), mRbCelsius.isChecked(), mCbForecast.isChecked());
    }

    @Override
    public void navigateToMainActivity(String location, String subreddit, int pollingDelay, boolean wind, boolean atmosphere,
                          boolean sun, boolean celsius, boolean forecast) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.LOCATION_IDENTIFIER, location);
        intent.putExtra(Constants.SUBREDDIT_IDENTIFIER, subreddit);
        intent.putExtra(Constants.WIND_IDENTIFIER, wind);
        intent.putExtra(Constants.SUN_IDENTIFIER, sun);
        intent.putExtra(Constants.ATMOSPHERE_IDENTIFIER, atmosphere);
        intent.putExtra(Constants.CELSIUS_IDENTIFIER, celsius);
        intent.putExtra(Constants.FORECAST_IDENTIFIER, forecast);
        intent.putExtra(Constants.POLLING_IDENTIFIER, pollingDelay);
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
}
