package com.karacasoft.interestr.pages.search;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.karacasoft.interestr.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Esra Aydemir on 23.11.2017.
 */

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder>{
    private List<SearchResultItem> searchResults;
    private ImageLoader imageLoader;

    public SearchRecyclerViewAdapter(List<SearchResultItem> searchResults) {
        this.searchResults = searchResults;
        imageLoader = ImageLoader.getInstance();
    }



    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_group, parent, false);
        return new ViewHolder(view);

    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.item = searchResults.get(position);
        imageLoader.displayImage(searchResults.get(position).getImage(),holder.searchImageView);
        holder.searchTextView.setText(searchResults.get(position).getText());
        holder.searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        public SearchResultItem item;

        public ViewHolder(View view) {
            super(view);
            searchView = view;
            searchTextView = view.findViewById(R.id.textSearchItem);
            searchImageView = view.findViewById(R.id.imgSearchItem);
        }
    }

    class SearchResultItem  {

        private String text;
        private String imageUrl;

        public SearchResultItem(String text,String image) {
            this.text = text;
            this.imageUrl = image;
        }

        public SearchResultItem(String text){
            this.text = text;
            this.imageUrl = null;
        }


        public void setImage(String image) {
            imageUrl = image;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getImage() {
            return imageUrl;
        }

        public String getText() {
            return text;
        }
    }
}

