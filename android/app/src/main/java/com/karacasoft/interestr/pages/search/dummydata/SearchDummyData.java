package com.karacasoft.interestr.pages.search.dummydata;

import com.karacasoft.interestr.pages.search.SearchResultItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aras on 28.11.2017.
 */

public class SearchDummyData {
    public static int typeGroup = 0;
    public static int typeUser = 1;

    public static List<SearchResultItem> createDummySearchResultList(){
        List<SearchResultItem> dummySearchResultList = new ArrayList<>();
        dummySearchResultList.add(new SearchResultItem(typeGroup,"recipe","https://pbs.twimg.com/profile_images/2885812159/8e586603687c58acd982e2ae0544095e_400x400.jpeg"));
        dummySearchResultList.add(new SearchResultItem(typeUser,"mahmut","http://images.all-free-download.com/images/graphiclarge/harry_potter_icon_6825007.jpg"));
        return dummySearchResultList;
    }
}
