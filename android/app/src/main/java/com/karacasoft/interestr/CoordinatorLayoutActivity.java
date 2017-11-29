package com.karacasoft.interestr;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.karacasoft.interestr.pages.profile.ProfileFragment;

public class CoordinatorLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_layout);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.coordinator_layout_activity_content, ProfileFragment.newInstance(0))
                .commit();
    }
}
