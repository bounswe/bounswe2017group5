package com.karacasoft.interestr.network.models;

import com.karacasoft.interestr.pages.datatemplates.data.Template;

/**
 * Created by karacasoft on 29.11.2017.
 */

public class DataTemplate {
    private int id;
    private String name;
    private int groupId;
    private int userId;
    private Template template;

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
}
