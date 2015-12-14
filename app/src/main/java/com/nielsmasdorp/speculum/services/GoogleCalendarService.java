package com.nielsmasdorp.speculum.services;

import com.nielsmasdorp.speculum.models.yahoo_weather.CurrentWeatherConditions;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.GET;
import rx.Observable;

/**
 * Created by Niels on 12/14/2015.
 */
public class GoogleCalendarService {

    private GoogleCalendarApi mGoogleCalendarApi;

    public GoogleCalendarService() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("foobar")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mGoogleCalendarApi = retrofit.create(GoogleCalendarApi.class);
    }

    public GoogleCalendarApi getApi() {

        return mGoogleCalendarApi;
    }

    public interface GoogleCalendarApi {

        @GET("yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22amsterdam%22)%20and%20u=%22c%22&format=json")
        Observable<CurrentWeatherConditions> getCurrentWeatherConditions();
    }
}
