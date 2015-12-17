package com.nielsmasdorp.speculum.services;

import com.nielsmasdorp.speculum.models.CurrentWeather;
import com.nielsmasdorp.speculum.models.RedditPost;
import com.nielsmasdorp.speculum.models.reddit.Data_;
import com.nielsmasdorp.speculum.models.reddit.RedditResponse;
import com.nielsmasdorp.speculum.models.yahoo_weather.YahooWeatherResponse;
import com.nielsmasdorp.speculum.util.Constants;

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
                .baseUrl(Constants.REDDIT_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRedditApi = retrofit.create(RedditApi.class);
    }

    public Observable<RedditPost> getRedditPost(RedditResponse response) {

        Data_ postData = response.getData().getChildren().get(0).getData();
        return Observable.just(new RedditPost(postData.getTitle(), postData.getAuthor(),
                postData.getUps()));
    }

    public RedditApi getApi() {

        return mRedditApi;
    }

    public interface RedditApi {

        @GET("{subreddit}/top.json")
        Observable<RedditResponse> getTopRedditPostForSubreddit(@Path("subreddit") String subreddit, @Query("limit") int limit);
    }
}
