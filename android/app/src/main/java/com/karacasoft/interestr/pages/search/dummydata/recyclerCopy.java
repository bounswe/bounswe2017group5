//package com.karacasoft.interestr.pages.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.karacasoft.interestr.R;
import com.karacasoft.interestr.network.models.Group;
import com.karacasoft.interestr.network.models.User;
import com.karacasoft.interestr.pages.search.SearchResultItem;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Esra Aydemir on 23.11.2017.
 */
/*
public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder>{
    private List<SearchResultItem> searchResults;//type - group/user
    private ImageLoader imageLoader;
    private SearchFragment.OnSearchFragmentInteractionListener slistener;


    public SearchRecyclerViewAdapter(List<SearchResultItem> searchResults) {
        this.searchResults = searchResults;
        imageLoader = ImageLoader.getInstance();
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.item = searchResults.get(position);
        //todo add type as string

        imageLoader.displayImage(searchResults.get(position).getImageUrl(),holder.searchImageView);
        holder.searchTextView.setText(searchResults.get(position).getName());
        if(searchResults.get(position).getType() ==1){
            holder.searchTypeView.setText("user");
        }else if(searchResults.get(position).getType() ==0){
            holder.searchTypeView.setText("group");
        }else{
            holder.searchTypeView.setText("");//todo set maybe or give exception
        }

        holder.searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(slistener!=null){
                    slistener.onSearchFragmentInteraction(holder.item);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View searchView;
        public final ImageView searchImageView;
        public final TextView searchTextView;
        public final TextView searchTypeView;

        public SearchResultItem item;

        public ViewHolder(View view) {
            super(view);
            searchView = view;
            searchTextView = view.findViewById(R.id.textSearchItem);
            searchImageView = view.findViewById(R.id.imgSearchItem);
            searchTypeView = view.findViewById(R.id.tvSearchItemType);
        }
    }

}


*/