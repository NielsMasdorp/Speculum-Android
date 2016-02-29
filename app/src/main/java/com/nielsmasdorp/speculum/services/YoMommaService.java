package com.nielsmasdorp.speculum.services;

import com.nielsmasdorp.speculum.models.YoMommaJoke;
import com.nielsmasdorp.speculum.util.Constants;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.GET;
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
