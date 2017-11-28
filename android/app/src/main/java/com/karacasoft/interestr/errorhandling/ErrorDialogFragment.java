package com.karacasoft.interestr.errorhandling;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.karacasoft.interestr.R;

/**
 * Created by karacasoft on 28.11.2017.
 */

public class ErrorDialogFragment extends DialogFragment {

    public static final String ARG_MESSAGE = "com.karacasoft.interestr.error_dialog.message";

    public static ErrorDialogFragment ErrorDialogFragment(String message) {
        ErrorDialogFragment f = new ErrorDialogFragment();

        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        f.setArguments(args);

        return f;
    }

    public ErrorDialogFragment() {}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString(ARG_MESSAGE);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.a_mistake)
                .setMessage(title)
                .create();
    }
}
