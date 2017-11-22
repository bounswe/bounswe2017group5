package com.karacasoft.interestr.pages.data_templates.view_holders;

import android.view.View;
import android.widget.EditText;

import com.karacasoft.interestr.R;

/**
 * Created by karacasoft on 23.11.2017.
 */

public class BooleanFieldViewHolder extends FieldViewHolder {

    public static final int BOOLEAN_VIEW_HOLDER_TYPE = 1;

    public EditText fieldName;

    public BooleanFieldViewHolder(View itemView) {
        super(itemView);
        fieldName = itemView.findViewById(R.id.field_name);
    }

}
