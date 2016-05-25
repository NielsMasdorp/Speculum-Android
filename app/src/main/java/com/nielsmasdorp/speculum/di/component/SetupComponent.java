package com.nielsmasdorp.speculum.di.component;

import com.nielsmasdorp.speculum.activity.SetupActivity;
import com.nielsmasdorp.speculum.di.PerActivity;
import com.nielsmasdorp.speculum.di.module.SetupModule;

import dagger.Subcomponent;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
@PerActivity
@Subcomponent(modules = SetupModule.class)
public interface SetupComponent {

    void inject(SetupActivity setupActivity);
}
