package com.karacasoft.interestr.network.models;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by karacasoft on 29.11.2017.
 */

public class Post {
    private int id;
    private int owner;
    private int groupId;
    private int dataTemplateId;
    private JSONArray data;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getDataTemplateId() {
        return dataTemplateId;
    }

    public void setDataTemplateId(int dataTemplateId) {
        this.dataTemplateId = dataTemplateId;
    }

    public JSONArray getData() {
        return data;
    }

    public void setData(JSONArray data) {
        this.data = data;
    }
}
