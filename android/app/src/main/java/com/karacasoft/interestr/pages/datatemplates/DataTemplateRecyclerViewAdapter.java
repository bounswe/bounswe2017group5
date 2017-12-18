package com.karacasoft.interestr.pages.datatemplates;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karacasoft.interestr.R;
import com.karacasoft.interestr.pages.datatemplates.data.MultipleChoiceTemplateField;
import com.karacasoft.interestr.pages.datatemplates.data.Template;
import com.karacasoft.interestr.pages.datatemplates.data.TemplateField;
import com.karacasoft.interestr.pages.datatemplates.view_holders.BooleanFieldViewHolder;
import com.karacasoft.interestr.pages.datatemplates.view_holders.DateFieldViewHolder;
import com.karacasoft.interestr.pages.datatemplates.view_holders.EmailFieldViewHolder;
import com.karacasoft.interestr.pages.datatemplates.view_holders.FieldViewHolder;
import com.karacasoft.interestr.pages.datatemplates.view_holders.LongTextFieldViewHolder;
import com.karacasoft.interestr.pages.datatemplates.view_holders.MultipleChoiceFieldViewHolder;
import com.karacasoft.interestr.pages.datatemplates.view_holders.NumericFieldViewHolder;
import com.karacasoft.interestr.pages.datatemplates.view_holders.ShortTextFieldViewHolder;
import com.karacasoft.interestr.pages.datatemplates.view_holders.TelephoneFieldViewHolder;
import com.karacasoft.interestr.pages.datatemplates.view_holders.UrlFieldViewHolder;
import com.karacasoft.interestr.pages.datatemplates.view_holders.multiple_choice.ChoiceItem;
import com.karacasoft.interestr.pages.datatemplates.view_holders.multiple_choice.ChoiceItemDialog;

import java.util.ArrayList;

/**
 * Generates views according to the template fields
 *
 * Created by karacasoft on 22.11.2017.
 */

public class DataTemplateRecyclerViewAdapter extends RecyclerView.Adapter<FieldViewHolder> {

    public static final int VIEW_TYPE_CHECKBOX = 0;
    public static final int VIEW_TYPE_MULTISEL = 1;
    public static final int VIEW_TYPE_TEXTAREA = 2;
    public static final int VIEW_TYPE_TEXT = 3;
    public static final int VIEW_TYPE_NUMBER = 4;
    public static final int VIEW_TYPE_DATE = 5;
    public static final int VIEW_TYPE_EMAIL = 6;
    public static final int VIEW_TYPE_URL = 7;
    public static final int VIEW_TYPE_TEL = 8;

    private ArrayList<TemplateField> fields;
    private DataTemplateCreatorFragment.OnDataTemplateFieldRemoveListener onDataTemplateFieldClickListener;

    private AppCompatActivity context;

    public DataTemplateRecyclerViewAdapter(AppCompatActivity context, ArrayList<TemplateField> fields,
                                           DataTemplateCreatorFragment.OnDataTemplateFieldRemoveListener onDataTemplateFieldClickListener) {
        this.context = context;
        this.fields = fields;
        this.onDataTemplateFieldClickListener = onDataTemplateFieldClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        TemplateField field = fields.get(position);
        switch (field.getType()) {
            case MULTISEL:
                return VIEW_TYPE_MULTISEL;
            case TEXTAREA:
                return VIEW_TYPE_TEXTAREA;
            case NUMBER:
                return VIEW_TYPE_NUMBER;
            case DATE:
                return VIEW_TYPE_DATE;
            case EMAIL:
                return VIEW_TYPE_EMAIL;
            case TEXT:
                return VIEW_TYPE_TEXT;
            case CHECKBOX:
                return VIEW_TYPE_CHECKBOX;
            case URL:
                return VIEW_TYPE_URL;
            case TEL:
                return VIEW_TYPE_TEL;
        }
        return super.getItemViewType(position);
    }

    @Override
    public FieldViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FieldViewHolder vh = null;
        View v;
        switch (viewType) {
            case VIEW_TYPE_TEXT:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.data_template_field_text, parent, false);

