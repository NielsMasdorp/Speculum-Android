package com.nielsmasdorp.speculum.models.yahoo_weather;

public class YahooWeatherResponse {

    private Query query;

    public YahooWeatherResponse() {
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }
}
