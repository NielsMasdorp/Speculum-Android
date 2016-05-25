package com.nielsmasdorp.speculum.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerActivity { }
