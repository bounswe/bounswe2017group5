package com.karacasoft.interestr.pages.createpost;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.karacasoft.interestr.pages.createpost.viewholders.FormFieldViewHolder;
import com.karacasoft.interestr.pages.datatemplates.data.Template;

/**
 * Created by karacasoft on 13.12.2017.
 */

public class TemplateFormGenerationAdapter extends RecyclerView.Adapter<FormFieldViewHolder> {

    private static final int VIEW_TYPE_CHECKBOX = 0;
    private static final int VIEW_TYPE_MULTISEL = 1;
    private static final int VIEW_TYPE_TEXTAREA = 2;
    private static final int VIEW_TYPE_TEXT = 3;
    private static final int VIEW_TYPE_NUMBER = 4;
    private static final int VIEW_TYPE_DATE = 5;
    private static final int VIEW_TYPE_EMAIL = 6;
    private static final int VIEW_TYPE_URL = 7;
    private static final int VIEW_TYPE_TEL = 8;

    private Template template;

    public TemplateFormGenerationAdapter(Template template) {
        this.template = template;
    }

    @Override
    public int getItemViewType(int position) {
        switch (template.getFields().get(position).getType()) {
            case CHECKBOX:
                return VIEW_TYPE_CHECKBOX;
            case MULTISEL:
                return VIEW_TYPE_MULTISEL;
            case TEXTAREA:
                return VIEW_TYPE_TEXTAREA;
            case TEXT:
                return VIEW_TYPE_TEXT;
            case NUMBER:
                return VIEW_TYPE_NUMBER;
            case DATE:
                return VIEW_TYPE_DATE;
            case EMAIL:
                return VIEW_TYPE_EMAIL;
            case URL:
                return VIEW_TYPE_URL;
            case TEL:
                return VIEW_TYPE_TEL;
        }
        return super.getItemViewType(position);
    }

    @Override
    public FormFieldViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        return null;
    }

    @Override
    public void onBindViewHolder(FormFieldViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return template.getFields().size();
    }
}
