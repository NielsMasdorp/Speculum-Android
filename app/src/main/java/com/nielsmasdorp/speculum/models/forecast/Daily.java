
package com.nielsmasdorp.speculum.models.forecast;

import java.util.ArrayList;
import java.util.List;

public class Daily {

    private String summary;
    private String icon;
    private List<Datum__> data = new ArrayList<Datum__>();

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

    public List<Datum__> getData() {
        return data;
    }

    public void setData(List<Datum__> data) {
        this.data = data;
    }
}
