package com.nielsmasdorp.speculum.models.reddit;

public class RedditResponse {

    private String kind;
    private Data data;

    public RedditResponse() {
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
