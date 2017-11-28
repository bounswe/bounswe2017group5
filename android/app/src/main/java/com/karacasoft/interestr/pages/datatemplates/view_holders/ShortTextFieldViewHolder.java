package com.karacasoft.interestr.pages.datatemplates.view_holders;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.karacasoft.interestr.R;
import com.karacasoft.interestr.pages.datatemplates.data.TemplateField;

/**
 * Created by karacasoft on 23.11.2017.
 */

public class ShortTextFieldViewHolder extends FieldViewHolder {

    public static final int SHORT_TEXT_VIEW_HOLDER_TYPE = 0;

    public TemplateField field;

    public TextView fieldType;
    public EditText fieldName;
    public EditText fieldHint;
    public Button btnRemove;


    public ShortTextFieldViewHolder(View itemView) {
        super(itemView);
        fieldType = itemView.findViewById(R.id.field_type);
        fieldName = itemView.findViewById(R.id.field_name);
        fieldHint = itemView.findViewById(R.id.field_placeholder);
        btnRemove = itemView.findViewById(R.id.btn_remove);
    }

    public void updateField() {
        field.setName(fieldName.getText().toString());
        field.setHint(fieldHint.getText().toString());
    }

    @Override
    public String toString() {
        return super.toString() + "0:" + fieldName.getText().toString();
    }
}
