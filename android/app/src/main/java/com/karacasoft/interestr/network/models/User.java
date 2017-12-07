package com.karacasoft.interestr.network.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by karacasoft on 13/11/17.
 */

public class User {

    private int id;
    private String username;
    private String password;
    private String email;
    private ArrayList<Group> joinedGroups = new ArrayList<>();
    private ArrayList<Group> moderatedGroups = new ArrayList<>();

    private ArrayList<DataTemplate> dataTemplates = new ArrayList<>();
    private ArrayList<Post> posts = new ArrayList<>();

    public User() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email= email;
    }

    public ArrayList<Group> getJoinedGroups() {
        return joinedGroups;
    }

    public ArrayList<Group> getModeratedGroups() {
        return moderatedGroups;
    }

    public ArrayList<DataTemplate> getDataTemplates() {
        return dataTemplates;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public static User fromJSON(JSONObject obj) throws JSONException {
        User u = new User();

        u.setId(obj.getInt("id"));
        u.setUsername(obj.getString("username"));
        u.setEmail(obj.getString("email"));

        JSONArray joinedGroups = obj.getJSONArray("joined_groups");

        for (int i = 0; i < joinedGroups.length(); i++) {
            Group g = Group.fromSmallJSON(joinedGroups.getJSONObject(i));
            u.getJoinedGroups().add(g);
        }

        JSONArray moderatedGroups = obj.getJSONArray("moderated_groups");

        for (int i = 0; i < moderatedGroups.length(); i++) {
            Group g = Group.fromSmallJSON(moderatedGroups.getJSONObject(i));
            u.getModeratedGroups().add(g);
        }

        JSONArray dataTemplates = obj.getJSONArray("data_templates");

        for(int i = 0; i < dataTemplates.length(); i++) {
            DataTemplate t = DataTemplate.fromJSON(dataTemplates.getJSONObject(i));
            u.getDataTemplates().add(t);
        }

        JSONArray posts = obj.getJSONArray("posts");

        for (int i = 0; i < posts.length(); i++) {
            Post p = Post.fromJSON(posts.getJSONObject(i));
            u.getPosts().add(p);
        }

        return u;
    }

}
