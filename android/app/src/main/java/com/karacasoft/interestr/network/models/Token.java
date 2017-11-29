package com.karacasoft.interestr.network.models;

import java.io.Serializable;

/**
 * Created by karacasoft on 18.11.2017.
 */

public class Token implements Serializable {

    private String key;

    private boolean anonymous = true;

    public Token() {}

    public Token(String key) {
        this.key = key;
        anonymous = false;
    }

    public String getKey() {
        return key;
    }

    public boolean isAnonymous() {
        return anonymous;
    }
}
