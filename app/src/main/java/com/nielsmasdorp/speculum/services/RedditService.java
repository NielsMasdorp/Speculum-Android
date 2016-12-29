package com.nielsmasdorp.speculum.services;

import com.nielsmasdorp.speculum.models.RedditPost;
import com.nielsmasdorp.speculum.models.reddit.Data_;
import com.nielsmasdorp.speculum.models.reddit.RedditResponse;
import com.nielsmasdorp.speculum.util.Constants;

import java.text.DecimalFormat;
import java.util.Random;

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
public class RedditService {

    private static final String REDDIT_UPS_THOUSAND_SUFFIX = "k";
    private static final String REDDIT_UPS_UNKNOWN = "-";

    private RedditApi redditApi;

    public RedditService() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REDDIT_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        redditApi = retrofit.create(RedditApi.class);
    }

    public Observable<RedditPost> getRedditPost(RedditResponse response) {

        //Get random post from list of top reddit posts
        Random r = new Random();
        int randomNumber = r.nextInt(Constants.REDDIT_LIMIT);
        Data_ postData = response.getData().getChildren().get(randomNumber).getData();

        return Observable.just(new RedditPost(postData.getTitle(), postData.getAuthor(),
                mapRedditUps(postData.getUps())));
    }

    private String mapRedditUps(Integer ups) {
        if (ups == null) {
            return REDDIT_UPS_UNKNOWN;
        }
        if ((ups / 1000) > 0) {
            DecimalFormat df = new DecimalFormat("#.#");
            return df.format((double) ups / 1000) + REDDIT_UPS_THOUSAND_SUFFIX;
        } else {
            return ups.toString();
        }
    }

    public RedditApi getApi() {

        return redditApi;
    }

    public interface RedditApi {

        @GET("{subreddit}/top.json")
        Observable<RedditResponse> getTopRedditPostForSubreddit(@Path("subreddit") String subreddit, @Query("limit") int limit);
    }
}
