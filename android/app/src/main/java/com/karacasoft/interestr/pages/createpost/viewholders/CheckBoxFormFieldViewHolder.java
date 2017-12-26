package com.karacasoft.interestr.pages.createpost.viewholders;

import android.view.View;
import android.widget.Switch;

import com.karacasoft.interestr.R;
import com.karacasoft.interestr.pages.createpost.data.PostData;

/**
 * Created by karacasoft on 25.12.2017.
 */

public class CheckBoxFormFieldViewHolder extends FormFieldViewHolder {

    public Switch aSwitch;

    public CheckBoxFormFieldViewHolder(View itemView) {
        super(itemView);

        aSwitch = itemView.findViewById(R.id.form_field_switch_input);

        aSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            updateField();
        });
    }

    @Override
    public String getAnswer() {
        return (aSwitch.isChecked()) ? "Yes" : "No";
    }
}
