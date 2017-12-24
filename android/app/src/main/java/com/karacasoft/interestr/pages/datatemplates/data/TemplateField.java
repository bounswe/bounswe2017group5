package com.karacasoft.interestr.pages.datatemplates.data;

import java.io.Serializable;

/**
 * Created by karacasoft on 22.11.2017.
 */

public class TemplateField implements Serializable {

    public enum Type {
        CHECKBOX,
        MULTISEL,
        TEXTAREA,
        TEXT,
        NUMBER,
        DATE,
        EMAIL,
        URL,
        TEL,
        SELECT,
    }

    private String name;
    private String hint;
    private Type type = Type.TEXT;

    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final Type getType() {
        return type;
    }

    public final void setType(Type type) {
        this.type = type;
    }

    public final String getHint() {
        return hint;
    }

    public final void setHint(String hint) {
        this.hint = hint;
    }
}
