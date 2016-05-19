package com.nielsmasdorp.speculum.services;

import com.nielsmasdorp.speculum.models.google.GoogleMapsResponse;
import com.nielsmasdorp.speculum.models.google.Location;
import com.nielsmasdorp.speculum.models.google.Result;
import com.nielsmasdorp.speculum.util.Constants;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class GoogleMapService {

    private GoogleMapsApi mGoogleMapsApi;

    public GoogleMapService() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.GOOGLE_MAPS_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mGoogleMapsApi = retrofit.create(GoogleMapsApi.class);
    }

    public Observable<String> getLatLong(GoogleMapsResponse response) {

        if (response.getResults().isEmpty()) {
            throw new RuntimeException("No matches for your city found.");
        }

        // first result in the list is the best guess
        Result bestResult = response.getResults().get(0);
        Location location = bestResult.getGeometry().getLocation();

        return Observable.just(location.getLat() + "," + location.getLng());
    }

    public GoogleMapsApi getApi() {

        return mGoogleMapsApi;
    }

    public interface GoogleMapsApi {

        @GET("json")
        Observable<GoogleMapsResponse> getLatLongForAddress(@Query("address") String address, @Query("sensor") String sensorBool);
    }
}
