package com.karacasoft.interestr.pages.search;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.karacasoft.interestr.ErrorHandler;
import com.karacasoft.interestr.FloatingActionButtonHandler;
import com.karacasoft.interestr.InterestrApplication;
import com.karacasoft.interestr.R;
import com.karacasoft.interestr.ToolbarHandler;
import com.karacasoft.interestr.network.InterestrAPI;
import com.karacasoft.interestr.network.InterestrAPIImpl;
import com.karacasoft.interestr.network.InterestrAPIResult;
import com.karacasoft.interestr.network.models.Group;
import com.karacasoft.interestr.network.models.Token;
import com.karacasoft.interestr.network.models.User;
import com.karacasoft.interestr.pages.search.SearchResultItem;
import com.karacasoft.interestr.pages.search.dummydata.SearchDummyData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnSearchFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    private View root;
    private InterestrAPI api;
    private RecyclerView searchResultList;
    private ImageButton btnSearch;
    private EditText etSearch;
    private SearchRecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<SearchResultItem> results = new ArrayList<>();

    private ErrorHandler errorHandler;
    private ToolbarHandler toolbarHandler;

    private FloatingActionButtonHandler fabHandler;
    private OnSearchFragmentInteractionListener slistener;

    /*search by gorup name or description*/
    private InterestrAPI.Callback<ArrayList<Group>> groupsCallback = new InterestrAPI.Callback<ArrayList<Group>>() {
        @Override
        public void onResult(InterestrAPIResult<ArrayList<Group>> result) {
            for (Group grp: result.get() ) {
                if(etSearch.getText().toString().equalsIgnoreCase(grp.getName().toString())
                        ||
                        grp.getDescription().toString().contains(etSearch.getText().toString())
                        ){
                    SearchResultItem s = new SearchResultItem(SearchResultItem.TYPE_GROUP);
                    s.setName(grp.getName());
                    s.setImageUrl(grp.getPictureUrl());
                }
            }
        }

        @Override
        public void onError(String error_message) {
            Log.d("search fragment", "onError: error!");
        }
    };

    /*search by username*/
    private InterestrAPI.Callback<ArrayList<User>> usersCallback = new InterestrAPI.Callback<ArrayList<User>>() {
        @Override
        public void onResult(InterestrAPIResult<ArrayList<User>> result) {
            for (User usr: result.get()){
                if(etSearch.getText().toString().equalsIgnoreCase(usr.getUsername().toString())){
                    SearchResultItem s = new SearchResultItem(SearchResultItem.TYPE_USER);
                    s.setName(usr.getUsername());
                    s.setImageUrl(null);
                }
            }
        }

        @Override
        public void onError(String error_message) {

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = ((InterestrApplication)getActivity().getApplication()).getApi();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_search, container, false);
        etSearch=root.findViewById(R.id.etSearch);
        btnSearch =root.findViewById(R.id.imgBtnSearch);
        searchResultList = root.findViewById(R.id.searchResultList);
        recyclerViewAdapter = new SearchRecyclerViewAdapter(results);

        searchResultList.setLayoutManager(new LinearLayoutManager(getContext()));
        searchResultList.setAdapter(recyclerViewAdapter);
        btnSearch.setOnClickListener(
            (view) ->slistener.onSearchButtonClicked()
        );
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSearchFragmentInteractionListener){
            slistener = (OnSearchFragmentInteractionListener) context;
        }else{
            Log.d("searchfragment", "implement OnSearchFragmentInteractionListener");
        }

        fabHandler = (FloatingActionButtonHandler) context;
        errorHandler = (ErrorHandler) context;
        toolbarHandler = (ToolbarHandler) context;
       // slistener = (OnSearchFragmentInteractionListener) getActivity();
        api = ((InterestrApplication) getActivity().getApplication()).getApi();
        fabHandler.hideFloatingActionButton();
    }

    @Override
    public void onResume() {
        super.onResume();
        api.getGroups(groupsCallback);
        api.getUsers(usersCallback);
    }

    /*private void fillList(){
        results.addAll( SearchDummyData.createDummySearchResultList());

        recyclerViewAdapter.notifyItemRangeInserted(0, results.size());
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        slistener = null;
        // TODO
    }

    public interface OnSearchFragmentInteractionListener{
        void onSearchFragmentInteraction(SearchResultItem item);
        void onSearchButtonClicked();
    }
}
