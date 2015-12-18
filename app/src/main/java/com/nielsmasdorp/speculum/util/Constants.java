package com.nielsmasdorp.speculum.util;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class Constants {

    public static final String WEATHER_QUERY_FIRST = "select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"";
    public static final String WEATHER_QUERY_SECOND_CELSIUS = "\") and u=\"c\"";
    public static final String WEATHER_QUERY_SECOND_FAHRENHEIT = "\")";

    public static final String FINANCE_QUERY_FIRST = "select * from yahoo.finance.quote where symbol in (\"";
    public static final String FINANCE_QUERY_SECOND = "\")";
    public static final String FINANCE_QUERY_ENV = "store://datatables.org/alltableswithkeys";

    public static final String YAHOO_QUERY_FORMAT = "json";

    public static final int REDDIT_LIMIT = 1;

    public static final String LOCATION_DEFAULT = "New York";
    public static final String SUBREDDIT_DEFAULT = "news";
    public static final String STOCK_DEFAULT = "MSFT";

    public static final String REDDIT_BASE_URL = "https://www.reddit.com/r/";
    public static final String YAHOO_BASE_URL = "https://query.yahooapis.com/v1/public/";

    public static final String CONFIGURATION_IDENTIFIER = "conf";

    public static final String SIMPLEDATEFORMAT_DDMMYY = "dd/MM/yy";
    public static final String SIMPLEDATEFORMAT_HHMMSSDDMMYY = "hh:mm:ss dd/MM/yy";
    public static final String END_OF_DAY_TIME = "23:59:59 ";

    public static final String CALENDAR_QUERY_FIRST = "( dtstart >";
    public static final String CALENDAR_QUERY_SECOND = ") and (dtend  <";
    public static final String CALENDAR_QUERY_THIRD= ")";
    public static final String CALENDAR_QUERY_FOURTH = "dtstart ASC";

    public static final String PRESSURE_IMPERIAL = "in";
    public static final String PRESSURE_METRIC = "mb";

    public static final String SPEED_IMPERIAL = "mph";
    public static final String SPEED_METRIC = "km/h";

    public static final String DISTANCE_IMPERIAL = "mi";
    public static final String DISTANCE_METRIC = "km";

    public static final String TEMPERATURE_IMPERIAL = "F";
    public static final String TEMPERATURE_METRIC = "C";
}
