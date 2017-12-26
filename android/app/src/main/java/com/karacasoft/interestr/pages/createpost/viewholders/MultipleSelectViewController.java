package com.karacasoft.interestr.pages.createpost.viewholders;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.karacasoft.interestr.R;

import java.util.ArrayList;

/**
 * Created by karacasoft on 26.12.2017.
 */

public class MultipleSelectViewController {

    private OnSelectedChangedListener onSelectedChangedListener;

    @NonNull
    private final View view;

    @NonNull
    private final LinearLayout choiceList;

    @NonNull
    private final TextView multipleSelectQuestion;

    @NonNull
    private String question;

    @NonNull
    private ArrayList<String> choices;

    @NonNull
    private ArrayList<String> selected;

    public MultipleSelectViewController(@NonNull View view,
                                        @NonNull String question,
                                        @NonNull ArrayList<String> choices,
                                        @NonNull ArrayList<String> selected) {
        this.view = view;

        this.question = question;
        this.choices = choices;

        this.selected = selected;

        choiceList = view.findViewById(R.id.multiple_select_choice_list);
        multipleSelectQuestion = view.findViewById(R.id.multiple_select_question_text);

        init();
    }

    private View generateChoiceView(ViewGroup container, String choiceString, boolean isSelected) {
        View v = LayoutInflater.from(container.getContext())
                .inflate(R.layout.form_field_list_item_multiple_select, container, false);

        CheckBox checkBox = v.findViewById(R.id.multiple_select_field_choice_checkbox);

        checkBox.setText(choiceString);

        if(isSelected) {
            checkBox.setSelected(true);
        }

        checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b) {
                selected.add(choiceString);
            } else {
                selected.remove(choiceString);
            }
            if(onSelectedChangedListener != null) {
                onSelectedChangedListener.onSelectedChanged();
            }
        });

        return v;
    }

    public void updateList() {
        choiceList.removeAllViews();
        for (String choice : choices) {
            choiceList.addView(generateChoiceView(choiceList, choice, selected.contains(choice)));
        }
    }

    private void init() {
        updateList();
        multipleSelectQuestion.setText(question);
    }

    public void setOnSelectedChangedListener(OnSelectedChangedListener onSelectedChangedListener) {
        this.onSelectedChangedListener = onSelectedChangedListener;
    }

    public void setChoices(@NonNull ArrayList<String> choices) {
        this.choices = choices;
    }

    @NonNull
    public ArrayList<String> getSelected() {
        return selected;
    }

    public void setSelected(@NonNull ArrayList<String> selected) {
        this.selected = selected;
    }

    public void setQuestion(@NonNull String question) {
        this.question = question;
    }

    public interface OnSelectedChangedListener {
        void onSelectedChanged();
    }
}
