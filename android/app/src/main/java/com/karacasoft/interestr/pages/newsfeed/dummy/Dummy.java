package com.karacasoft.interestr.pages.newsfeed.dummy;

import com.karacasoft.interestr.network.models.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Dummy data generator for Feed and Recommendations
 *
 * Created by karacasoft on 13.12.2017.
 */

public class Dummy {


    private static ArrayList<Post> generateDummyData(String name) throws JSONException {
        ArrayList<Post> posts = new ArrayList<>();

        for (int i = 1; i < 7; i++) {
            Post p = new Post();
            p.setId(i);
            p.setOwner(i);
            p.setOwnerName(name + " Owner " + i);
            p.setGroupId(i);
            p.setGroupName(name + " Group " + i);
            p.setDataTemplateId(i);
            p.setDataTemplateName(name + " DT name " + i);

            JSONArray array = new JSONArray();
            for (int j = 0; j < 3; j++) {
                JSONObject field = new JSONObject();

                field.put("question", i + ") Question " + j);
                field.put("response", i + ") Response " + j);

                array.put(field);
            }

            p.setData(array);

            posts.add(p);
        }


        return posts;
    }

    public static ArrayList<Post> getDummyFeedData() throws JSONException {
        return generateDummyData("Feed");
    }

    public static ArrayList<Post> getDummyRecommendationData() throws JSONException {
        return generateDummyData("Recommendation");
    }

}
