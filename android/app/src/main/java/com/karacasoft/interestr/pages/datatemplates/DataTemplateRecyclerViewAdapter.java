package com.karacasoft.interestr.pages.datatemplates;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karacasoft.interestr.R;
import com.karacasoft.interestr.pages.datatemplates.data.TemplateField;
import com.karacasoft.interestr.pages.datatemplates.view_holders.BooleanFieldViewHolder;
import com.karacasoft.interestr.pages.datatemplates.view_holders.FieldViewHolder;
import com.karacasoft.interestr.pages.datatemplates.view_holders.ShortTextFieldViewHolder;

import java.util.ArrayList;

/**
 * Generates views according to the template fields
 *
 * Created by karacasoft on 22.11.2017.
 */

public class DataTemplateRecyclerViewAdapter extends RecyclerView.Adapter<FieldViewHolder> {

    private ArrayList<TemplateField> fields;
    private DataTemplateCreatorFragment.OnDataTemplateFieldRemoveListener onDataTemplateFieldClickListener;

    public DataTemplateRecyclerViewAdapter(ArrayList<TemplateField> fields,
                                           DataTemplateCreatorFragment.OnDataTemplateFieldRemoveListener onDataTemplateFieldClickListener) {
        this.fields = fields;
        this.onDataTemplateFieldClickListener = onDataTemplateFieldClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        TemplateField field = fields.get(position);
        switch (field.getType()) {
            case SHORT_TEXT:
                return ShortTextFieldViewHolder.SHORT_TEXT_VIEW_HOLDER_TYPE;
            case BOOLEAN:
                return BooleanFieldViewHolder.BOOLEAN_VIEW_HOLDER_TYPE;
            //TODO add
            default:
                return -1; // undefined
        }
    }

    @Override
    public FieldViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FieldViewHolder vh = null;
        View v;
        switch (viewType) {
            case ShortTextFieldViewHolder.SHORT_TEXT_VIEW_HOLDER_TYPE:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.data_template_field_text, parent, false);

                vh = new ShortTextFieldViewHolder(v);
                break;
            case BooleanFieldViewHolder.BOOLEAN_VIEW_HOLDER_TYPE:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.data_template_field_checkbox, parent, false);

                vh = new BooleanFieldViewHolder(v);
                break;
            default:

        }
        return vh;
    }

    @Override
    public void onBindViewHolder(FieldViewHolder holder, int position) {
        if(holder instanceof ShortTextFieldViewHolder) {
            configureShortTextViewHolder(fields.get(position), (ShortTextFieldViewHolder) holder);
        } else if(holder instanceof BooleanFieldViewHolder) {
            configureBooleanViewHolder(fields.get(position), (BooleanFieldViewHolder) holder);
        }
    }

    private void configureShortTextViewHolder(TemplateField field, ShortTextFieldViewHolder viewHolder) {
        if(field.getName() != null) {
            viewHolder.fieldName.setText(field.getName());
        } else {
            viewHolder.fieldName.setText("");
        }
        if(field.getHint() != null) {
            viewHolder.fieldHint.setText(field.getHint());
        } else {
            viewHolder.fieldHint.setText("");
        }
        viewHolder.btnRemove.setOnClickListener((view) -> {
            onDataTemplateFieldClickListener.onDataTemplateFieldRemove(field);
        });
    }

    private void configureBooleanViewHolder(TemplateField field, BooleanFieldViewHolder viewHolder) {
        if(field.getName() != null) {
            viewHolder.fieldName.setText(field.getName());
        } else {
            viewHolder.fieldName.setText("");
        }
        viewHolder.btnRemove.setOnClickListener((view) -> {
            onDataTemplateFieldClickListener.onDataTemplateFieldRemove(field);
        });
    }

    @Override
    public int getItemCount() {
        return this.fields.size();
    }

}
