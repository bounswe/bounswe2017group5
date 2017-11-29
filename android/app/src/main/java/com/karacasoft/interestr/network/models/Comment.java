package com.karacasoft.interestr.network.models;

/**
 * Created by karacasoft on 29.11.2017.
 */

public class Comment {
    private int owner;
    private String text;
    private int postId;

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
