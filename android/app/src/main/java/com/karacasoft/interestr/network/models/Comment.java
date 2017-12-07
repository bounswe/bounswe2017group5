package com.karacasoft.interestr.network.models;

import com.karacasoft.interestr.util.ModelUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by karacasoft on 29.11.2017.
 */

public class Comment {
    private int id;
    private int owner;
    private String text;
    private int postId;
    private int ownerId;
    private String ownerName;
    private Date created;
    private Date updated;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public static Comment fromJSON(JSONObject obj) throws JSONException {
        Comment c = new Comment();

        c.setId(obj.getInt("id"));
        c.setText(obj.getString("text"));
        c.setPostId(obj.getInt("post"));

        JSONObject owner = obj.getJSONObject("owner");

        c.setOwnerId(owner.getInt("id"));
        c.setOwnerName(owner.getString("username"));

        try {
            c.setCreated(ModelUtils.jsonGetDate(obj, "created"));
            c.setUpdated(ModelUtils.jsonGetDate(obj, "updated"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return c;
    }
}
