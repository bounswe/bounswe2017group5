package com.karacasoft.interestr.network;

import com.karacasoft.interestr.network.models.Group;
import com.karacasoft.interestr.network.models.User;

import java.util.ArrayList;

import okhttp3.Response;

/**
 * Created by karacasoft on 30.10.2017.
 */

public interface InterestrAPI {

    public void login(String username, String password, Callback<User> callback);

    public void getGroups(Callback<ArrayList<Group>> callback);

    public void getGroupDetail(int group_id, Callback<Group> callback);

    public interface Callback<T extends Object> {
        public void onResult(InterestrAPIResult<T> result);
        public void onError(String error_message);
    }
}
