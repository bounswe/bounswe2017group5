package com.karacasoft.interestr;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.karacasoft.interestr.pages.groupdetail.GroupDetailFragment;
import com.karacasoft.interestr.pages.profile.ProfileFragment;

public class CoordinatorLayoutActivity extends AppCompatActivity {

    public static final String ACTION_DISPLAY_USER ="com.karacasoft.interestr.display_user";
    public static final String EXTRA_USER_ID = "com.karacasoft.interestr.extra_user_id";

    public static final String ACTION_DISPLAY_GROUP ="com.karacasoft.interestr.display_group";
    public static final String EXTRA_GROUP_ID = "com.karacasoft.interestr.extra_group_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_layout);

        FragmentManager fm = getSupportFragmentManager();
        if(getIntent()!=null && getIntent().getAction()!=null){
            if(getIntent().getAction().equals(ACTION_DISPLAY_USER)){
                fm.beginTransaction()
                        .replace(R.id.coordinator_layout_activity_content, ProfileFragment.newInstance(getIntent().getIntExtra(EXTRA_USER_ID,0)))
                        .commit();
            }else if(getIntent().getAction().equals(ACTION_DISPLAY_GROUP)){
                fm.beginTransaction()
                        .replace(R.id.coordinator_layout_activity_content, GroupDetailFragment.newInstance(getIntent().getIntExtra(EXTRA_GROUP_ID,0)))
                        .commit();
            }
        }else{
            //todo remove this else after putting a link to group page it is just for debug
            fm.beginTransaction()
                    .replace(R.id.coordinator_layout_activity_content,GroupDetailFragment.newInstance(0))
                    .commit();

        }
    }
}
