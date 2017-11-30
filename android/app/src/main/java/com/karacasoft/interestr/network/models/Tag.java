package com.karacasoft.interestr.network.models;

import java.io.Serializable;

/**
 * Created by karacasoft on 30.11.2017.
 */

public class Tag implements Serializable {
    private int id;
    private String label;
    private String url;
    private String description;
    private String conceptUri;

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
}
