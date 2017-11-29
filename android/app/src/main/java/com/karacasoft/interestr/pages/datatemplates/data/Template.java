package com.karacasoft.interestr.pages.datatemplates.data;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by karacasoft on 22.11.2017.
 */

public class Template implements Serializable {
    private String name;
    private ArrayList<TemplateField> fields = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<TemplateField> getFields() {
        return fields;
    }

    public JSONObject toJSON() {
        JSONObject object = new JSONObject();

        // TODO fill object

        return object;
    }

    public static Template fromJSON(JSONObject object) {
        Template template = new Template();

        // TODO fill object

        return template;
    }


}
