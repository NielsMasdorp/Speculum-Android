package com.nielsmasdorp.speculum.models;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class Configuration {

    private boolean celsius;
    private String location;
    private String subreddit;
    private int pollingDelay;
    private boolean rememberConfig;
    private boolean voiceCommands;
    private boolean simpleLayout;

    public static class Builder {

        private boolean celsius;
        private String location;
        private String subreddit;
        private int pollingDelay;
        private boolean rememberConfig;
        private boolean voiceCommands;
        private boolean simpleLayout;

        public Builder celsius(boolean celsius) {
            this.celsius = celsius;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder subreddit(String subreddit) {
            this.subreddit = subreddit;
            return this;
        }

        public Builder pollingDelay(int pollingDelay) {
            this.pollingDelay = pollingDelay;
            return this;
        }

        public Builder rememberConfig(boolean rememberConfig) {
            this.rememberConfig = rememberConfig;
            return this;
        }

        public Builder voiceCommands(boolean voiceCommands) {
            this.voiceCommands = voiceCommands;
            return this;
        }

        public Builder simpleLayout(boolean simpleLayout) {
            this.simpleLayout = simpleLayout;
            return this;
        }

        public Configuration build() {

            return new Configuration(this);
        }
    }

    private Configuration(Builder builder) {

        this.celsius = builder.celsius;
        this.location = builder.location;
        this.subreddit = builder.subreddit;
        this.pollingDelay = builder.pollingDelay;
        this.rememberConfig = builder.rememberConfig;
        this.voiceCommands = builder.voiceCommands;
        this.simpleLayout = builder.simpleLayout;
    }

    public boolean isCelsius() {
        return celsius;
    }

    public void setCelsius(boolean celsius) {
        this.celsius = celsius;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    public int getPollingDelay() {
        return pollingDelay;
    }

    public void setPollingDelay(int pollingDelay) {
        this.pollingDelay = pollingDelay;
    }

    public boolean isVoiceCommands() {
        return voiceCommands;
    }

    public void setVoiceCommands(boolean voiceCommands) {
        this.voiceCommands = voiceCommands;
    }

    public boolean isSimpleLayout() {
        return simpleLayout;
    }

    public void setSimpleLayout(boolean simpleLayout) {
        this.simpleLayout = simpleLayout;
    }

    public boolean isRememberConfig() {
        return rememberConfig;
    }

    public void setRememberConfig(boolean rememberConfig) {
        this.rememberConfig = rememberConfig;
    }
}
