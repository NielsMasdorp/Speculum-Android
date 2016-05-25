package com.nielsmasdorp.speculum;

import android.app.Application;

import com.nielsmasdorp.speculum.di.component.ApplicationComponent;
import com.nielsmasdorp.speculum.di.component.DaggerApplicationComponent;
import com.nielsmasdorp.speculum.di.component.MainComponent;
import com.nielsmasdorp.speculum.di.component.SetupComponent;
import com.nielsmasdorp.speculum.di.module.AppModule;
import com.nielsmasdorp.speculum.di.module.MainModule;
import com.nielsmasdorp.speculum.di.module.ServiceModule;
import com.nielsmasdorp.speculum.di.module.SetupModule;
import com.nielsmasdorp.speculum.di.module.StorageModule;
import com.nielsmasdorp.speculum.di.module.UtilModule;
import com.nielsmasdorp.speculum.views.MainView;
import com.nielsmasdorp.speculum.views.SetupView;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class SpeculumApplication extends Application {

    private ApplicationComponent applicationComponent;
    private SetupComponent setupComponent;
    private MainComponent mainComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .appModule(new AppModule(this))
                .serviceModule(new ServiceModule())
                .storageModule(new StorageModule())
                .utilModule(new UtilModule())
                .build();
    }

    public SetupComponent createSetupComponent(SetupView view) {
        setupComponent = applicationComponent.plus(new SetupModule(view));
        return setupComponent;
    }

    public MainComponent createMainComponent(MainView view) {
        mainComponent = applicationComponent.plus(new MainModule(view));
        return mainComponent;
    }

    public void releaseSetupComponent() {
        setupComponent = null;
    }

    public void releaseMainComponent() {
        mainComponent = null;
    }
}
