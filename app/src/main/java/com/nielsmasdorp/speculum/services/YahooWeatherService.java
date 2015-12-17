package com.nielsmasdorp.speculum.services;

import com.nielsmasdorp.speculum.models.CurrentWeather;
import com.nielsmasdorp.speculum.models.yahoo_weather.Channel;
import com.nielsmasdorp.speculum.models.yahoo_weather.YahooWeatherResponse;
import com.nielsmasdorp.speculum.util.Constants;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.GET;
import retrofit.http.Query;

import rx.Observable;

/**
 * @author Niels Masdorp (NielsMasdorp)
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

        Channel weatherData = response.getQuery().getResults().getChannel();
        return Observable.just(new CurrentWeather.Builder()
                .title(weatherData.getTitle())
                .condition(weatherData.getItem().getCondition().getText())
                .temperature(weatherData.getItem().getCondition().getTemp())
                .humidity(weatherData.getAtmosphere().getHumidity())
                .pressure(weatherData.getAtmosphere().getPressure())
                .visibility(weatherData.getAtmosphere().getVisibility())
                .sunrise(weatherData.getAstronomy().getSunrise())
                .sunset(weatherData.getAstronomy().getSunset())
                .windSpeed(weatherData.getWind().getSpeed())
                .windTemperature(weatherData.getWind().getChill())
                .forecast(weatherData.getItem().getForecast())
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
