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
import com.karacasoft.interestr.pages.datatemplates.view_holders.EmailFieldViewHolder;
import com.karacasoft.interestr.pages.datatemplates.view_holders.FieldViewHolder;
import com.karacasoft.interestr.pages.datatemplates.view_holders.LongTextFieldViewHolder;
import com.karacasoft.interestr.pages.datatemplates.view_holders.MultipleChoiceFieldViewHolder;
import com.karacasoft.interestr.pages.datatemplates.view_holders.NumericFieldViewHolder;
import com.karacasoft.interestr.pages.datatemplates.view_holders.ShortTextFieldViewHolder;
import com.karacasoft.interestr.pages.datatemplates.view_holders.multiple_choice.ChoiceItem;
import com.karacasoft.interestr.pages.datatemplates.view_holders.multiple_choice.ChoiceItemDialog;

import java.util.ArrayList;

/**
 * Generates views according to the template fields
 *
 * Created by karacasoft on 22.11.2017.
 */

public class DataTemplateRecyclerViewAdapter extends RecyclerView.Adapter<FieldViewHolder> {

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
            case MULTIPLE_CHOICE:
                return MultipleChoiceFieldViewHolder.MULTIPLE_CHOICE_VIEW_HOLDER_TYPE;
            case LONG_TEXT:
                return LongTextFieldViewHolder.LONG_TEXT_VIEW_HOLDER_TYPE;
            case NUMERIC:
                return NumericFieldViewHolder.NUMERIC_VIEW_HOLDER_TYPE;
            case EMAIL:
                return EmailFieldViewHolder.EMAIL_VIEW_HOLDER_TYPE;
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
            case LongTextFieldViewHolder.LONG_TEXT_VIEW_HOLDER_TYPE:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.data_template_field_text, parent, false);

                vh = new LongTextFieldViewHolder(v);
                break;
            case EmailFieldViewHolder.EMAIL_VIEW_HOLDER_TYPE:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.data_template_field_text, parent, false);

                vh = new EmailFieldViewHolder(v);
                break;
            case NumericFieldViewHolder.NUMERIC_VIEW_HOLDER_TYPE:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.data_template_field_text, parent, false);

                vh = new NumericFieldViewHolder(v);
                break;
            case MultipleChoiceFieldViewHolder.MULTIPLE_CHOICE_VIEW_HOLDER_TYPE:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.data_template_field_multiple_choice, parent, false);

                vh = new MultipleChoiceFieldViewHolder(v);
                break;
            default:

        }
        return vh;
    }

    @Override
    public void onBindViewHolder(FieldViewHolder holder, int position) {
        if(getItemViewType(position) == ShortTextFieldViewHolder.SHORT_TEXT_VIEW_HOLDER_TYPE) {
            configureShortTextViewHolder(fields.get(position), (ShortTextFieldViewHolder) holder);
        } else if(getItemViewType(position) == BooleanFieldViewHolder.BOOLEAN_VIEW_HOLDER_TYPE) {
            configureBooleanViewHolder(fields.get(position), (BooleanFieldViewHolder) holder);
        } else if(getItemViewType(position) == LongTextFieldViewHolder.LONG_TEXT_VIEW_HOLDER_TYPE) {
            configureLongTextViewHolder(fields.get(position), (LongTextFieldViewHolder) holder);
        } else if(getItemViewType(position) == EmailFieldViewHolder.EMAIL_VIEW_HOLDER_TYPE) {
            configureEmailFieldViewHolder(fields.get(position), (EmailFieldViewHolder) holder);
        } else if(getItemViewType(position) == NumericFieldViewHolder.NUMERIC_VIEW_HOLDER_TYPE) {
            configureNumericFieldViewHolder(fields.get(position), (NumericFieldViewHolder) holder);
        } else if(getItemViewType(position) == MultipleChoiceFieldViewHolder.MULTIPLE_CHOICE_VIEW_HOLDER_TYPE) {
            configureMultipleChoiceFieldViewHolder((MultipleChoiceTemplateField) fields.get(position),
                    (MultipleChoiceFieldViewHolder) holder);
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
        viewHolder.field = field;
        viewHolder.fieldType.setText(R.string.check_box);
        if(field.getName() != null) {
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
        if(field.getName() != null) {
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
        if(field.getName() != null) {
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
        if(field.getName() != null) {
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
        if(field.getName() != null) {
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

        // TODO

        return t;
    }

    @Override
    public int getItemCount() {
        return this.fields.size();
    }

}
