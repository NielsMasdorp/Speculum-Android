package com.nielsmasdorp.speculum.di.module;

import android.app.Application;

import com.nielsmasdorp.speculum.services.SharedPreferenceService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
@Module
public class StorageModule {

    @Provides
    @Singleton
    public SharedPreferenceService provideSharedPreferenceService(Application application) {

        return new SharedPreferenceService(application);
    }
}
