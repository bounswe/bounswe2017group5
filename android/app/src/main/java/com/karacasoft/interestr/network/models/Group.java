package com.karacasoft.interestr.network.models;

/**
 * Created by karacasoft on 30.10.2017.
 */

public class Group {
    private int id;
    private String name;
    private String description;
    private int memberCount;

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

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