                vh = new ShortTextFieldViewHolder(v);
                break;
            case VIEW_TYPE_CHECKBOX:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.data_template_field_checkbox, parent, false);

                vh = new BooleanFieldViewHolder(v);
                break;
            case VIEW_TYPE_TEXTAREA:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.data_template_field_text, parent, false);

                vh = new LongTextFieldViewHolder(v);
                break;
            case VIEW_TYPE_EMAIL:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.data_template_field_text, parent, false);

                vh = new EmailFieldViewHolder(v);
                break;
            case VIEW_TYPE_NUMBER:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.data_template_field_text, parent, false);

                vh = new NumericFieldViewHolder(v);
                break;
            case VIEW_TYPE_MULTISEL:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.data_template_field_multiple_choice, parent, false);

                vh = new MultipleChoiceFieldViewHolder(v);
                break;
            case VIEW_TYPE_DATE:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.data_template_field_text, parent, false);

                vh = new DateFieldViewHolder(v);
                break;
            case VIEW_TYPE_URL:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.data_template_field_text, parent, false);

                vh = new UrlFieldViewHolder(v);
                break;
            case VIEW_TYPE_TEL:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.data_template_field_text, parent, false);

                vh = new TelephoneFieldViewHolder(v);
                break;
            default:

        }
        return vh;
    }

    @Override
    public void onBindViewHolder(FieldViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_TEXT) {
            configureShortTextViewHolder(fields.get(position), (ShortTextFieldViewHolder) holder);
        } else if (getItemViewType(position) == VIEW_TYPE_CHECKBOX) {
            configureBooleanViewHolder(fields.get(position), (BooleanFieldViewHolder) holder);
        } else if (getItemViewType(position) == VIEW_TYPE_TEXTAREA) {
            configureLongTextViewHolder(fields.get(position), (LongTextFieldViewHolder) holder);
        } else if (getItemViewType(position) == VIEW_TYPE_EMAIL) {
            configureEmailFieldViewHolder(fields.get(position), (EmailFieldViewHolder) holder);
        } else if (getItemViewType(position) == VIEW_TYPE_NUMBER) {
            configureNumericFieldViewHolder(fields.get(position), (NumericFieldViewHolder) holder);
        } else if (getItemViewType(position) == VIEW_TYPE_MULTISEL) {
            configureMultipleChoiceFieldViewHolder((MultipleChoiceTemplateField) fields.get(position),
                    (MultipleChoiceFieldViewHolder) holder);
        } else if (getItemViewType(position) == VIEW_TYPE_DATE) {
            configureDateFieldViewHolder(fields.get(position), (DateFieldViewHolder) holder);
        } else if (getItemViewType(position) == VIEW_TYPE_URL) {
            configureUrlFieldViewHolder(fields.get(position), (UrlFieldViewHolder) holder);
        } else if (getItemViewType(position) == VIEW_TYPE_TEL) {
            configureTelFieldViewHolder(fields.get(position), (TelephoneFieldViewHolder) holder);
        }
    }

    @Override
    public void onViewRecycled(FieldViewHolder holder) {
        holder.updateField();
        super.onViewRecycled(holder);
    }

    private void configureShortTextViewHolder(TemplateField field, ShortTextFieldViewHolder viewHolder) {
        viewHolder.field = field;
        viewHolder.fieldType.setText(R.string.short_text);
        if (field.getName() != null) {
            viewHolder.fieldName.setText(field.getName());
        } else {
            viewHolder.fieldName.setText("");
        }
        viewHolder.btnRemove.setOnClickListener((view) -> {
            onDataTemplateFieldClickListener.onDataTemplateFieldRemove(field);
        });
    }

    private void configureBooleanViewHolder(TemplateField field, BooleanFieldViewHolder viewHolder) {
        viewHolder.field = field;
        viewHolder.fieldType.setText(R.string.check_box);
        if (field.getName() != null) {
            viewHolder.fieldName.setText(field.getName());
        } else {
            viewHolder.fieldName.setText("");
        }
        viewHolder.btnRemove.setOnClickListener((view) -> {
            onDataTemplateFieldClickListener.onDataTemplateFieldRemove(field);
        });
    }

    private void configureLongTextViewHolder(TemplateField field, LongTextFieldViewHolder viewHolder) {
        viewHolder.field = field;
        viewHolder.fieldType.setText(R.string.long_text);
        if (field.getName() != null) {
            viewHolder.fieldName.setText(field.getName());
        } else {
            viewHolder.fieldName.setText("");
        }
        viewHolder.btnRemove.setOnClickListener((view) -> {
            onDataTemplateFieldClickListener.onDataTemplateFieldRemove(field);
        });
    }

    private void configureEmailFieldViewHolder(TemplateField field, EmailFieldViewHolder viewHolder) {
        viewHolder.field = field;
        viewHolder.fieldType.setText(R.string.email);
        if (field.getName() != null) {
            viewHolder.fieldName.setText(field.getName());
        } else {
            viewHolder.fieldName.setText("");
        }
        viewHolder.btnRemove.setOnClickListener((view) -> {
            onDataTemplateFieldClickListener.onDataTemplateFieldRemove(field);
        });
    }

    private void configureNumericFieldViewHolder(TemplateField field, NumericFieldViewHolder viewHolder) {
        viewHolder.field = field;
        viewHolder.fieldType.setText(R.string.numeric);
        if (field.getName() != null) {
            viewHolder.fieldName.setText(field.getName());
        } else {
            viewHolder.fieldName.setText("");
        }
        viewHolder.btnRemove.setOnClickListener((view) -> {
            onDataTemplateFieldClickListener.onDataTemplateFieldRemove(field);
        });
    }

    private void configureDateFieldViewHolder(TemplateField field, DateFieldViewHolder viewHolder) {
        viewHolder.field = field;
        viewHolder.fieldType.setText(R.string.date);
        if (field.getName() != null) {
            viewHolder.fieldName.setText(field.getName());
        } else {
            viewHolder.fieldName.setText("");
        }
        viewHolder.btnRemove.setOnClickListener((view) -> {
            onDataTemplateFieldClickListener.onDataTemplateFieldRemove(field);
        });
    }

    private void configureUrlFieldViewHolder(TemplateField field, UrlFieldViewHolder viewHolder) {
        viewHolder.field = field;
        viewHolder.fieldType.setText(R.string.url);
        if (field.getName() != null) {
            viewHolder.fieldName.setText(field.getName());
        } else {
            viewHolder.fieldName.setText("");
        }
        viewHolder.btnRemove.setOnClickListener((view) -> {
            onDataTemplateFieldClickListener.onDataTemplateFieldRemove(field);
        });
    }

    private void configureTelFieldViewHolder(TemplateField field, TelephoneFieldViewHolder viewHolder) {
        viewHolder.field = field;
        viewHolder.fieldType.setText(R.string.telephone);
        if (field.getName() != null) {
            viewHolder.fieldName.setText(field.getName());
        } else {
            viewHolder.fieldName.setText("");
        }
        viewHolder.btnRemove.setOnClickListener((view) -> {
            onDataTemplateFieldClickListener.onDataTemplateFieldRemove(field);
        });
    }

    private void configureMultipleChoiceFieldViewHolder(MultipleChoiceTemplateField field, MultipleChoiceFieldViewHolder viewHolder) {
        viewHolder.field = field;
        viewHolder.fieldType.setText(R.string.multiple_choice);
        if (field.getName() != null) {
            viewHolder.fieldName.setText(field.getName());
        } else {
            viewHolder.fieldName.setText("");
        }

        viewHolder.clearItems();

        for (String str : field.getChoices()) {
            ChoiceItem item = new ChoiceItem();
            item.setName(str);
            viewHolder.addItem(item);
        }

        viewHolder.adapter.notifyDataSetChanged();

        viewHolder.adapter.setOnChoiceClickedListener((index, item) -> {
            ChoiceItemDialog dialog = ChoiceItemDialog.newInstance("");
            dialog.setOnChoiceItemChangedListener((str) -> {
                viewHolder.editItem(index, str);
            });
            dialog.show(context.getSupportFragmentManager(), "choiceItemDialog");
        });

        viewHolder.adapter.setOnChoiceDeleteClickedListener((index, item) -> {
            viewHolder.removeItem(index);
            Snackbar.make(viewHolder.itemView,
                    String.format("%s %s", context.getString(R.string.removed_item), item.getName()), Snackbar.LENGTH_LONG)
                    .setAction("Undo", view -> viewHolder.addItem(index, item)).show();
        });

        viewHolder.btnAddItem.setOnClickListener((view) -> {
            ChoiceItemDialog dialog = ChoiceItemDialog.newInstance("");
            dialog.setOnChoiceItemChangedListener((str) -> {
                ChoiceItem item = new ChoiceItem();
                item.setName(str);
                viewHolder.addItem(item);
            });
            dialog.show(context.getSupportFragmentManager(), "choiceItemDialog");
        });

        viewHolder.btnRemove.setOnClickListener((view) -> {
            onDataTemplateFieldClickListener.onDataTemplateFieldRemove(field);
        });
    }

    public Template getTemplate() {
        Template t = new Template();

        t.getFields().addAll(fields);

        return t;
    }

    @Override
    public int getItemCount() {
        return this.fields.size();
    }

}
