package com.karacasoft.interestr.network.models;

import com.karacasoft.interestr.pages.datatemplates.data.Template;
import com.karacasoft.interestr.util.ModelUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by karacasoft on 29.11.2017.
 */

public class DataTemplate {
    private int id;
    private String name;
    private int groupId;
    private String groupName;
    private int userId;
    private String userName;
    private Template template;
    private Date created;
    private Date updated;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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


    /**
     * Parses a JSONObject into a DataTemplate object.
     *
     * @param obj JSONObject to be parsed
     * @return DataTemplate object that the JSONObject contains
     * @throws JSONException If the format of the JSON is not correct
     */
    public static DataTemplate fromJSON(JSONObject obj) throws JSONException {
        DataTemplate t = new DataTemplate();

        t.setId(obj.getInt("id"));
        t.setName(obj.getString("name"));

        JSONObject group = obj.getJSONObject("group");

        t.setGroupId(group.getInt("id"));
        t.setGroupName(group.getString("name"));

        JSONObject user = obj.getJSONObject("user");

        t.setUserId(user.getInt("id"));
        t.setUserName(user.getString("username"));

        try {
            t.setCreated(ModelUtils.jsonGetDate(obj, "created"));
            t.setUpdated(ModelUtils.jsonGetDate(obj, "updated"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        t.setTemplate(Template.fromJSON(obj.getJSONArray("fields")));

        return t;
    }


    /**
     * Parses a JSONObject into a DataTemplate and returns it. The difference from
     * {@link DataTemplate#fromJSON(JSONObject)} is this one only requires <code>id</code>,
     * <code>name</code> and <code>fields</code> fields to exist.
     *
     * @param obj JSONObject to be parsed
     * @return A DataTemplate object
     * @throws JSONException if the format of the JSON is not correct
     */
    public static DataTemplate fromJSONReduced(JSONObject obj) throws JSONException {
        DataTemplate t = new DataTemplate();

        t.setId(obj.getInt("id"));
        t.setName(obj.getString("name"));
        t.setTemplate(Template.fromJSON(obj.getJSONArray("fields")));

        return t;
    }


}
