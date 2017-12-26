package com.karacasoft.interestr.pages.createpost.viewholders;

import android.text.InputType;
import android.view.View;

/**
 * Created by karacasoft on 25.12.2017.
 */

public class NumberFormFieldViewHolder extends TextFormFieldViewHolder {
    @SuppressWarnings("ConstantConditions")
    public NumberFormFieldViewHolder(View itemView) {
        super(itemView);

        textInputLayout.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
    }
}
