package com.nielsmasdorp.speculum.services;

import com.nielsmasdorp.speculum.models.CurrentWeather;
import com.nielsmasdorp.speculum.models.yahoo_weather.YahooWeatherResponse;
import com.nielsmasdorp.speculum.util.Constants;

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
                .baseUrl(Constants.YAHOO_WEATHER_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mYahooWeatherApi = retrofit.create(YahooWeatherApi.class);
    }

    public Observable<CurrentWeather> getCurrentWeather(YahooWeatherResponse response) {
        return Observable.just(new CurrentWeather.Builder()
                .title(response.query.results.channel.item.title)
                .condition(response.query.results.channel.item.condition.text)
                .temperature(response.query.results.channel.item.condition.temp)
                .humidity(response.query.results.channel.atmosphere.humidity)
                .pressure(response.query.results.channel.atmosphere.pressure)
                .visibility(response.query.results.channel.atmosphere.visibility)
                .sunrise(response.query.results.channel.astronomy.sunrise)
                .sunset(response.query.results.channel.astronomy.sunset)
                .windSpeed(response.query.results.channel.wind.speed)
                .windTemperature(response.query.results.channel.wind.chill)
                .forecast(response.query.results.channel.item.forecast)
                .build());
    }

    public YahooWeatherApi getApi() {

        return mYahooWeatherApi;
    }

    public interface YahooWeatherApi {

        @GET("yql")
        Observable<YahooWeatherResponse> getCurrentWeatherConditions(@Query("q") String query, @Query("format") String format);
    }
}
