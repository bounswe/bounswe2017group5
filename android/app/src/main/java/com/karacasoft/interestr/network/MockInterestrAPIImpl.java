package com.karacasoft.interestr.network;

import com.karacasoft.interestr.network.models.DataTemplate;
import com.karacasoft.interestr.network.models.Group;
import com.karacasoft.interestr.network.models.Post;
import com.karacasoft.interestr.network.models.Tag;
import com.karacasoft.interestr.network.models.Token;
import com.karacasoft.interestr.network.models.User;

import java.util.ArrayList;

/**
 * Created by karacasoft on 30.11.2017.
 */

public class MockInterestrAPIImpl implements InterestrAPI {
    @Override
    public void setLimit(int limit) {

    }

    @Override
    public void setOffset(int offset) {

    }

    @Override
    public void login(String username, String password, Callback<Token> callback) {
        if(username.equals("Error"))
        {
            callback.onError("I am error");
            return;
        }
        Token token = new Token("CustomTokenKey");
        InterestrAPIResult<Token> result = new InterestrAPIResult<>(token);

        callback.onResult(result);
    }

    @Override
    public void signup(User user, Callback<User> callback) {

    }

    @Override
    public void getUsers(Callback<ArrayList<User>> callback) {

    }

    @Override
    public void createGroup(Group group, Callback<Group> callback) {

    }

    @Override
    public void getGroups(Callback<ArrayList<Group>> callback) {

    }

    @Override
    public void getGroupDetail(int group_id, Callback<Group> callback) {

    }

    @Override
    public void joinGroup(int group_id, Callback<Boolean> callback) {

    }

    @Override
    public void leaveGroup(int group_id, Callback<Boolean> callback) {

    }

    @Override
    public void getPosts(int group_id, Callback<ArrayList<Post>> callback) {

    }

    @Override
    public void createPost(Post p, Callback<Post> callback) {

    }

    @Override
    public void getDataTemplates(int group_id, Callback<ArrayList<DataTemplate>> callback) {

    }

    @Override
    public void createDataTemplate(DataTemplate dataTemplate, Callback<DataTemplate> callback) {

    }

    @Override
    public void createTag(Tag tag, Callback<Tag> callback) {

    }

    @Override
    public void getRecommendedPosts(Callback<ArrayList<Post>> callback) {

    }

    @Override
    public void getRecommendedGroups(Callback<ArrayList<Group>> callback) {

    }

    @Override
    public void logout() {

    }

    @Override
    public void authenticate(Token token) {

    }

    @Override
    public boolean isLoggedIn() {
        return false;
    }

    @Override
    public void getProfile(Callback<User> callback) {

    }
}
