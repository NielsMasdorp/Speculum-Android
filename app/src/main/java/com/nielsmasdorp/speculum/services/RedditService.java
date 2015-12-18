package com.nielsmasdorp.speculum.services;

import com.nielsmasdorp.speculum.models.RedditPost;
import com.nielsmasdorp.speculum.models.reddit.Data_;
import com.nielsmasdorp.speculum.models.reddit.RedditResponse;
import com.nielsmasdorp.speculum.util.Constants;

import java.util.Random;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * @author Niels Masdorp (NielsMasdorp)
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

        //Get random post from list of top reddit posts
        Random r = new Random();
        int randomNumber = r.nextInt(Constants.REDDIT_LIMIT - 0);
        Data_ postData = response.getData().getChildren().get(randomNumber).getData();

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
