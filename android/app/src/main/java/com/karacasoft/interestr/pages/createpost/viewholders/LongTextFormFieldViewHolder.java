package com.karacasoft.interestr.pages.createpost.viewholders;

import android.text.InputType;
import android.view.View;

/**
 * Created by karacasoft on 25.12.2017.
 */

public class LongTextFormFieldViewHolder extends TextFormFieldViewHolder {
    @SuppressWarnings("ConstantConditions")
    public LongTextFormFieldViewHolder(View itemView) {
        super(itemView);

        textInputLayout.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
    }
}
