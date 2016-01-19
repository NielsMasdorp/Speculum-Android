package com.nielsmasdorp.speculum.models;

import java.io.Serializable;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class Configuration implements Serializable {

    private boolean celsius;
    private String location;
    private String subreddit;
    private int pollingDelay;

    public static class Builder {

        private boolean celsius;
        private String location;
        private String subreddit;
        private int pollingDelay;

        public Builder celsius(boolean celsius){this.celsius = celsius; return this; }
        public Builder location(String location){this.location = location; return this; }
        public Builder subreddit(String subreddit){this.subreddit = subreddit; return this; }
        public Builder pollingDelay(int pollingDelay){this.pollingDelay = pollingDelay; return this; }

        public Configuration build() {

            return new Configuration(this);
        }
    }

    private Configuration(Builder builder) {

        this.celsius = builder.celsius;
        this.location = builder.location;
        this.subreddit = builder.subreddit;
        this.pollingDelay = builder.pollingDelay;
    }

    public boolean isCelsius() {
        return celsius;
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
