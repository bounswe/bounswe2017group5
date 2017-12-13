package com.karacasoft.interestr.pages.newsfeed;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karacasoft.interestr.R;
import com.karacasoft.interestr.network.models.Post;
import com.karacasoft.interestr.pages.groupdetail.PostRecyclerViewAdapter;
import com.karacasoft.interestr.pages.newsfeed.dummy.Dummy;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFeedListFragment extends Fragment {

    public static final int DATA_LOCATION_FEED = 0;
    public static final int DATA_LOCATION_RECOMMENDATIONS = 1;

    public static final String ARG_DATA_LOCATION = "com.karacasoft.interestr.data_location";

    private RecyclerView listView;
    private PostRecyclerViewAdapter adapter;

    private ArrayList<Post> posts = new ArrayList<>();

    private int dataLocation = 0;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news_feed_list, container, false);

        listView = v.findViewById(R.id.news_feed_list);
        adapter = new PostRecyclerViewAdapter(posts);

        listView.setLayoutManager(new LinearLayoutManager(getContext()));

        listView.setAdapter(adapter);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(getArguments() != null) {
            dataLocation = getArguments().getInt(ARG_DATA_LOCATION);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        int size = posts.size();
        posts.clear();

        adapter.notifyItemRangeRemoved(0, size);

        if(dataLocation == DATA_LOCATION_FEED) {
            try {
                posts.addAll(Dummy.getDummyFeedData());

                adapter.notifyItemRangeInserted(0, posts.size());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if(dataLocation == DATA_LOCATION_RECOMMENDATIONS) {
            try {
                posts.addAll(Dummy.getDummyRecommendationData());

                adapter.notifyItemRangeInserted(0, posts.size());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
