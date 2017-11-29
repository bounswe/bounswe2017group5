package com.karacasoft.interestr.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.karacasoft.interestr.R;
import com.karacasoft.interestr.network.InterestrAPI;
import com.karacasoft.interestr.network.InterestrAPIImpl;
import com.karacasoft.interestr.network.InterestrAPIResult;
import com.karacasoft.interestr.network.models.Group;
import com.karacasoft.interestr.network.models.Token;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        InterestrAPI api = new InterestrAPIImpl(this);

        api.login("KaracaSoft", "Mahmut95", new InterestrAPI.Callback<Token>() {
            @Override
            public void onResult(InterestrAPIResult<Token> result) {
                api.authenticate(result.get());
            }

            @Override
            public void onError(String error_message) {
                Log.d("Test", "NOPE");
            }
        });

//        api.getGroups(new InterestrAPI.Callback<ArrayList<Group>>() {
//            @Override
//            public void onResult(InterestrAPIResult<ArrayList<Group>> result) {
//                Log.d("TestGroup", "OK");
//            }
//
//            @Override
//            public void onError(String error_message) {
//                Log.d("TestGroup", "NOPE");
//            }
//        });

//        Group group = new Group();
//        group.setName("Heavy Metal fans");
//        group.setDescription("Heavy Metal fans of Kutahya");
//        group.setLocation("Kutahya");
//
//        api.createGroup(group, new InterestrAPI.Callback<Group>() {
//            @Override
//            public void onResult(InterestrAPIResult<Group> result) {
//                Log.d("TestGroup", "OK");
//            }
//
//            @Override
//            public void onError(String error_message) {
//                Log.d("TestGroup", "NOPE");
//            }
//        });

//        api.getGroupDetail(2, new InterestrAPI.Callback<Group>() {
//            @Override
//            public void onResult(InterestrAPIResult<Group> result) {
//                Log.d("TestGroupDet", "OK");
//            }
//
//            @Override
//            public void onError(String error_message) {
//                Log.d("TestGroupDet", "NOPE");
//            }
//        });

        api.joinGroup(1, new InterestrAPI.Callback<Boolean>() {
            @Override
            public void onResult(InterestrAPIResult<Boolean> result) {
                Log.d("TestGroupJoin", "OK");
            }

            @Override
            public void onError(String error_message) {
                Log.d("TestGroupJoin", "NOPE");
            }
        });

    }
}
