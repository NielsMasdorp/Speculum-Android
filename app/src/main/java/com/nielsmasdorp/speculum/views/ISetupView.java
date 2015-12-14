package com.nielsmasdorp.speculum.views;

/**
 * Created by Niels on 12/14/2015.
 */
public interface ISetupView {

    void onSuccess(String location);

    void onError(String message);
}
