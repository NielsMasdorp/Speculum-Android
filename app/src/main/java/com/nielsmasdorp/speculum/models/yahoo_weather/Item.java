package com.nielsmasdorp.speculum.models.yahoo_weather;

import java.util.ArrayList;
import java.util.List;

/**
 * Generated class ("org.jsonschema2pojo")
 */
public class Item {

    private String title;
    private String latitude;
    private String longitude;
    private String link;
    private String pubDate;
    private Condition condition;
    private String description;
    private List<Forecast> forecast = new ArrayList<>();
    private Guid guid;

    public Item() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Forecast> getForecast() {
        return forecast;
    }

    public void setForecast(List<Forecast> forecast) {
        this.forecast = forecast;
    }

    public Guid getGuid() {
        return guid;
    }

    public void setGuid(Guid guid) {
        this.guid = guid;
    }
}
