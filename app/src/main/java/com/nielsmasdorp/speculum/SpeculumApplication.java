package com.nielsmasdorp.speculum;

import android.app.Application;
import android.content.Context;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class SpeculumApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        SpeculumApplication.context = getApplicationContext();
    }

    public static Context getContext() {
        return SpeculumApplication.context;
    }
}
