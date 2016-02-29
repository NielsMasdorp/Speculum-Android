package com.nielsmasdorp.speculum.util;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class Constants {

    //Query constants
    public static final String REDDIT_BASE_URL = "https://www.reddit.com/r/";
    public static final String YAHOO_BASE_URL = "https://query.yahooapis.com/v1/public/";
    public static final String YOMOMMA_BASE_URL = "http://api.yomomma.info/";

    public static final String WEATHER_QUERY_FIRST = "select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"";
    public static final String WEATHER_QUERY_SECOND_CELSIUS = "\") and u=\"c\"";
    public static final String WEATHER_QUERY_SECOND_FAHRENHEIT = "\")";

    public static final String CALENDAR_QUERY_FIRST = "( dtstart >";
    public static final String CALENDAR_QUERY_SECOND = ") and (dtend  <";
    public static final String CALENDAR_QUERY_THIRD = ")";
    public static final String CALENDAR_QUERY_FOURTH = "dtstart ASC";

    //Query extras
    public static final String YAHOO_QUERY_FORMAT = "json";
    public static final int REDDIT_LIMIT = 5;

    //Defaults
    public static final String LOCATION_DEFAULT = "New York";
    public static final String SUBREDDIT_DEFAULT = "news";
    public static final String POLLING_DELAY_DEFAULT = "30";

    //Intent identifiers
    public static final String CONFIGURATION_IDENTIFIER = "conf";
    public static final String SAVED_CONFIGURATION_IDENTIFIER = "savedConf";

    //Formats
    public static final String SIMPLEDATEFORMAT_DDMMYY = "dd/MM/yy";
    public static final String SIMPLEDATEFORMAT_HHMMSSDDMMYY = "hh:mm:ss dd/MM/yy";
    public static final String END_OF_DAY_TIME = "23:59:59 ";

    //Units
    public static final String PRESSURE_IMPERIAL = "in";
    public static final String PRESSURE_METRIC = "mb";

    public static final String SPEED_IMPERIAL = "mph";
    public static final String SPEED_METRIC = "km/h";

    public static final String DISTANCE_IMPERIAL = "mi";
    public static final String DISTANCE_METRIC = "km";

    public static final String TEMPERATURE_IMPERIAL = "F";
    public static final String TEMPERATURE_METRIC = "C";

    //Speech recognition commands
    public static final String KWS_SEARCH = "wakeup";
    public static final String COMMANDS_SEARCH = "commands";
    public static final String KEYPHRASE = "hello magic mirror";
    public static final String UPDATE_PHRASE = "get me new data";
    public static final String NEWS_PHRASE = "what is new";
    public static final String JOKE_PHRASE = "tell me a joke";
    public static final String SLEEP_PHRASE = "go back to sleep";

    //TTS phrases
    public static final String UPDATE_NOTIFICATION = "Okay, data is being updated.";
    public static final String SLEEP_NOTIFICATION = "I am going back to sleep, bye bye.";
    public static final String WAKE_NOTIFICATION = "Hello there. What can I do for you?";

    //Shared preference identifiers
    public static final String SP_LOCATION_IDENTIFIER = "location";
    public static final String SP_SUBREDDIT_IDENTIFIER = "subreddit";
    public static final String SP_POLLING_IDENTIFIER = "pollingDelay";
    public static final String SP_CELSIUS_IDENTIFIER = "celsius";
    public static final String SP_VOICE_IDENTIFIER = "voiceCommands";
    public static final String SP_REMEMBER_IDENTIFIER = "rememberConfiguration";
    public static final String SP_LAYOUT_IDENTIFIER = "simpleLayout";
}
