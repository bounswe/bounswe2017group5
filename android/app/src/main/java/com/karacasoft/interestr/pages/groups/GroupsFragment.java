package com.karacasoft.interestr.pages.groups;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.karacasoft.interestr.ErrorHandler;
import com.karacasoft.interestr.FloatingActionButtonHandler;
import com.karacasoft.interestr.FloatingActionsMenuHandler;
import com.karacasoft.interestr.R;
import com.karacasoft.interestr.network.InterestrAPI;
import com.karacasoft.interestr.network.InterestrAPIImpl;
import com.karacasoft.interestr.network.InterestrAPIResult;
import com.karacasoft.interestr.network.models.Group;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnGroupsListItemClickedListener}
 * interface.
 */
public class GroupsFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnGroupsListItemClickedListener mListener;
    private OnCreateGroupClicked onCreateGroupClicked;

    private ArrayList<Group> dataset = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyGroupRecyclerViewAdapter groupRecyclerViewAdapter;

    private InterestrAPI api;

    private boolean detached = false;

    private FloatingActionButtonHandler fabHandler;
    private FloatingActionsMenuHandler famHandler;

    private InterestrAPI.Callback<ArrayList<Group>> groupsCallback = new InterestrAPI.Callback<ArrayList<Group>>() {
        @Override
        public void onResult(InterestrAPIResult result) {
            if(detached) return;

            dataset.clear();

            //noinspection unchecked
            dataset.addAll((ArrayList<Group>) result.get());

            // TODO handle this via inserted and replaced
            groupRecyclerViewAdapter.notifyDataSetChanged();

        }

        @Override
        public void onError(String error_message) {
            Log.e("Groups Fragment", "Hata olmuş, lel :" + error_message);
        }
    };
    private ErrorHandler errorHandler;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GroupsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static GroupsFragment newInstance(int columnCount) {
        GroupsFragment fragment = new GroupsFragment();
        Bundle args = new Bundle();
        Log.d("Group Fragment","new group fragment instance created");
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        api = new InterestrAPIImpl(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(groupRecyclerViewAdapter = new MyGroupRecyclerViewAdapter(dataset, mListener));
        }
        return view;
    }

    private void setupFloatingActionButton(FloatingActionButton fab) {
        fab.setImageResource(R.drawable.ic_add_white_24dp);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGroupsListItemClickedListener) {
            mListener = (OnGroupsListItemClickedListener) context;
            onCreateGroupClicked = (OnCreateGroupClicked) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnGroupsListItemClickedListener");
        }

        errorHandler = (ErrorHandler) context;

        fabHandler = (FloatingActionButtonHandler) context;
        famHandler = (FloatingActionsMenuHandler) context;

        fabHandler.getFloatingActionButton().setOnClickListener(
                (view) -> onCreateGroupClicked.onCreateGroupClicked());

        fabHandler.hideFloatingActionButton();
        famHandler.hideFloatingActionsMenu();

        setupFloatingActionButton(fabHandler.getFloatingActionButton());
        fabHandler.showFloatingActionButton();
    }

    @Override
    public void onResume() {
        super.onResume();
        detached = false;

        api.getGroups(groupsCallback);
    }

    @Override
    public void onPause() {
        super.onPause();
        detached = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

    }

    public interface OnGroupsListItemClickedListener {
        void onGroupsListItemClicked(Group item);
    }

    public interface OnCreateGroupClicked {
        void onCreateGroupClicked();
    }

}
