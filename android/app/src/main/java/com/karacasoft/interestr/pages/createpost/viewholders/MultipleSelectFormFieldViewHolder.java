package com.karacasoft.interestr.pages.createpost.viewholders;

import android.view.View;

import java.util.ArrayList;

/**
 * Created by karacasoft on 27.12.2017.
 */

public class MultipleSelectFormFieldViewHolder extends FormFieldViewHolder {

    private MultipleSelectViewController viewCtrl;

    private View itemView;

    public MultipleSelectFormFieldViewHolder(View itemView) {
        super(itemView);

        this.itemView = itemView;
    }

    public void setViewCtrl(String question, ArrayList<String> choices, ArrayList<String> selected) {
        viewCtrl = new MultipleSelectViewController(itemView, question, choices, selected);

        viewCtrl.setOnSelectedChangedListener(this::updateField);
    }

    public void updateView() {
        viewCtrl.updateList();
    }

    @Override
    public String getAnswer() {
        StringBuilder selected = new StringBuilder();
        boolean first = true;
        for (String sel : viewCtrl.getSelected()) {
            if(first) {
                selected.append(sel);
                first = false;
            } else {
                selected.append(", ").append(sel);
            }
        }
        return selected.toString();
    }
}
