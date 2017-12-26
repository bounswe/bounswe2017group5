package com.karacasoft.interestr.pages.profile;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.karacasoft.interestr.ErrorHandler;
import com.karacasoft.interestr.InterestrApplication;
import com.karacasoft.interestr.R;
import com.karacasoft.interestr.ToolbarHandler;
import com.karacasoft.interestr.network.InterestrAPI;
import com.karacasoft.interestr.network.InterestrAPIResult;
import com.karacasoft.interestr.network.models.Group;
import com.karacasoft.interestr.network.models.User;
import com.karacasoft.interestr.pages.groupdetail.GroupDetailFragment;
import com.karacasoft.interestr.pages.groups.GroupsFragment;
import com.karacasoft.interestr.pages.groups.MyGroupRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private static final String ARG_USER_ID = "user_id";
    private InterestrAPI api;
    private ErrorHandler errorHandler;
    private ToolbarHandler toolbarHandler;
    private int userId;
    private View root;
    private TextView followers;
    private TextView following;
    private TextView followingNum;
    private TextView followerNum;
    private Button btnFollow;
    private Button btnMore;
    private TextView aboutMe;

    private GroupsFragment.OnGroupsListItemClickedListener mListener;
    private RecyclerView myGroupsList;
    private MyGroupRecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<Group> myGroups = new ArrayList<>();
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
        followerNum = root.findViewById(R.id.tvFollowerNum);
        followingNum = root.findViewById(R.id.tvFollowingNum);
        aboutMe =root.findViewById(R.id.tvAbout);
        btnFollow.setVisibility(View.GONE);
        followers.setVisibility(View.GONE);
        following.setVisibility(View.GONE);
        followerNum.setVisibility(View.GONE);
        followingNum.setVisibility(View.GONE);
        aboutMe.setVisibility(View.GONE);
        registerForContextMenu(btnMore);

        myGroupsList = root.findViewById(R.id.rvMyGroups);
        recyclerViewAdapter = new MyGroupRecyclerViewAdapter(myGroups,mListener);
        myGroupsList.setLayoutManager(new LinearLayoutManager(getContext()));
        myGroupsList.setAdapter(recyclerViewAdapter);


        btnMore.setOnClickListener((view -> {
            if(getActivity() != null) {
                getActivity().openContextMenu(btnMore);
            }else{
                Log.d("profile", "onCreateView: activity is null ");
            }

        }));
        /*btnFollow.setOnClickListener(view -> {
            //todo follow user
        });*/

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        api = ((InterestrApplication)getActivity().getApplication()).getApi();
        if(getArguments()!= null){
            userId = getArguments().getInt(ARG_USER_ID);
        }else{
            Log.d("profile", "onAttach: error on attach");
        }
        errorHandler = (ErrorHandler) context;
        toolbarHandler = (ToolbarHandler) context;

        fillProfileInfo();

    }

    private void fillProfileInfo() {
        api.getProfile(new InterestrAPI.Callback<User>() {
            @Override
            public void onResult(InterestrAPIResult<User> result) {
                Log.d("profile","on result");
                User user = result.get();
                myGroups.clear();
                myGroups.addAll(result.get().getJoinedGroups());
                //todo
            }

            @Override
            public void onError(String error_message)
            {
                Log.d("error", "onError: profile");
                //errorHandler.onError(error_message);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        if(getActivity() != null) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.context_menu_user_options, menu);
        }

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_report) {
            // TODO report action
            Snackbar.make(getView(), R.string.user_reported, Snackbar.LENGTH_SHORT)
                .show();
            return true;
        }
        return super.onContextItemSelected(item);
    }
}
