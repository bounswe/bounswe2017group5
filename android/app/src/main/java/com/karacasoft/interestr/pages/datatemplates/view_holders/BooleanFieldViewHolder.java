package com.karacasoft.interestr.pages.datatemplates.view_holders;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.karacasoft.interestr.R;
import com.karacasoft.interestr.pages.datatemplates.data.TemplateField;

/**
 * Created by karacasoft on 23.11.2017.
 */

public class BooleanFieldViewHolder extends FieldViewHolder {

    public static final int BOOLEAN_VIEW_HOLDER_TYPE = 1;

    public TemplateField field;

    public TextView fieldType;
    public EditText fieldName;
    public Button btnRemove;

    public BooleanFieldViewHolder(View itemView) {
        super(itemView);
        fieldType = itemView.findViewById(R.id.field_type);
        fieldName = itemView.findViewById(R.id.field_name);
        btnRemove = itemView.findViewById(R.id.btn_remove);

        fieldName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                field.setName(editable.toString());
            }
        });
    }

    @Override
    public void updateField() {
        field.setName(fieldName.getText().toString());
    }

    @Override
    public String toString() {
        return super.toString() + "0:" + fieldName.getText().toString();
    }

}
