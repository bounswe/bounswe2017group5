package com.karacasoft.interestr.pages.datatemplates.view_holders.multiple_choice;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.karacasoft.interestr.R;

/**
 * Created by karacasoft on 28.11.2017.
 */

public class ChoiceItemDialog extends DialogFragment {

    public static final String ARG_ITEM_NAME = "com.karacasoft.interestr.item_name";

    private OnChoiceItemChangedListener onChoiceItemChangedListener;

    public static ChoiceItemDialog newInstance(String item_name) {
        ChoiceItemDialog dialog = new ChoiceItemDialog();

        Bundle args = new Bundle();
        args.putString(ARG_ITEM_NAME, item_name);
        dialog.setArguments(args);

        return dialog;
    }

    @SuppressWarnings("ConstantConditions")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String itemName = "";
        if(getArguments() != null) {
            itemName = getArguments().getString(ARG_ITEM_NAME, "");
        }

        @SuppressLint("InflateParams") View dialogView = LayoutInflater.from(getContext())
                .inflate(R.layout.data_template_multiple_choice_add_choice_dialog, null);

        EditText editText = dialogView.findViewById(R.id.edt_choice_name);

        assert editText != null;
        editText.setText(itemName, TextView.BufferType.EDITABLE);

        final String finalItemName = itemName;

        return new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.InterestrTheme_AlertDialog))
                .setTitle("Choice")
                .setView(dialogView)
                .setPositiveButton(R.string.done, (dialogInterface, i) -> {
                    String newText;
                    if(!finalItemName.equals(newText = editText.getText().toString())) {
                        onChoiceItemChangedListener.onChoiceItemChanged(newText);
                    }
                })
                .create();
    }

    public void setOnChoiceItemChangedListener(OnChoiceItemChangedListener onChoiceItemChangedListener) {
        this.onChoiceItemChangedListener = onChoiceItemChangedListener;
    }

    public interface OnChoiceItemChangedListener {
        public void onChoiceItemChanged(String newItemName);
    }
}
