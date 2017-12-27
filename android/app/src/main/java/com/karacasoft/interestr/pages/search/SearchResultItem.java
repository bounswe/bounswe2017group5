package com.karacasoft.interestr.pages.search;

/**
 * Created by aras on 28.11.2017.
 */

public class SearchResultItem  {

    public static final int TYPE_GROUP = 0;
    public static final int TYPE_USER = 1;

    private int type;//group=0; user=1;
    private String name;//item name
    private String imageUrl;//item image url


    public SearchResultItem (int type, String name, String imageUrl){
        this.type = type;
        this.name = name;
        this.imageUrl = imageUrl;
    }
    public SearchResultItem (int type, String name){
        this.type = type;
        this.name = name;
        this.imageUrl = null;//no image provided
    }
    public SearchResultItem (int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}