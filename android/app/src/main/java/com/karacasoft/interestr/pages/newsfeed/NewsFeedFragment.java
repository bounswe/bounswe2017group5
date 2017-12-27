package com.karacasoft.interestr.pages.newsfeed;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karacasoft.interestr.ErrorHandler;
import com.karacasoft.interestr.R;
import com.karacasoft.interestr.ToolbarHandler;

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

        ViewPager viewPager = v.findViewById(R.id.feed_and_recommendations_pager);
        NewsFeedPagerAdapter newsFeedPagerAdapter = new NewsFeedPagerAdapter(getContext(), getFragmentManager());

        viewPager.setAdapter(newsFeedPagerAdapter);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        errorHandler = (ErrorHandler) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();

        errorHandler = null;
    }

    public static class NewsFeedPagerAdapter extends FragmentStatePagerAdapter {

        private Context context;

        private NewsFeedListFragment[] fragments = new NewsFeedListFragment[2];


        NewsFeedPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            this.context = context;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0) {
                return context.getString(R.string.posts);
            } else if(position == 1) {
                return context.getString(R.string.groups);
            }
            return super.getPageTitle(position);
        }

        @Override
        public Fragment getItem(int position) {
            if(fragments[position] == null) {
                fragments[position] = NewsFeedListFragment.newInstance(position);
            }
            return fragments[position];
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
