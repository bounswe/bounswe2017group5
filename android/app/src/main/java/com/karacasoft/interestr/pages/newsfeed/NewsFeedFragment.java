package com.karacasoft.interestr.pages.newsfeed;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karacasoft.interestr.ErrorHandler;
import com.karacasoft.interestr.R;

public class NewsFeedFragment extends Fragment {


    private ErrorHandler errorHandler;

    public static NewsFeedFragment newInstance() {
        NewsFeedFragment newsFeedFragment = new NewsFeedFragment();

        Bundle args = new Bundle();
        newsFeedFragment.setArguments(args);

        return newsFeedFragment;
    }

    public NewsFeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news_feed, container, false);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        errorHandler = (ErrorHandler) context;
    }
}
