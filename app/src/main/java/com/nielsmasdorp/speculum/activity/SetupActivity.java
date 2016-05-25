package com.nielsmasdorp.speculum.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.assent.Assent;
import com.nielsmasdorp.speculum.R;
import com.nielsmasdorp.speculum.SpeculumApplication;
import com.nielsmasdorp.speculum.models.Configuration;
import com.nielsmasdorp.speculum.presenters.SetupPresenter;
import com.nielsmasdorp.speculum.util.Constants;
import com.nielsmasdorp.speculum.views.SetupView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class SetupActivity extends AppCompatActivity implements SetupView, View.OnSystemUiVisibilityChangeListener, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.et_location)
    EditText etLocation;

    @BindView(R.id.et_subreddit)
    EditText etSubreddit;

    @BindView(R.id.tv_reddit_title)
    TextView tvRedditTitle;

    @BindView(R.id.et_polling_delay)
    EditText etPollingDelay;

    @BindView(R.id.rb_celsius)
    RadioButton rbCelsius;

    @BindView(R.id.rb_simple)
    RadioButton rbSimpleLayout;

    @BindView(R.id.cb_voice_commands)
    CheckBox cbVoiceCommands;

    @BindView(R.id.cb_remember_config)
    CheckBox cbRememberConfig;

    @Inject
    SetupPresenter presenter;

    View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        ((SpeculumApplication) getApplication()).createSetupComponent(this).inject(this);
        ButterKnife.bind(this);
        Assent.setActivity(this, this);

        if (!Assent.isPermissionGranted(Assent.READ_CALENDAR)) {
            Assent.requestPermissions(result -> {
                // Permission granted or denied
                if (!result.allPermissionsGranted()) {
                    Toast.makeText(SetupActivity.this, getString(R.string.no_permission_for_calendar), Toast.LENGTH_SHORT).show();
                }
            }, 1, Assent.READ_CALENDAR);
        }

        cbVoiceCommands.setOnCheckedChangeListener(this);
        rbSimpleLayout.setOnCheckedChangeListener(this);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void hideSystemUI() {
        decorView = getWindow().getDecorView();

        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        decorView.setOnSystemUiVisibilityChangeListener(this);
    }

    @OnClick(R.id.btn_launch)
    @SuppressWarnings("unused")
    public void launch() {

        presenter.validate(etLocation.getText().toString(), etSubreddit.getText().toString(),
                etPollingDelay.getText().toString(), rbCelsius.isChecked(), cbVoiceCommands.isChecked(), cbRememberConfig.isChecked(), rbSimpleLayout.isChecked());
    }

    @Override
    public void navigateToMainActivity(Configuration configuration) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.CONFIGURATION_IDENTIFIER, configuration);
        intent.putExtra(Constants.SAVED_CONFIGURATION_IDENTIFIER, configuration.isRememberConfig());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        hideSystemUI();

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

    @Override
    public void showError(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    @SuppressWarnings("all")
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.cb_voice_commands) {
            if (isChecked) {
                if (!Assent.isPermissionGranted(Assent.RECORD_AUDIO)) {
                    Assent.requestPermissions(result -> {
                        // Permission granted or denied
                        if (!result.allPermissionsGranted()) {
                            Toast.makeText(SetupActivity.this, getString(R.string.no_permission_for_voice), Toast.LENGTH_SHORT).show();
                            cbVoiceCommands.setChecked(false);
                        }
                    }, 2, Assent.RECORD_AUDIO);
                }
            }
        } else {
            if (isChecked) {
                etSubreddit.setVisibility(View.GONE);
                tvRedditTitle.setVisibility(View.GONE);
            } else {
                etSubreddit.setVisibility(View.VISIBLE);
                tvRedditTitle.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((SpeculumApplication) getApplication()).releaseSetupComponent();
    }
}
