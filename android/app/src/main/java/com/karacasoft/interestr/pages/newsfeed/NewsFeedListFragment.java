package com.karacasoft.interestr.pages.newsfeed;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karacasoft.interestr.CoordinatorLayoutActivity;
import com.karacasoft.interestr.ErrorHandler;
import com.karacasoft.interestr.InterestrApplication;
import com.karacasoft.interestr.R;
import com.karacasoft.interestr.network.InterestrAPI;
import com.karacasoft.interestr.network.InterestrAPIResult;
import com.karacasoft.interestr.network.models.Group;
import com.karacasoft.interestr.network.models.Post;
import com.karacasoft.interestr.pages.groupdetail.PostRecyclerViewAdapter;
import com.karacasoft.interestr.pages.groups.GroupRecyclerViewAdapter;
import com.karacasoft.interestr.pages.newsfeed.dummy.Dummy;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFeedListFragment extends Fragment {

    public static final int DATA_LOCATION_POSTS = 0;
    public static final int DATA_LOCATION_GROUPS = 1;

    public static final String ARG_DATA_LOCATION = "com.karacasoft.interestr.data_location";

    private RecyclerView listView;
    private PostRecyclerViewAdapter postAdapter;
    private GroupRecyclerViewAdapter groupAdapter;

    private ArrayList<Post> posts = new ArrayList<>();
    private ArrayList<Group> groups = new ArrayList<>();

    private int dataLocation = 0;

    private InterestrAPI api;

    private ErrorHandler errorHandler;

    public static NewsFeedListFragment newInstance(int dataLocation) {
        NewsFeedListFragment fragment = new NewsFeedListFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_DATA_LOCATION, dataLocation);
        fragment.setArguments(args);

        return fragment;
    }

    public NewsFeedListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getContext() != null) {
            api = ((InterestrApplication) getContext().getApplicationContext()).getApi();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news_feed_list, container, false);

        listView = v.findViewById(R.id.news_feed_list);
        postAdapter = new PostRecyclerViewAdapter(posts);
        groupAdapter = new GroupRecyclerViewAdapter(groups, item -> {
            Intent i = new Intent(getContext(), CoordinatorLayoutActivity.class);
            i.setAction(CoordinatorLayoutActivity.ACTION_DISPLAY_GROUP);
            i.putExtra(CoordinatorLayoutActivity.EXTRA_GROUP_ID, item.getId());
            startActivity(i);
        });

        listView.setLayoutManager(new LinearLayoutManager(getContext()));


        if(dataLocation == 0) {
            listView.setAdapter(postAdapter);
        } else if(dataLocation == 1) {
            listView.setAdapter(groupAdapter);
        }

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(getArguments() != null) {
            dataLocation = getArguments().getInt(ARG_DATA_LOCATION);
        }


        errorHandler = (ErrorHandler) context;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(dataLocation == DATA_LOCATION_POSTS) {
            int size = posts.size();
            posts.clear();

            postAdapter.notifyItemRangeRemoved(0, size);

            api.getRecommendedPosts(new InterestrAPI.Callback<ArrayList<Post>>() {
                @Override
                public void onResult(InterestrAPIResult<ArrayList<Post>> result) {
                    posts.addAll(result.get());

                    postAdapter.notifyItemRangeInserted(0, posts.size());
                }

                @Override
                public void onError(String error_message) {
                    errorHandler.onError(error_message);
                }
            });

        } else if(dataLocation == DATA_LOCATION_GROUPS) {
            int size = groups.size();
            groups.clear();

            groupAdapter.notifyItemRangeRemoved(0, size);

            api.getRecommendedGroups(new InterestrAPI.Callback<ArrayList<Group>>() {
                @Override
                public void onResult(InterestrAPIResult<ArrayList<Group>> result) {
                    groups.addAll(result.get());

                    groupAdapter.notifyItemRangeInserted(0, groups.size());
                }

                @Override
                public void onError(String error_message) {
                    errorHandler.onError(error_message);
                }
            });
        }
    }
}
