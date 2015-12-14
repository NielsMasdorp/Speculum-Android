package com.nielsmasdorp.speculum.models.yahoo_weather;

import java.util.ArrayList;
import java.util.List;

public class Item {

    public String title;
    public String lat;
    public String _long;
    public String link;
    public String pubDate;
    public Condition condition;
    public String description;
    public List<Forecast> forecast = new ArrayList<Forecast>();
    public Guid guid;
}
