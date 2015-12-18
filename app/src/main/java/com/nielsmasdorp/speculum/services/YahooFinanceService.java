package com.nielsmasdorp.speculum.services;

import com.nielsmasdorp.speculum.models.StockInformation;
import com.nielsmasdorp.speculum.models.yahoo_finance.Quote;
import com.nielsmasdorp.speculum.models.yahoo_finance.YahooFinanceResponse;
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
public class YahooFinanceService {

    private YahooFinanceApi mYahooFinanceApi;

    public YahooFinanceService() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.YAHOO_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mYahooFinanceApi = retrofit.create(YahooFinanceApi.class);
    }

    public Observable<StockInformation> getStockInformation(YahooFinanceResponse response) {

        Quote quote = response.getQuery().getResults().getQuote();
        return Observable.just(new StockInformation(quote.getSymbol(), quote.getChange(), quote.getName(), quote.getStockExchange()));
    }

    public YahooFinanceApi getApi() {

        return mYahooFinanceApi;
    }

    public interface YahooFinanceApi {

        @GET("yql")
        Observable<YahooFinanceResponse> getStockQuote(@Query("q") String query, @Query("format") String format, @Query("env") String env);
    }
}
