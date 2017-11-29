package com.karacasoft.interestr.network.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by karacasoft on 30.10.2017.
 */

public class Group implements Serializable{
    private int id;
    private String name;
    private String description;
    private String location;
    private int memberCount;

    private ArrayList<String> tags = new ArrayList<>();

    private String pictureUrl;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public ArrayList<String> getTags() {
        return tags;
    }
}
