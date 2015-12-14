package com.nielsmasdorp.speculum.services;

import android.net.Uri;

import com.nielsmasdorp.speculum.models.yahoo_weather.CurrentWeatherConditions;


import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.GET;
import rx.Observable;

/**
 * Created by Niels on 12/14/2015.
 */
public class YahooWeatherService {

    private YahooWeatherApi mYahooWeatherApi;

    public YahooWeatherService() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://query.yahooapis.com/v1/public/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mYahooWeatherApi = retrofit.create(YahooWeatherApi.class);
    }

    public YahooWeatherApi getApi() {

        return mYahooWeatherApi;
    }

    public interface YahooWeatherApi {

        @GET("yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22amsterdam%22)%20and%20u=%22c%22&format=json")
        Observable<CurrentWeatherConditions> getCurrentWeatherConditions();
    }
}
