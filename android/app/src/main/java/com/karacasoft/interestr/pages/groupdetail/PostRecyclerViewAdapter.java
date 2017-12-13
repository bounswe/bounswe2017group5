package com.karacasoft.interestr.pages.groupdetail;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.karacasoft.interestr.R;
import com.karacasoft.interestr.network.InterestrAPI;
import com.karacasoft.interestr.network.models.Post;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aras on 29.11.2017.
 */

public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.ViewHolder>{
    private List<Post> postList = new ArrayList<>();

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

        holder.mPostOwnerView.setText(holder.mPost.getOwnerName());

        StringBuilder postContent = new StringBuilder();

        try {
            for (int i = 0; i < holder.mPost.getData().length(); i++) {
                JSONObject obj = holder.mPost.getData().getJSONObject(i);

                postContent.append("<b>").append(obj.getString("question")).append(":</b> ");
                postContent.append("<span>").append(obj.getString("response")).append("</span>");
                postContent.append("<br>");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.mPostContentView.setText(Html.fromHtml(postContent.toString(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.mPostContentView.setText(Html.fromHtml(postContent.toString()));
        }

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mPostOwnerView;
        final TextView mPostContentView;
        final Button mPostDetailButton;
        Post mPost;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mPostOwnerView = view.findViewById(R.id.tv_post_owner);
            mPostContentView = view.findViewById(R.id.tv_postText);//todo change to another type if template used
            mPostDetailButton = view.findViewById(R.id.btnPostDetail);
        }
    }
}
