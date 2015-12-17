package com.nielsmasdorp.speculum.models;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class Configuration {

    private boolean sun;
    private boolean atmosphere;
    private boolean wind;
    private boolean celsius;
    private boolean forecast;
    private String location;
    private String subreddit;
    private int pollingDelay;

    public static class Builder {

        private boolean sun;
        private boolean atmosphere;
        private boolean wind;
        private boolean celsius;
        private boolean forecast;
        private String location;
        private String subreddit;
        private int pollingDelay;

        public Builder sun(boolean sun){this.sun = sun; return this; }
        public Builder atmosphere(boolean atmosphere){this.atmosphere = atmosphere; return this; }
        public Builder wind(boolean wind){this.wind = wind; return this; }
        public Builder celsius(boolean celsius){this.celsius = celsius; return this; }
        public Builder forecast(boolean forecast){this.forecast = forecast; return this; }
        public Builder location(String location){this.location = location; return this; }
        public Builder subreddit(String subreddit){this.subreddit = subreddit; return this; }
        public Builder pollingDelay(int pollingDelay){this.pollingDelay = pollingDelay; return this; }

        public Configuration build() {

            return new Configuration(this);
        }
    }

    private Configuration(Builder builder) {

        this.sun = builder.sun;
        this.atmosphere = builder.atmosphere;
        this.wind = builder.wind;
        this.celsius = builder.celsius;
        this.forecast = builder.forecast;
        this.location = builder.location;
        this.subreddit = builder.subreddit;
        this.pollingDelay = builder.pollingDelay;
    }

    public boolean isSun() {
        return sun;
    }

    public boolean isAtmosphere() {
        return atmosphere;
    }

    public boolean isWind() {
        return wind;
    }

    public boolean isCelsius() {
        return celsius;
    }

    public boolean isForecast() {
        return forecast;
    }

    public String getLocation() {
        return location;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public int getPollingDelay() {
        return pollingDelay;
    }
}
