package com.nielsmasdorp.speculum.services;

import com.nielsmasdorp.speculum.models.reddit.RedditResponse;
import com.nielsmasdorp.speculum.models.yahoo_weather.CurrentWeatherConditions;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Niels on 12/14/2015.
 */
public class RedditService {

    private RedditApi mRedditApi;

    public RedditService() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.reddit.com/r/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRedditApi = retrofit.create(RedditApi.class);
    }

    public RedditApi getApi() {

        return mRedditApi;
    }

    public interface RedditApi {

        @GET("{subreddit}/top.json")
        Observable<RedditResponse> getTopRedditPostForSubreddit(@Path("subreddit") String subreddit, @Query("limit") int limit);
    }
}
