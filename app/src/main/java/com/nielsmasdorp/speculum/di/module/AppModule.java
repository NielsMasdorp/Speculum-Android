package com.nielsmasdorp.speculum.di.module;

import android.app.Application;

import com.nielsmasdorp.speculum.SpeculumApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
@Module
public class AppModule {

    private SpeculumApplication app;

    public AppModule(SpeculumApplication app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public Application provideApplication() {
        return app;
    }
}