package com.karacasoft.interestr.pages.groups;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.karacasoft.interestr.R;
import com.karacasoft.interestr.network.models.Group;
import com.karacasoft.interestr.pages.groups.GroupsFragment.*;
import com.karacasoft.interestr.util.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link com.karacasoft.interestr.network.models.Group}
 * and makes a call to the specified {@link OnGroupsListItemClickedListener}.
 */
public class GroupRecyclerViewAdapter extends RecyclerView.Adapter<GroupRecyclerViewAdapter.ViewHolder> {

    private final List<Group> mValues;

    @Nullable
    private final OnGroupsListItemClickedListener mListener;

    private ImageLoader imageLoader;

    public GroupRecyclerViewAdapter(List<Group> items, @Nullable OnGroupsListItemClickedListener listener) {
        mValues = items;
        mListener = listener;
        imageLoader = ImageLoader.getInstance();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mGroupNameView.setText(mValues.get(position).getName());
        holder.mGroupDescView.setText(mValues.get(position).getDescription());
        holder.mGroupMemberCountView.setText(StringUtils.pluralize(mValues.get(position).getMemberCount(), "Member"));
        imageLoader.displayImage(mValues.get(position).getPictureUrl(), holder.mGroupPictureView);

        holder.mView.setOnClickListener((view) -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onGroupsListItemClicked(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView mGroupPictureView;
        final TextView mGroupNameView;
        final TextView mGroupDescView;
        final TextView mGroupMemberCountView;

        Group mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mGroupPictureView = view.findViewById(R.id.group_image);
            mGroupNameView = view.findViewById(R.id.group_name);
            mGroupDescView = view.findViewById(R.id.group_desc);
            mGroupMemberCountView = view.findViewById(R.id.group_member_count);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mGroupNameView.getText() + "'";
        }
    }
}
