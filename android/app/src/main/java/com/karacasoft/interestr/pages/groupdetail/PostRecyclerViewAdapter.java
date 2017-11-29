package com.karacasoft.interestr.pages.groupdetail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.karacasoft.interestr.R;
import com.karacasoft.interestr.network.models.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aras on 29.11.2017.
 */

public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.ViewHolder>{
    List<Post> postList = new ArrayList<>();

    public PostRecyclerViewAdapter(List<Post> posts){
        postList = posts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mPost = postList.get(position);
        holder.mPostOwnerView.setText(postList.get(position).getOwner());
        //todo add post content in correct format
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mPostOwnerView;
        public final TextView mPostContentView;
        public final Button mPostDetailButton;
        public Post mPost;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPostOwnerView = view.findViewById(R.id.tv_post_owner);
            mPostContentView = view.findViewById(R.id.tv_postText);//todo change to another type if template used
            mPostDetailButton = view.findViewById(R.id.btnPostDetail);
        }
    }
}
