package com.nielsmasdorp.speculum.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nielsmasdorp.speculum.R;
import com.nielsmasdorp.speculum.models.yahoo_weather.CurrentWeatherConditions;
import com.nielsmasdorp.speculum.presenters.MainPresenter;
import com.nielsmasdorp.speculum.presenters.SetupPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetupActivity extends AppCompatActivity implements ISetupView {

    @Bind(R.id.et_location)
    EditText mEditTextLocation;

    SetupPresenter mSetupPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        ButterKnife.bind(this);

        mSetupPresenter = new SetupPresenter(this);
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

    @OnClick(R.id.btn_launch)
    @SuppressWarnings("unused")
    public void launch() {
        mSetupPresenter.launch(mEditTextLocation.getText().toString());
    }

    @Override
    public void onSuccess(String location) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("location", location);
        startActivity(intent);
        finish();
    }

    @Override
    public void onError(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
