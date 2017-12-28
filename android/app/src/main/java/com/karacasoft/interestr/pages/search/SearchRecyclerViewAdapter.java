package com.karacasoft.interestr.pages.search;

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

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder>{
    private List<SearchResultItem> searchResults;//type - group/user
    private ImageLoader imageLoader;

    private OnSearchItemClickListener onSearchItemClickListener;


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

        if(holder.item.getImageUrl() != null) {
            imageLoader.displayImage(holder.item.getImageUrl(), holder.searchImageView);
        } else if(holder.item.getType() == SearchResultItem.TYPE_USER) {
            holder.searchImageView.setImageResource(R.drawable.ic_person_black_24dp);
        } else {
            holder.searchImageView.setImageResource(R.mipmap.ic_launcher);
        }
        holder.searchTextView.setText(holder.item.getName());

        if (holder.item.getType() == 1) {
            holder.searchTypeView.setText(R.string.user_lowercase);
        } else if (holder.item.getType() == 0) {
            holder.searchTypeView.setText(R.string.group_lowercase);
        } else {
            holder.searchTypeView.setText("");
        }

        holder.searchView.setOnClickListener(view1 ->
                onSearchItemClickListener.onSearchItemClick(holder.item));
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View searchView;

        final ImageView searchImageView;
        final TextView searchTextView;
        final TextView searchTypeView;

        public SearchResultItem item;

        ViewHolder(View view) {
            super(view);
            searchView = view;

            searchTextView = view.findViewById(R.id.textSearchItem);
            searchImageView = view.findViewById(R.id.imgSearchItem);
            searchTypeView = view.findViewById(R.id.tvSearchItemType);
        }
    }

    public void setOnSearchItemClickListener(OnSearchItemClickListener onSearchItemClickListener) {
        this.onSearchItemClickListener = onSearchItemClickListener;
    }

    public interface OnSearchItemClickListener {
        void onSearchItemClick(SearchResultItem item);
    }

}


