
package com.nielsmasdorp.speculum.models.yahoo_weather;

import java.util.HashMap;
import java.util.Map;

public class Location {

    public String city;
    public String country;
    public String region;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
