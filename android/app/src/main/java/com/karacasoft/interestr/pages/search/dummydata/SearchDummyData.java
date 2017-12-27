package com.karacasoft.interestr.pages.search.dummydata;

import android.util.Log;

import com.karacasoft.interestr.network.InterestrAPI;
import com.karacasoft.interestr.network.InterestrAPIResult;
import com.karacasoft.interestr.network.models.Group;
import com.karacasoft.interestr.network.models.User;
import com.karacasoft.interestr.pages.search.SearchResultItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aras on 28.11.2017.
 */

public class SearchDummyData {
    public static int typeGroup = 0;
    public static int typeUser = 1;
    private InterestrAPI api;
    private static ArrayList<Group> groups;

    public static List<SearchResultItem> createDummySearchResultList(){
        List<SearchResultItem> dummySearchResultList = new ArrayList<>();
        dummySearchResultList.add(new SearchResultItem(typeGroup,"recipe","https://pbs.twimg.com/profile_images/2885812159/8e586603687c58acd982e2ae0544095e_400x400.jpeg"));
        dummySearchResultList.add(new SearchResultItem(typeUser,"mahmut","http://images.all-free-download.com/images/graphiclarge/harry_potter_icon_6825007.jpg"));
        return dummySearchResultList;
    }
    /*
    public static List<SearchResultItem> createSearchResult(String searchKey){
        List<SearchResultItem> results = new ArrayList<>();
        List<User> users = api.getGroups(new InterestrAPI.Callback<ArrayList<Group>>() {
            @Override
            public void onResult(InterestrAPIResult<ArrayList<Group>> result) {
                groups.clear();
                groups.addAll((ArrayList<Group>) result.get());
            }

            @Override
            public void onError(String error_message) {

            }
        });
    }*/
}
