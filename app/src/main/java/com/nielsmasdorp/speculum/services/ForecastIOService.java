package com.nielsmasdorp.speculum.services;

import com.nielsmasdorp.speculum.models.CurrentWeather;
import com.nielsmasdorp.speculum.models.forecast.ForecastResponse;
import com.nielsmasdorp.speculum.util.Constants;

import java.util.Date;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class ForecastIOService {

    private ForecastIOApi mForecastIOApi;

    public ForecastIOService() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.FORECAST_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mForecastIOApi = retrofit.create(ForecastIOApi.class);
    }

    public Observable<CurrentWeather> getCurrentWeather(ForecastResponse response) {

        // Convert degrees to cardinal directions for wind
        String[] directions = {"N", "NE", "E", "SE", "S", "SW", "W", "NW", "N"};
        String direction = directions[(int) Math.round((response.getCurrently().getWindBearing() % 360) / 45)];

        return Observable.just(new CurrentWeather.Builder()
                .lastUpdated(new Date((long) response.getCurrently().getTime() * 1000))
                .summary(response.getCurrently().getSummary())
                .icon(response.getCurrently().getIcon())
                .temperature(response.getCurrently().getTemperature().intValue())
                .humidity((int) (response.getCurrently().getHumidity() * 100))
                .pressure(response.getCurrently().getPressure().toString())
                .visibility(response.getCurrently().getVisibility().toString())
                .windSpeed(response.getCurrently().getWindSpeed().toString())
                .windTemperature(response.getCurrently().getApparentTemperature().intValue())
                .windDirection(direction)
                .forecast(response.getForecast().getData())
                .build());
    }

    public ForecastIOApi getApi() {

        return mForecastIOApi;
    }

    public interface ForecastIOApi {

        @GET("{apiKey}/{latLong}")
        Observable<ForecastResponse> getCurrentWeatherConditions(@Path("apiKey") String apiKey, @Path("latLong") String latLong, @Query("units") String units);
    }
}
