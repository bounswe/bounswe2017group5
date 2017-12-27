package com.karacasoft.interestr.pages.createpost.viewholders;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.karacasoft.interestr.R;

import java.util.ArrayList;

/**
 * Created by karacasoft on 27.12.2017.
 */

public class DropdownFormFieldViewHolder extends FormFieldViewHolder {

    public final TextView textView;
    private final Spinner dropdown;

    public DropdownFormFieldViewHolder(View itemView) {
        super(itemView);

        dropdown = itemView.findViewById(R.id.dropdown);
        textView = itemView.findViewById(R.id.dropdown_name);
    }

    public void populateSpinner(ArrayList<String> choices, String selected) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                dropdown.getContext(),
                android.R.layout.simple_spinner_item,
                choices
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(spinnerAdapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateField();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                updateField();
            }
        });
    }

    @Override
    public String getAnswer() {
        if(dropdown.getSelectedItem() != null) {
            return dropdown.getSelectedItem().toString();
        }
        return "";
    }

}
