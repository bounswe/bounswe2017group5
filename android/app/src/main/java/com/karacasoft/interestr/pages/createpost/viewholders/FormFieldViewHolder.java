package com.karacasoft.interestr.pages.createpost.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.karacasoft.interestr.pages.createpost.data.PostData;
import com.karacasoft.interestr.pages.datatemplates.data.TemplateField;

/**
 * Base class for form fields.
 *
 * Created by karacasoft on 13.12.2017.
 */

public abstract class FormFieldViewHolder extends RecyclerView.ViewHolder {

    public PostData data;
    protected TemplateField field;

    public FormFieldViewHolder(View itemView) {
        super(itemView);
    }

    public abstract String getAnswer();

    public void updateField() {
        if(data != null) data.setResponse(getAnswer());
    }
}
