package com.karacasoft.interestr.pages.createpost.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.karacasoft.interestr.pages.datatemplates.data.TemplateField;

/**
 * Base class for form fields.
 *
 * Created by karacasoft on 13.12.2017.
 */

public abstract class FormFieldViewHolder extends RecyclerView.ViewHolder {

    protected TemplateField field;
    protected String answer;

    public FormFieldViewHolder(View itemView) {
        super(itemView);
    }

    public abstract String getAnswer();

    public abstract void updateField();
}
