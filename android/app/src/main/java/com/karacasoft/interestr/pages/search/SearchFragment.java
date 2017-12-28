package com.karacasoft.interestr.pages.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.karacasoft.interestr.ErrorHandler;
import com.karacasoft.interestr.FloatingActionButtonHandler;
import com.karacasoft.interestr.InterestrApplication;
import com.karacasoft.interestr.R;
import com.karacasoft.interestr.ToolbarHandler;
import com.karacasoft.interestr.network.InterestrAPI;
import com.karacasoft.interestr.network.InterestrAPIResult;
import com.karacasoft.interestr.network.models.Group;
import com.karacasoft.interestr.network.models.User;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnSearchFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    private InterestrAPI api;

    private RecyclerView searchResultList;
    private EditText etSearch;
    private SearchRecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<SearchResultItem> results = new ArrayList<>();

    private ErrorHandler errorHandler;
    private ToolbarHandler toolbarHandler;

    private FloatingActionButtonHandler fabHandler;
    private OnSearchFragmentInteractionListener onSearchFragmentInteractionListener;

    private boolean isSearching = false;
    private boolean isQueryChanged = false;
    private String query = "";


    /*search by group name or description*/
    private InterestrAPI.Callback<ArrayList<Group>> groupsCallback = new InterestrAPI.Callback<ArrayList<Group>>() {
        @Override
        public void onResult(InterestrAPIResult<ArrayList<Group>> result) {
            int size = 0;
            for (Group grp : result.get() ) {
                SearchResultItem s = new SearchResultItem(SearchResultItem.TYPE_GROUP);
                s.setName(grp.getName());
                s.setImageUrl(grp.getPictureUrl());
                s.setItemId(grp.getId());
                results.add(s);
                size++;
            }
            recyclerViewAdapter.notifyItemRangeInserted(0, size);
        }

        @Override
        public void onError(String error_message) {
            errorHandler.onError(error_message);
        }
    };

    /*search by username*/
    private InterestrAPI.Callback<ArrayList<User>> usersCallback = new InterestrAPI.Callback<ArrayList<User>>() {
        @Override
        public void onResult(InterestrAPIResult<ArrayList<User>> result) {
            int size = 0;
            for (User usr: result.get()){
                SearchResultItem s = new SearchResultItem(SearchResultItem.TYPE_USER);
                s.setName(usr.getUsername());
                s.setImageUrl(null);
                s.setItemId(usr.getId());

                results.add(s);
                size++;
            }
            recyclerViewAdapter.notifyItemRangeInserted(0, size);

            isSearching = false;
            if(isQueryChanged) {
                updateSearch();
            }
        }

        @Override
        public void onError(String error_message) {
            errorHandler.onError(error_message);
        }
    };

    public SearchFragment(){
        //empty constructor
    }
    public static SearchFragment newInstance(){
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = ((InterestrApplication)getActivity().getApplication()).getApi();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        etSearch= root.findViewById(R.id.etSearch);
        searchResultList = root.findViewById(R.id.searchResultList);
        recyclerViewAdapter = new SearchRecyclerViewAdapter(results);

        searchResultList.setLayoutManager(new LinearLayoutManager(getContext()));
        searchResultList.setAdapter(recyclerViewAdapter);

        recyclerViewAdapter.setOnSearchItemClickListener(item ->
                onSearchFragmentInteractionListener.onSearchFragmentInteraction(item));

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                query = editable.toString();
                if(!isSearching) {
                    updateSearch();
                } else {
                    isQueryChanged = true;
                }
            }
        });


        return root;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        api = ((InterestrApplication) getActivity().getApplication()).getApi();

        onSearchFragmentInteractionListener = (OnSearchFragmentInteractionListener) context;
        fabHandler = (FloatingActionButtonHandler) context;
        errorHandler = (ErrorHandler) context;
        toolbarHandler = (ToolbarHandler) context;

        fabHandler.hideFloatingActionButton();
    }

    @Override
    public void onResume() {
        super.onResume();

        updateSearch();
    }

    private void updateSearch() {
        isQueryChanged = false;
        isSearching = true;

        int size = results.size();
        results.clear();

        recyclerViewAdapter.notifyItemRangeRemoved(0, size);

        api.searchGroups(query, groupsCallback);
        api.searchUsers(query, usersCallback);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onSearchFragmentInteractionListener = null;
    }

    public interface OnSearchFragmentInteractionListener{
        void onSearchFragmentInteraction(SearchResultItem item);
    }
}
