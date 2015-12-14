
package com.nielsmasdorp.speculum.models.yahoo_weather;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
