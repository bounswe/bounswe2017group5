package com.karacasoft.interestr.network.models;

import com.karacasoft.interestr.util.ModelUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by karacasoft on 30.11.2017.
 */

public class Tag implements Serializable {
    private int id;
    private String label;
    private String url;
    private String description;
    private String conceptUri;

    private Date created;
    private Date updated;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConceptUri() {
        return conceptUri;
    }

    public void setConceptUri(String conceptUri) {
        this.conceptUri = conceptUri;
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

    public static Tag fromJSON(JSONObject obj) throws JSONException {
        Tag t = new Tag();

        t.setId(obj.getInt("id"));
        t.setLabel(obj.getString("label"));
        t.setUrl(obj.getString("url"));
        t.setDescription(obj.getString("description"));
        t.setConceptUri(obj.getString("concepturi"));

        try {
            t.setCreated(ModelUtils.jsonGetDate(obj, "created"));
            t.setUpdated(ModelUtils.jsonGetDate(obj, "updated"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return t;
    }
}
