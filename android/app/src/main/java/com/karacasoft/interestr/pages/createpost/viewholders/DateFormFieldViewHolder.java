package com.karacasoft.interestr.pages.createpost.viewholders;

import android.support.v4.app.FragmentManager;
import android.view.View;

import java.util.Locale;

/**
 * Created by karacasoft on 25.12.2017.
 */

public class DateFormFieldViewHolder extends TextFormFieldViewHolder {

    @SuppressWarnings("ConstantConditions")
    public DateFormFieldViewHolder(View itemView, FragmentManager fragmentManager) {
        super(itemView);
        FragmentManager fragmentManager1 = fragmentManager;

        textInputLayout.setOnClickListener(view -> {
            DatePickerFragment fragment = new DatePickerFragment();

            fragment.setOnDateSetListener((year, month, day) -> {
                textInputLayout.getEditText().setText(String.format(Locale.getDefault(), "%d/%d/%d", day, month, year));
                updateField();
            });

            fragment.show(fragmentManager, null);
        });
    }

}
