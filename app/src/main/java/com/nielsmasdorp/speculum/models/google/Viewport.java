
package com.nielsmasdorp.speculum.models.google;

import java.util.HashMap;
import java.util.Map;

public class Viewport {

    private Northeast_ northeast;
    private Southwest_ southwest;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Northeast_ getNortheast() {
        return northeast;
    }

    public void setNortheast(Northeast_ northeast) {
        this.northeast = northeast;
    }

    public Southwest_ getSouthwest() {
        return southwest;
    }

    public void setSouthwest(Southwest_ southwest) {
        this.southwest = southwest;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
