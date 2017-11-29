package com.karacasoft.interestr.pages.profile;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.karacasoft.interestr.R;
import com.karacasoft.interestr.pages.groupdetail.GroupDetailFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private static final String ARG_USER_ID = "user_id";
    private int userId;
    private View root;
    private TextView followers;
    private TextView following;
    private Button btnFollow;
    private Button btnMore;
    private TextView aboutMe;
    //todo group recycler reuse
    //todo context menu add

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(int userId){
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID,userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt(ARG_USER_ID);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_profile, container, false);
        btnFollow = root.findViewById(R.id.btnFollowUser);
        btnMore = root.findViewById(R.id.btnMore);
        followers = root.findViewById(R.id.tvFollowerNum);
        following = root.findViewById(R.id.tvFollowingNum);
        btnMore.setOnClickListener((view -> {
            //todo create context menu
        }));
        btnFollow.setOnClickListener(view -> {
            //todo follow user
        });

        return root;
    }

}
