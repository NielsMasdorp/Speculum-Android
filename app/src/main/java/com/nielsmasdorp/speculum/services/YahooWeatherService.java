package com.nielsmasdorp.speculum.services;

import android.net.Uri;

import com.nielsmasdorp.speculum.models.yahoo_weather.CurrentWeatherConditions;


import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.GET;
import retrofit.http.Query;
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

        @GET("yql")
        Observable<CurrentWeatherConditions> getCurrentWeatherConditions(@Query("q") String query, @Query("format") String format);
    }
}
