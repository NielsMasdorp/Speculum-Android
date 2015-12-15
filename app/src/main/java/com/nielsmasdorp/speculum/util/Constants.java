package com.nielsmasdorp.speculum.util;

/**
 * Created by Niels on 12/15/2015.
 */
public class Constants {

    public static final String WEATHER_QUERY_FIRST = "select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"";
    public static final String WEATHER_QUERY_SECOND = "\") and u=\"c\"";
    public static final String WEATHER_QUERY_FORMAT = "json";

    public static final int REDDIT_LIMIT = 1;

    public static final String LOCATION_DEFAULT = "New York";
    public static final String SUBREDDIT_DEFAULT = "news";

    public static final String REDDIT_BASE_URL = "https://www.reddit.com/r/";
    public static final String YAHOO_WEATHER_BASE_URL = "https://query.yahooapis.com/v1/public/";

    public static final String LOCATION_IDENTIFIER = "location";
    public static final String SUBREDDIT_IDENTIFIER = "subreddit";

    public static final String SIMPLEDATEFORMAT_DDMMYY = "dd/MM/yy";
    public static final String SIMPLEDATEFORMAT_HHMMSSDDMMYY = "hh:mm:ss dd/MM/yy";
    public static final String END_OF_DAY_TIME = "23:59:59 ";

    public static final String CALENDAR_QUERY_FIRST = "( dtstart >";
    public static final String CALENDAR_QUERY_SECOND = ") and (dtend  <";
    public static final String CALENDAR_QUERY_THIRD= ")";
    public static final String CALENDAR_QUERY_FOURTH= "dtstart ASC";
}
