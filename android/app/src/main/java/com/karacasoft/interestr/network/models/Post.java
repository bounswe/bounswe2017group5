package com.karacasoft.interestr.network.models;

import com.karacasoft.interestr.pages.datatemplates.data.Template;
import com.karacasoft.interestr.util.ModelUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by karacasoft on 29.11.2017.
 */

public class Post {
    private int id;
    private int owner;
    private String ownerName;
    private int groupId;
    private String groupName;
    private int dataTemplateId;
    private String dataTemplateName;
    private Template dataTemplateFields;
    private JSONArray data;
    private Date created;
    private Date updated;


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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDataTemplateName() {
        return dataTemplateName;
    }

    public void setDataTemplateName(String dataTemplateName) {
        this.dataTemplateName = dataTemplateName;
    }

    public Template getDataTemplateFields() {
        return dataTemplateFields;
    }

    public void setDataTemplateFields(Template dataTemplateFields) {
        this.dataTemplateFields = dataTemplateFields;
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

    public static Post fromJSON(JSONObject obj) throws JSONException {
        Post p = new Post();

        p.setId(obj.getInt("id"));

        try {
            JSONObject owner = obj.getJSONObject("owner");

            p.setOwner(owner.getInt("id"));
            p.setOwnerName(owner.getString("username"));
        } catch (JSONException e) {
            p.setOwner(obj.getInt("owner"));
        }

        try {
            JSONObject group = obj.getJSONObject("group");

            p.setGroupId(group.getInt("id"));
            p.setGroupName(group.getString("name"));
        } catch(JSONException e) {
            p.setGroupId(obj.getInt("group"));
        }


        try {
            JSONObject dataTemplate = obj.getJSONObject("data_template");

            p.setDataTemplateId(dataTemplate.getInt("id"));
            p.setDataTemplateName(dataTemplate.getString("name"));
            p.setDataTemplateFields(Template.fromJSON(dataTemplate.getJSONArray("fields")));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            p.setCreated(ModelUtils.jsonGetDate(obj, "created"));
            p.setUpdated(ModelUtils.jsonGetDate(obj, "updated"));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        p.setData(ModelUtils.jsonGetJSONArrayOrDefault(obj, "data", null));

        return p;
    }

}
