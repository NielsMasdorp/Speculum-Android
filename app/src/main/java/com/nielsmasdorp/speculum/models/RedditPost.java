package com.nielsmasdorp.speculum.models;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class RedditPost {

    private String title;
    private String author;
    private int ups;

    public RedditPost(String title, String author, int ups) {
        this.title = title;
        this.author = author;
        this.ups = ups;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getUps() {
        return ups;
    }
}
