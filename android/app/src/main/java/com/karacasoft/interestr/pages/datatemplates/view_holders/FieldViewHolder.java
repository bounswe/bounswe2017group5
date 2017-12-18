package com.karacasoft.interestr.pages.datatemplates.view_holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by karacasoft on 23.11.2017.
 */

public abstract class FieldViewHolder extends RecyclerView.ViewHolder {

    public int viewType = 0;

    public FieldViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void updateField();

}
