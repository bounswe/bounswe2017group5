package com.karacasoft.interestr.network.models;

import org.json.JSONObject;

/**
 * Created by karacasoft on 29.11.2017.
 */

public class Post {
    private int owner;
    private int groupId;
    private int dataTemplateId;
    private JSONObject data;

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

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}
