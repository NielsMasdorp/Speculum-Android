package com.nielsmasdorp.speculum.services;

import com.nielsmasdorp.speculum.models.YoMommaJoke;
import com.nielsmasdorp.speculum.util.Constants;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import rx.Observable;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class YoMommaService {

    private YoMommaApi mYoMommaApi;

    public YoMommaService() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.YOMOMMA_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mYoMommaApi = retrofit.create(YoMommaApi.class);
    }

    public YoMommaApi getApi() {

        return mYoMommaApi;
    }

    public interface YoMommaApi {

        @GET("/")
        Observable<YoMommaJoke> getJoke();
    }
}
