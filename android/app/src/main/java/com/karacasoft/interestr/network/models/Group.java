package com.karacasoft.interestr.network.models;

import com.karacasoft.interestr.util.ModelUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by karacasoft on 30.10.2017.
 */

public class Group implements Serializable{
    private int id;
    private String name;
    private String description;
    private String location;
    private int memberCount;
    private Date created;
    private Date updated;

    private ArrayList<DataTemplate> dataTemplates = new ArrayList<>();
    private ArrayList<Tag> tags = new ArrayList<>();

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

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public ArrayList<DataTemplate> getDataTemplates() {
        return dataTemplates;
    }

    public static Group fromJSON(JSONObject obj) throws JSONException {
        Group g = new Group();

        g.setId(obj.getInt("id"));
        g.setName(obj.getString("name"));
        g.setDescription(obj.getString("description"));
        g.setLocation(obj.getString("location"));
        g.setMemberCount(obj.getInt("size"));
        g.setPictureUrl(obj.getString("picture"));

        try {
            g.setCreated(ModelUtils.jsonGetDate(obj, "created"));
            g.setUpdated(ModelUtils.jsonGetDate(obj, "updated"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JSONArray dataTemplatesArr = ModelUtils.jsonGetJSONArrayOrDefault(obj, "data_templates", null);

        if(dataTemplatesArr != null) {
            for (int i = 0; i < dataTemplatesArr.length(); i++) {
                JSONObject dataTemplateObj = dataTemplatesArr.getJSONObject(i);

                try {
                    g.getDataTemplates().add(DataTemplate.fromJSONReduced(dataTemplateObj));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        JSONArray tagsArr = ModelUtils.jsonGetJSONArrayOrDefault(obj, "tags", null);

        if(tagsArr != null) {
            for (int i = 0; i < tagsArr.length(); i++) {
                JSONObject tagObj = tagsArr.getJSONObject(i);

                try {
                    g.getTags().add(Tag.fromJSON(tagObj));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return g;
    }

}
