package com.karacasoft.interestr.pages.datatemplates;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.karacasoft.interestr.ErrorHandler;
import com.karacasoft.interestr.FloatingActionButtonHandler;
import com.karacasoft.interestr.FloatingActionsMenuHandler;
import com.karacasoft.interestr.MenuHandler;
import com.karacasoft.interestr.R;
import com.karacasoft.interestr.pages.datatemplates.data.MultipleChoiceTemplateField;
import com.karacasoft.interestr.pages.datatemplates.data.Template;
import com.karacasoft.interestr.pages.datatemplates.data.TemplateField;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataTemplateCreatorFragment extends Fragment {

    public static final String ARG_TEMPLATE = "arg_template";
    public static final String ARG_CREATE_NEW_TEMPLATE = "arg_create_new_template";

    private RecyclerView fieldList;
    private Button btnAddField;

    private Template template;
    private boolean createNewTemplate;

    private DataTemplateRecyclerViewAdapter fieldListAdapter;
    private OnDataTemplateFieldRemoveListener onDataTemplateFieldClickListener;
    private OnDataTemplateSavedListener onDataTemplateSavedListener;

    private FloatingActionsMenuHandler famHandler;
    private FloatingActionButtonHandler fabHandler;

    private ErrorHandler errorHandler;
    private MenuHandler menuHandler;

    public DataTemplateCreatorFragment() {
        // Required empty public constructor
    }

    public static DataTemplateCreatorFragment newInstance() {
        DataTemplateCreatorFragment fragment = new DataTemplateCreatorFragment();

        Bundle args = new Bundle();
        args.putBoolean(ARG_CREATE_NEW_TEMPLATE, true);
        fragment.setArguments(args);

        return fragment;
    }

    public static DataTemplateCreatorFragment newInstance(Template t) {
        DataTemplateCreatorFragment fragment = new DataTemplateCreatorFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_TEMPLATE, t);
        args.putBoolean(ARG_CREATE_NEW_TEMPLATE, false);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        if(args != null) {
            if(args.getBoolean(ARG_CREATE_NEW_TEMPLATE)) {
                this.template = new Template();
            } else {
                this.template = (Template) args.getSerializable(ARG_TEMPLATE);
            }
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_data_template_creator, container, false);

        setOnDataTemplateFieldClickListener((field) -> {
            int index = template.getFields().indexOf(field);
            template.getFields().remove(index);
            fieldListAdapter.notifyItemRemoved(index);
        });

        fieldList = v.findViewById(R.id.fields_list);
        fieldListAdapter = new DataTemplateRecyclerViewAdapter((AppCompatActivity) getActivity(), template.getFields(), this.onDataTemplateFieldClickListener);

        fieldList.setLayoutManager(new LinearLayoutManager(getContext()));
        fieldList.setItemAnimator(new DefaultItemAnimator());

        fieldList.setAdapter(fieldListAdapter);

        return v;
    }

    private void setupFloatingActionsMenu(FloatingActionMenu menu) {
        FloatingActionButton buttonAddShortText =
                new FloatingActionButton(getContext());
        buttonAddShortText.setLabelText(getString(R.string.short_text));
        buttonAddShortText.setImageResource(R.drawable.ic_short_text_white_24dp);
        buttonAddShortText.setColorNormalResId(R.color.colorAccent);
        buttonAddShortText.setColorPressedResId(R.color.colorAccentDark);
        buttonAddShortText.setOnClickListener((view) -> {
            TemplateField field = new TemplateField();
            field.setType(TemplateField.Type.SHORT_TEXT);

            int index = template.getFields().size();
            template.getFields().add(field);

            fieldListAdapter.notifyItemInserted(index);
            menu.close(true);
        });

        FloatingActionButton buttonAddBooleanField =
                new FloatingActionButton(getContext());
        buttonAddBooleanField.setLabelText(getString(R.string.check_box));
        buttonAddBooleanField.setImageResource(R.drawable.ic_check_box_white_24dp);
        buttonAddBooleanField.setColorNormalResId(R.color.colorAccent);
        buttonAddBooleanField.setColorPressedResId(R.color.colorAccentDark);
        buttonAddBooleanField.setOnClickListener((view) -> {
            TemplateField field = new TemplateField();
            field.setType(TemplateField.Type.BOOLEAN);

            int index = template.getFields().size();
            template.getFields().add(field);

            fieldListAdapter.notifyItemInserted(index);
            menu.close(true);
        });


        FloatingActionButton buttonAddLongTextField =
                new FloatingActionButton(getContext());
        buttonAddLongTextField.setLabelText(getString(R.string.long_text));
        buttonAddLongTextField.setImageResource(R.drawable.ic_long_text_white_24dp);
        buttonAddLongTextField.setColorNormalResId(R.color.colorAccent);
        buttonAddLongTextField.setColorPressedResId(R.color.colorAccentDark);
        buttonAddLongTextField.setOnClickListener((view) -> {
            TemplateField field = new TemplateField();
            field.setType(TemplateField.Type.LONG_TEXT);

            int index = template.getFields().size();
            template.getFields().add(field);

            fieldListAdapter.notifyItemInserted(index);
            menu.close(true);
        });



        FloatingActionButton buttonAddEmailField =
                new FloatingActionButton(getContext());
        buttonAddEmailField.setLabelText(getString(R.string.email));
        buttonAddEmailField.setImageResource(R.drawable.ic_email_white_24dp);
        buttonAddEmailField.setColorNormalResId(R.color.colorAccent);
        buttonAddEmailField.setColorPressedResId(R.color.colorAccentDark);
        buttonAddEmailField.setOnClickListener((view) -> {
            TemplateField field = new TemplateField();
            field.setType(TemplateField.Type.EMAIL);

            int index = template.getFields().size();
            template.getFields().add(field);

            fieldListAdapter.notifyItemInserted(index);
            menu.close(true);
        });


        FloatingActionButton buttonAddNumericField =
                new FloatingActionButton(getContext());
        buttonAddNumericField.setLabelText(getString(R.string.numeric));
        buttonAddNumericField.setImageResource(R.drawable.ic_numeric_white_24dp);
        buttonAddNumericField.setColorNormalResId(R.color.colorAccent);
        buttonAddNumericField.setColorPressedResId(R.color.colorAccentDark);
        buttonAddNumericField.setOnClickListener((view) -> {
            TemplateField field = new TemplateField();
            field.setType(TemplateField.Type.NUMERIC);

            int index = template.getFields().size();
            template.getFields().add(field);

            fieldListAdapter.notifyItemInserted(index);
            menu.close(true);
        });


        FloatingActionButton buttonAddMultipleChoiceField =
                new FloatingActionButton(getContext());
        buttonAddMultipleChoiceField.setLabelText(getString(R.string.multiple_choice));
        buttonAddMultipleChoiceField.setImageResource(R.drawable.ic_list_black_24dp);
        buttonAddMultipleChoiceField.setColorNormalResId(R.color.colorAccent);
        buttonAddMultipleChoiceField.setColorPressedResId(R.color.colorAccentDark);
        buttonAddMultipleChoiceField.setOnClickListener((view) -> {
            MultipleChoiceTemplateField field = new MultipleChoiceTemplateField();
            field.setType(TemplateField.Type.MULTIPLE_CHOICE);

            int index = template.getFields().size();
            template.getFields().add(field);

            fieldListAdapter.notifyItemInserted(index);
            menu.close(true);
        });

        menu.addMenuButton(buttonAddShortText);
        menu.addMenuButton(buttonAddBooleanField);
        menu.addMenuButton(buttonAddLongTextField);
        menu.addMenuButton(buttonAddEmailField);
        menu.addMenuButton(buttonAddNumericField);
        menu.addMenuButton(buttonAddMultipleChoiceField);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fabHandler = (FloatingActionButtonHandler) context;
        famHandler = (FloatingActionsMenuHandler) context;

        errorHandler = (ErrorHandler) context;

        fabHandler.hideFloatingActionButton();
        famHandler.hideFloatingActionsMenu();

        famHandler.clearFloatingActionsMenu();
        setupFloatingActionsMenu(famHandler.getFloatingActionsMenu());
        famHandler.showFloatingActionsMenu();

        this.onDataTemplateSavedListener = (OnDataTemplateSavedListener) context;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_with_save, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_save:
                Template template = fieldListAdapter.getTemplate();
                // TODO do stuff with template
                onDataTemplateSavedListener.onDataTemplateSaved(template);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setOnDataTemplateFieldClickListener(OnDataTemplateFieldRemoveListener onDataTemplateFieldClickListener) {
        this.onDataTemplateFieldClickListener = onDataTemplateFieldClickListener;
    }

    public interface OnDataTemplateFieldRemoveListener {
        void onDataTemplateFieldRemove(TemplateField field);
    }

    public interface OnDataTemplateSavedListener {
        void onDataTemplateSaved(Template template);
    }
}
