package com.karacasoft.interestr.pages.datatemplates.view_holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.karacasoft.interestr.R;

/**
 * Created by karacasoft on 28.11.2017.
 */

public class MultipleChoiceFieldViewHolder extends FieldViewHolder {

    public static final int MULTIPLE_CHOICE_VIEW_HOLDER_TYPE = 5;

    public TextView fieldType;
    public EditText fieldName;

    public RecyclerView choiceList;
    public Button btnAddItem;

    public Button btnRemove;

    public MultipleChoiceFieldViewHolder(View itemView) {
        super(itemView);

        fieldType = itemView.findViewById(R.id.field_type);
        fieldName = itemView.findViewById(R.id.field_name);

        choiceList = itemView.findViewById(R.id.choices_list);
        btnAddItem = itemView.findViewById(R.id.add_choice);
        btnRemove = itemView.findViewById(R.id.btn_remove);
    }

    @Override
    public String toString() {
        return super.toString() + "0:" + fieldName.getText().toString();
    }


}
