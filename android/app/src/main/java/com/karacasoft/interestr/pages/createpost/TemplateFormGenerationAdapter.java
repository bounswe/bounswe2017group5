package com.karacasoft.interestr.pages.createpost;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karacasoft.interestr.R;
import com.karacasoft.interestr.pages.createpost.data.PostData;
import com.karacasoft.interestr.pages.createpost.viewholders.CheckBoxFormFieldViewHolder;
import com.karacasoft.interestr.pages.createpost.viewholders.DateFormFieldViewHolder;
import com.karacasoft.interestr.pages.createpost.viewholders.DropdownFormFieldViewHolder;
import com.karacasoft.interestr.pages.createpost.viewholders.EmailFormFieldViewHolder;
import com.karacasoft.interestr.pages.createpost.viewholders.FormFieldViewHolder;
import com.karacasoft.interestr.pages.createpost.viewholders.LongTextFormFieldViewHolder;
import com.karacasoft.interestr.pages.createpost.viewholders.MultipleSelectFormFieldViewHolder;
import com.karacasoft.interestr.pages.createpost.viewholders.NumberFormFieldViewHolder;
import com.karacasoft.interestr.pages.createpost.viewholders.TelephoneFormFieldViewHolder;
import com.karacasoft.interestr.pages.createpost.viewholders.TextFormFieldViewHolder;
import com.karacasoft.interestr.pages.createpost.viewholders.UrlFormFieldViewHolder;
import com.karacasoft.interestr.pages.datatemplates.data.MultipleChoiceTemplateField;
import com.karacasoft.interestr.pages.datatemplates.data.Template;
import com.karacasoft.interestr.pages.datatemplates.data.TemplateField;

import java.util.ArrayList;
import java.util.HashMap;

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
    private static final int VIEW_TYPE_SELECT = 9;

    @Nullable
    private Template template;
    private HashMap<String, PostData> postData;

    private final FragmentManager fragmentManager;

    public TemplateFormGenerationAdapter(FragmentManager fragmentManager) {
        this(null, fragmentManager);
    }

    public TemplateFormGenerationAdapter(@Nullable Template template, FragmentManager fragmentManager) {
        this.template = template;
        this.fragmentManager = fragmentManager;

        setDataTemplate(template);
    }

    public void setDataTemplate(@Nullable Template template) {
        if(template != null) {
            postData = new HashMap<>(template.getFields().size());

            for (TemplateField field : template.getFields()) {
                PostData data = new PostData();
                data.setQuestion(field.getName());
                data.setResponse("");
                postData.put(field.getName(), data);
            }
        }
        this.template = template;
    }

    @Override
    public int getItemViewType(int position) {
        if(template != null) {
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
                case SELECT:
                    return VIEW_TYPE_SELECT;
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public FormFieldViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_CHECKBOX:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.form_field_switch, parent, false);

                return new CheckBoxFormFieldViewHolder(view);
            case VIEW_TYPE_MULTISEL:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.form_field_multiple_select, parent, false);

                return new MultipleSelectFormFieldViewHolder(view);
            case VIEW_TYPE_TEXTAREA:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.form_field_text, parent, false);

                return new LongTextFormFieldViewHolder(view);
            case VIEW_TYPE_TEXT:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.form_field_text, parent, false);

                return new TextFormFieldViewHolder(view);
            case VIEW_TYPE_NUMBER:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.form_field_text, parent, false);

                return new NumberFormFieldViewHolder(view);
            case VIEW_TYPE_DATE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.form_field_text, parent, false);

                return new DateFormFieldViewHolder(view, fragmentManager);
            case VIEW_TYPE_EMAIL:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.form_field_text, parent, false);

                return new EmailFormFieldViewHolder(view);
            case VIEW_TYPE_URL:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.form_field_text, parent, false);

                return new UrlFormFieldViewHolder(view);
            case VIEW_TYPE_TEL:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.form_field_text, parent, false);

                return new TelephoneFormFieldViewHolder(view);
            case VIEW_TYPE_SELECT:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.form_field_dropdown, parent, false);

                return new DropdownFormFieldViewHolder(view);
            default:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(FormFieldViewHolder holder, int position) {
        if(template != null) {
            TemplateField field = template.getFields().get(position);

            holder.data = postData.get(field.getName());

            int viewType = getItemViewType(position);
            if (viewType == VIEW_TYPE_CHECKBOX) {
                configureCheckBoxField(field, (CheckBoxFormFieldViewHolder) holder);
            } else if (viewType == VIEW_TYPE_MULTISEL) {
                configureMultiSelField(field, (MultipleSelectFormFieldViewHolder) holder);
            } else if (viewType == VIEW_TYPE_TEXTAREA) {
                configureTextField(field, (TextFormFieldViewHolder) holder);
            } else if (viewType == VIEW_TYPE_TEXT) {
                configureTextField(field, (TextFormFieldViewHolder) holder);
            } else if (viewType == VIEW_TYPE_NUMBER) {
                configureTextField(field, (TextFormFieldViewHolder) holder);
            } else if (viewType == VIEW_TYPE_DATE) {
                configureTextField(field, (TextFormFieldViewHolder) holder);
            } else if (viewType == VIEW_TYPE_EMAIL) {
                configureTextField(field, (TextFormFieldViewHolder) holder);
            } else if (viewType == VIEW_TYPE_URL) {
                configureTextField(field, (TextFormFieldViewHolder) holder);
            } else if (viewType == VIEW_TYPE_TEL) {
                configureTextField(field, (TextFormFieldViewHolder) holder);
            } else if (viewType == VIEW_TYPE_SELECT) {
                configureDropdownField(field, (DropdownFormFieldViewHolder) holder);
            }
        }

    }

    private void configureCheckBoxField(TemplateField field, CheckBoxFormFieldViewHolder holder) {
        holder.aSwitch.setText(field.getName());

        holder.aSwitch.setSelected(postData.get(field.getName())
                .isCheckboxMarked());
    }

    @SuppressWarnings("ConstantConditions")
    private void configureTextField(TemplateField field, TextFormFieldViewHolder holder) {
        holder.setHint(field.getName());

        String response;
        if((response = postData.get(field.getName()).getResponse()) != null) {
            holder.textInputLayout.getEditText().setText(response);
        }
    }

    private void configureMultiSelField(TemplateField field, MultipleSelectFormFieldViewHolder holder) {
        holder.setViewCtrl(field.getName(), ((MultipleChoiceTemplateField) field).getChoices(),
                postData.get(field.getName()).getMultipleSelSelectedItems());

        holder.updateView();
    }

    private void configureDropdownField(TemplateField field, DropdownFormFieldViewHolder holder) {
        holder.populateSpinner(((MultipleChoiceTemplateField) field).getChoices(),
                postData.get(field.getName()).getResponse());
    }

    @Override
    public void onViewRecycled(FormFieldViewHolder holder) {
        holder.updateField();

        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return (template == null) ? 0 : template.getFields().size();
    }
}
