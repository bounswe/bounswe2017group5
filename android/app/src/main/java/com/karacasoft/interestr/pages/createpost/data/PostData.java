package com.karacasoft.interestr.pages.createpost.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by karacasoft on 24.12.2017.
 */

public class PostData {
    private boolean checkboxMarked = false;
    private ArrayList<String> multipleSelSelectedItems = new ArrayList<>();

    private String question;
    private String response;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ArrayList<String> getMultipleSelSelectedItems() {
        return multipleSelSelectedItems;
    }

    public void setCheckboxMarked(boolean checkboxMarked) {
        this.checkboxMarked = checkboxMarked;
    }

    public boolean isCheckboxMarked() {
        return checkboxMarked;
    }

    public static ArrayList<PostData> listFromJSONArray(JSONArray array) throws JSONException {
        int length = array.length();
        ArrayList<PostData> postDataList = new ArrayList<>(length);

        for (int i = 0; i < length; i++) {
            JSONObject data = array.getJSONObject(i);

            PostData postData = fromJSON(data);

            postDataList.add(postData);
        }

        return postDataList;
    }

    public static JSONArray toJSONArrayFromList(ArrayList<PostData> list) throws JSONException {
        JSONArray array = new JSONArray();

        for (PostData data : list) {
            array.put(data.toJSON());
        }

        return array;
    }

    public static PostData fromJSON(JSONObject data) throws JSONException {
        PostData postData = new PostData();
        postData.setQuestion(data.getString("question"));
        postData.setResponse(data.getString("response"));

        return postData;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject object = new JSONObject();

        object.put("question", getQuestion());
        object.put("response", getResponse());

        return object;
    }
}
