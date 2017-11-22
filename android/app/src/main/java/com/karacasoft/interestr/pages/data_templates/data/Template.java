package com.karacasoft.interestr.pages.data_templates.data;

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
}
