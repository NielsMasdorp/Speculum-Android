package com.nielsmasdorp.speculum.models.reddit;

import java.util.ArrayList;
import java.util.List;

public class Data {

    private String modhash;
    private List<Child> children = new ArrayList<>();
    private String after;
    private Object before;

    public Data() {
    }

    public String getModhash() {
        return modhash;
    }

    public void setModhash(String modhash) {
        this.modhash = modhash;
    }

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public Object getBefore() {
        return before;
    }

    public void setBefore(Object before) {
        this.before = before;
    }
}
