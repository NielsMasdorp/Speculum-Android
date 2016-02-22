package com.nielsmasdorp.speculum.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.nielsmasdorp.speculum.SpeculumApplication;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class SharedPreferenceService {

    private static SharedPreferenceService sInstance;

    private SharedPreferences mPrefs;

    private SharedPreferenceService() {

    }

    public static SharedPreferenceService instance() {
        if (sInstance == null) {
            sInstance = new SharedPreferenceService();
        }

        return sInstance;
    }

    private SharedPreferences getPreferencesManager() {

        if (mPrefs == null) {
            mPrefs = SpeculumApplication.getContext().getSharedPreferences("saveState", Context.MODE_PRIVATE);
        }

        return mPrefs;
    }

    public void storeConfiguration(String location, String subreddit, int pollingDelay, boolean celsius, boolean voiceCommands, boolean rememberConfiguration) {

        SharedPreferences.Editor editor = getPreferencesManager().edit();

        editor.putString("location", location);
        editor.putString("subreddit", subreddit);
        editor.putInt("pollingDelay", pollingDelay);
        editor.putBoolean("celsius", celsius);
        editor.putBoolean("voiceCommands", voiceCommands);
        editor.putBoolean("rememberConfiguration", rememberConfiguration);

        editor.apply();
    }

    public String getLocation() {
        return getPreferencesManager().getString("location", null);
    }

    public String getSubreddit() {
        return getPreferencesManager().getString("subreddit", null);
    }

    public int getPollingDelay() {
        return getPreferencesManager().getInt("pollingDelay", 0);
    }

    public boolean getCelsius() {
        return getPreferencesManager().getBoolean("celsius", false);
    }

    public boolean getVoiceCommands() { return getPreferencesManager().getBoolean("voiceCommands", false); }

    public boolean getRememberConfiguration() { return getPreferencesManager().getBoolean("rememberConfiguration", false); }

    public void removeConfiguration() {

        SharedPreferences.Editor editor = getPreferencesManager().edit();

        editor.remove("location");
        editor.remove("subreddit");
        editor.remove("pollingDelay");
        editor.remove("celsius");
        editor.remove("voiceCommands");
        editor.remove("rememberConfiguration");

        editor.apply();
    }
}