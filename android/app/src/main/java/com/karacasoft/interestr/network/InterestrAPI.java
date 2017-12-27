package com.karacasoft.interestr.network;

import com.karacasoft.interestr.network.models.DataTemplate;
import com.karacasoft.interestr.network.models.Group;
import com.karacasoft.interestr.network.models.Post;
import com.karacasoft.interestr.network.models.Tag;
import com.karacasoft.interestr.network.models.Token;
import com.karacasoft.interestr.network.models.User;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by karacasoft on 30.10.2017.
 */

public interface InterestrAPI {

    public void setLimit(int limit);
    public void setOffset(int offset);

    public void login(String username, String password, Callback<Token> callback);
    public void signup(User user, Callback<User> callback);

    public void getUsers(Callback<ArrayList<User>> callback);

    public void createGroup(Group group, Callback<Group> callback);
    public void getGroups(Callback<ArrayList<Group>> callback);
    public void getGroupDetail(int group_id, Callback<Group> callback);

    public void joinGroup(int group_id, Callback<Boolean> callback);
    public void leaveGroup(int group_id, Callback<Boolean> callback);

    public void getPosts(int group_id, Callback<ArrayList<Post>> callback);
    public void createPost(Post p, Callback<Post> callback);

    public void getDataTemplates(int group_id, Callback<ArrayList<DataTemplate>> callback);
    public void createDataTemplate(DataTemplate dataTemplate, Callback<DataTemplate> callback);

    public void createTag(Tag tag, Callback<Tag> callback);

    public void getRecommendedPosts(Callback<ArrayList<Post>> callback);
    public void getRecommendedGroups(Callback<ArrayList<Group>> callback);

    public void logout();
    public void authenticate(Token token);

    public boolean isLoggedIn();


    public void getProfile(Callback<User> callback);

    public interface Callback<T> {
        public void onResult(InterestrAPIResult<T> result);
        public void onError(String error_message);
    }
}
