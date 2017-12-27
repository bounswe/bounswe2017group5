package com.karacasoft.interestr.pages.profile;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.karacasoft.interestr.pages.groups.GroupsFragment;
import com.karacasoft.interestr.pages.groups.GroupRecyclerViewAdapter;
import com.karacasoft.interestr.pages.newsfeed.NewsFeedFragment;
import com.karacasoft.interestr.pages.newsfeed.NewsFeedListFragment;

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
    private Button btnMore;
    private TextView aboutMe;

    private GroupsFragment.OnGroupsListItemClickedListener mListener;

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
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        btnMore = root.findViewById(R.id.btnMore);
        registerForContextMenu(btnMore);


        ViewPager viewPager = root.findViewById(R.id.user_lists_view_pager);

        ProfilePagerAdapter adapter = new ProfilePagerAdapter(getContext(), getFragmentManager());

        viewPager.setAdapter(adapter);

        btnMore.setOnClickListener((view -> {
            if(getActivity() != null) {
                getActivity().openContextMenu(btnMore);
            }else{
                Log.d("profile", "onCreateView: activity is null ");
            }

        }));

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
    }

    @Override
    public void onResume() {
        super.onResume();

        fillProfileInfo();
    }

    private void fillProfileInfo() {
        api.getProfile(new InterestrAPI.Callback<User>() {
            @Override
            public void onResult(InterestrAPIResult<User> result) {
                User u = result.get();

                toolbarHandler.setTitle(u.getUsername());
            }

            @Override
            public void onError(String error_message) {
                errorHandler.onError(error_message);
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

    public static class ProfilePagerAdapter extends FragmentStatePagerAdapter {

        private Context context;

        private NewsFeedListFragment[] fragments = new NewsFeedListFragment[3];

        ProfilePagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            this.context = context;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0) {
                return "Posts";
            } else if(position == 1) {
                return "Joined Groups";
            } else if(position == 2) {
                return "Moderated Groups";
            }
            return super.getPageTitle(position);
        }

        @Override
        public Fragment getItem(int position) {
            if(fragments[position] == null) {
                fragments[position] = NewsFeedListFragment.newInstance(position + 2);
            }
            return fragments[position];
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}
