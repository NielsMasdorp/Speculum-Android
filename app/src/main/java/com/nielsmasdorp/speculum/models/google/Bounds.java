
package com.nielsmasdorp.speculum.models.google;

import java.util.HashMap;
import java.util.Map;

public class Bounds {

    private Northeast northeast;
    private Southwest southwest;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Northeast getNortheast() {
        return northeast;
    }

    public void setNortheast(Northeast northeast) {
        this.northeast = northeast;
    }

    public Southwest getSouthwest() {
        return southwest;
    }

    public void setSouthwest(Southwest southwest) {
        this.southwest = southwest;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
