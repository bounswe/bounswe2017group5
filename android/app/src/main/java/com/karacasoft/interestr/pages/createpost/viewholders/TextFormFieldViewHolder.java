package com.karacasoft.interestr.pages.createpost.viewholders;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.karacasoft.interestr.R;
import com.karacasoft.interestr.pages.createpost.data.PostData;
import com.karacasoft.interestr.pages.datatemplates.data.TemplateField;

/**
 * Created by karacasoft on 24.12.2017.
 */

public class TextFormFieldViewHolder extends FormFieldViewHolder {

    public final TextInputLayout textInputLayout;

    @SuppressWarnings("ConstantConditions")
    public TextFormFieldViewHolder(View itemView) {
        super(itemView);
        textInputLayout = itemView.findViewById(R.id.form_field_text_input);

        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateField();
            }
        });
    }

    public void setHint(String hint) {
        textInputLayout.setHint(hint);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public String getAnswer() {
        return textInputLayout.getEditText().getText().toString();
    }

}
