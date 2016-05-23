package com.nielsmasdorp.speculum.di.component;

import com.nielsmasdorp.speculum.di.module.AppModule;
import com.nielsmasdorp.speculum.di.module.MainModule;
import com.nielsmasdorp.speculum.di.module.ServiceModule;
import com.nielsmasdorp.speculum.di.module.SetupModule;
import com.nielsmasdorp.speculum.di.module.StorageModule;
import com.nielsmasdorp.speculum.di.module.UtilModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
@Singleton
@Component(modules = {AppModule.class, StorageModule.class, UtilModule.class, ServiceModule.class})
public interface ApplicationComponent {

    SetupComponent plus(SetupModule setupModule);

    MainComponent plus(MainModule mainModule);
}
