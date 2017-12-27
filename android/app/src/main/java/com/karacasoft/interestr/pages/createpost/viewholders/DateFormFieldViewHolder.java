package com.karacasoft.interestr.pages.createpost.viewholders;

import android.support.v4.app.FragmentManager;
import android.view.View;

import java.util.Locale;

/**
 * Created by karacasoft on 25.12.2017.
 */

public class DateFormFieldViewHolder extends TextFormFieldViewHolder {

    private final FragmentManager fragmentManager;

    private void displayDialog(FragmentManager fragmentManager) {
        DatePickerFragment fragment = new DatePickerFragment();

        fragment.setOnDateSetListener((year, month, day) -> {
            textInputLayout.getEditText().setText(String.format(Locale.getDefault(),
                    "%d/%d/%d",
                    day, month + 1, year));
            updateField();
        });

        fragment.show(fragmentManager, null);
    }

    @SuppressWarnings("ConstantConditions")
    public DateFormFieldViewHolder(View itemView, FragmentManager fragmentManager) {
        super(itemView);
        this.fragmentManager = fragmentManager;

        textInputLayout.getEditText().setOnFocusChangeListener((view, b) -> {
            if(b) {
                displayDialog(fragmentManager);
            }
        });

        //textInputLayout.setClickable(true);

        textInputLayout.setOnClickListener(view -> {
            displayDialog(fragmentManager);
        });
    }

}
