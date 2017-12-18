package com.karacasoft.interestr.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.ContextThemeWrapper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.karacasoft.interestr.R;

/**
 * Created by karacasoft on 18.12.2017.
 */

public class TextInputView extends RelativeLayout {

    private EditText editTextChild;
    private TextView labelChild;

    private String inputType = "(Type)";
    private String label = "Name";

    public TextInputView(Context context) {
        super(context);
        init();
    }

    public TextInputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextInputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void moveLabelAside() {

    }

    private static int dpToPx(DisplayMetrics metrics, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    private void init() {
        this.setFocusable(true);



        EditText editText = new EditText(getContext());
        editText.setLayoutParams(
                new LayoutParams(LayoutParams.MATCH_PARENT,
                                                dpToPx(getContext().getResources().getDisplayMetrics(), 60)));

        editText.setBackgroundResource(R.drawable.text_input_view_edit_text_bg);

        this.addView(editText);

        LayoutParams textViewLayoutParams =
                new LayoutParams(LayoutParams.WRAP_CONTENT,
                                                dpToPx(getContext().getResources().getDisplayMetrics(), 60));

        textViewLayoutParams.addRule(CENTER_VERTICAL);
        textViewLayoutParams.addRule(ALIGN_PARENT_LEFT);

        TextView textView = new TextView(
                new ContextThemeWrapper(getContext(), R.style.InterestrTheme_Text_InputHint),
                null, 0);

        textView.setPadding(20, 20, 20, 20);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            textView.setTextAppearance(R.style.InterestrTheme_Text_InputHint);
//        } else {
//            textView.setTextAppearance(getContext(), R.style.InterestrTheme_Text_InputHint);
//        }


        textView.setLayoutParams(textViewLayoutParams);

        textView.setBackgroundResource(R.drawable.text_input_view_label_bg);

        textView.setText(String.format("%s: ", label));

        this.addView(textView);

        ViewCompat.setElevation(textView, 2.0f);
        ViewCompat.setElevation(editText, 2.0f);

    }

    public EditText getEditTextChild() {
        return editTextChild;
    }

    public TextView getLabelChild() {
        return labelChild;
    }
}
