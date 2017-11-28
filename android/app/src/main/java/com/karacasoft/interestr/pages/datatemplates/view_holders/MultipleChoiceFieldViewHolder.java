package com.karacasoft.interestr.pages.datatemplates.view_holders;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.karacasoft.interestr.R;
import com.karacasoft.interestr.pages.datatemplates.data.MultipleChoiceTemplateField;
import com.karacasoft.interestr.pages.datatemplates.view_holders.multiple_choice.ChoiceItem;
import com.karacasoft.interestr.pages.datatemplates.view_holders.multiple_choice.Dummy;
import com.karacasoft.interestr.pages.datatemplates.view_holders.multiple_choice.MultipleChoiceFieldRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * Created by karacasoft on 28.11.2017.
 */

public class MultipleChoiceFieldViewHolder extends FieldViewHolder {

    public static final int MULTIPLE_CHOICE_VIEW_HOLDER_TYPE = 5;

    public MultipleChoiceTemplateField field;

    public TextView fieldType;
    public EditText fieldName;

    public RecyclerView choiceList;
    public Button btnAddItem;

    public MultipleChoiceFieldRecyclerViewAdapter adapter;
    private ArrayList<ChoiceItem> choiceItems = new ArrayList<>();

    public Button btnRemove;

    public MultipleChoiceFieldViewHolder(View itemView) {
        super(itemView);

        fieldType = itemView.findViewById(R.id.field_type);
        fieldName = itemView.findViewById(R.id.field_name);

        choiceList = itemView.findViewById(R.id.choices_list);
        btnAddItem = itemView.findViewById(R.id.add_choice);
        btnRemove = itemView.findViewById(R.id.btn_remove);

        choiceList.setLayoutManager(new GridLayoutManager(choiceList.getContext(), 2));
        choiceList.setItemAnimator(new DefaultItemAnimator());
        adapter = new MultipleChoiceFieldRecyclerViewAdapter(choiceItems);

        choiceList.setAdapter(adapter);
    }

    @Override
    public void updateField() {
        field.setName(fieldName.getText().toString());

        field.getChoices().clear();
        for (ChoiceItem item : choiceItems) {
            field.getChoices().add(item.getName());
        }
    }

    public synchronized void addItem(ChoiceItem item) {
        choiceItems.add(item);
        adapter.notifyItemInserted(choiceItems.size() - 1);
    }

    public synchronized void addItem(int index, ChoiceItem item) {
        choiceItems.add(index, item);
        adapter.notifyItemInserted(index);
    }

    public synchronized void editItem(int index, String newValue) {
        choiceItems.get(index).setName(newValue);
        adapter.notifyItemChanged(index);
    }

    public synchronized void removeItem(int index) {
        choiceItems.remove(index);
        adapter.notifyItemRemoved(index);
    }

    public synchronized void clearItems() {
        choiceItems.clear();
    }

    @Override
    public String toString() {
        return super.toString() + "0:" + fieldName.getText().toString();
    }


}
