
package com.nielsmasdorp.speculum.models.forecast;

import java.util.ArrayList;
import java.util.List;

public class Hourly {

    private String summary;
    private String icon;
    private List<Datum_> data = new ArrayList<Datum_>();

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<Datum_> getData() {
        return data;
    }

    public void setData(List<Datum_> data) {
        this.data = data;
    }
}
