package com.nielsmasdorp.speculum.di.component;

import com.nielsmasdorp.speculum.activity.MainActivity;
import com.nielsmasdorp.speculum.di.PerActivity;
import com.nielsmasdorp.speculum.di.module.MainModule;

import dagger.Subcomponent;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
@PerActivity
@Subcomponent(modules = MainModule.class)
public interface MainComponent {

    void inject(MainActivity mainActivity);
}
