package com.karacasoft.interestr.network;

import com.karacasoft.interestr.network.models.Group;

import java.util.ArrayList;

/**
 * Created by karacasoft on 30.10.2017.
 */

public interface InterestrAPI {

    public void getGroups(Callback<ArrayList<Group>> callback);

    public interface Callback<T extends Object> {
        public void onResult(InterestrAPIResult<T> result);
    }
}
