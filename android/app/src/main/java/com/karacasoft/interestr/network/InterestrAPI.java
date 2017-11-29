package com.karacasoft.interestr.network;

import com.karacasoft.interestr.network.models.DataTemplate;
import com.karacasoft.interestr.network.models.Group;
import com.karacasoft.interestr.network.models.Post;
import com.karacasoft.interestr.network.models.Token;
import com.karacasoft.interestr.network.models.User;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by karacasoft on 30.10.2017.
 */

public interface InterestrAPI {

    public void login(String username, String password, Callback<Token> callback);
    public void signup(String username, String email, String pass1, String pass2, Callback<User> callback);

    public void createGroup(Group group, Callback<Group> callback);
    public void getGroups(Callback<ArrayList<Group>> callback);
    public void getGroupDetail(int group_id, Callback<Group> callback);

    public void joinGroup(int group_id, Callback<Boolean> callback);
    public void leaveGroup(int group_id, Callback<Boolean> callback);

    public void getPosts(int group_id, Callback<ArrayList<Post>> callback);
    public void createPost(Post p, Callback<Post> callback);

    public void getDataTemplates(int group_id, Callback<ArrayList<DataTemplate>> callback);
    public void createDataTemplate(DataTemplate dataTemplate, Callback<DataTemplate> callback);

    public void logout();
    public void authenticate(Token token);

    public interface Callback<T extends Object> {
        public void onResult(InterestrAPIResult<T> result);
        public void onError(String error_message);
    }
}
