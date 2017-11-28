package com.karacasoft.interestr.pages.datatemplates;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.karacasoft.interestr.FloatingActionButtonHandler;
import com.karacasoft.interestr.FloatingActionsMenuHandler;
import com.karacasoft.interestr.R;
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

    private FloatingActionsMenuHandler famHandler;
    private FloatingActionButtonHandler fabHandler;

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
        fieldListAdapter = new DataTemplateRecyclerViewAdapter(template.getFields(), this.onDataTemplateFieldClickListener);

        fieldList.setLayoutManager(new LinearLayoutManager(getContext()));
        fieldList.setItemAnimator(new DefaultItemAnimator());

        fieldList.setAdapter(fieldListAdapter);

        return v;
    }

    private void setupFloatingActionsMenu(FloatingActionMenu menu) {
        FloatingActionButton buttonAddShortText =
                new FloatingActionButton(getContext());
        buttonAddShortText.setLabelText("Short Text");
        buttonAddShortText.setImageResource(R.drawable.ic_short_text_white_24dp);
        buttonAddShortText.setOnClickListener((view) -> {
            TemplateField field = new TemplateField();
            field.setType(TemplateField.Type.SHORT_TEXT);

            int index = template.getFields().size();
            template.getFields().add(field);

            fieldListAdapter.notifyItemInserted(index);
        });

        FloatingActionButton buttonAddBooleanField =
                new FloatingActionButton(getContext());
        buttonAddBooleanField.setLabelText("Check Box");
        buttonAddBooleanField.setImageResource(R.drawable.ic_check_box_white_24dp);
        buttonAddBooleanField.setOnClickListener((view) -> {
            TemplateField field = new TemplateField();
            field.setType(TemplateField.Type.BOOLEAN);

            int index = template.getFields().size();
            template.getFields().add(field);

            fieldListAdapter.notifyItemInserted(index);
        });

        menu.addMenuButton(buttonAddShortText);
        menu.addMenuButton(buttonAddBooleanField);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fabHandler = (FloatingActionButtonHandler) context;
            famHandler = (FloatingActionsMenuHandler) context;
        } catch (ClassCastException e) {
            // Added to debug stuff
            e.printStackTrace();
        }
        fabHandler = (FloatingActionButtonHandler) context;
        famHandler = (FloatingActionsMenuHandler) context;

        fabHandler.hideFloatingActionButton();
        famHandler.hideFloatingActionsMenu();

        famHandler.clearFloatingActionsMenu();
        setupFloatingActionsMenu(famHandler.getFloatingActionsMenu());
        famHandler.showFloatingActionsMenu();
    }

    public void setOnDataTemplateFieldClickListener(OnDataTemplateFieldRemoveListener onDataTemplateFieldClickListener) {
        this.onDataTemplateFieldClickListener = onDataTemplateFieldClickListener;
    }

    public interface OnDataTemplateFieldRemoveListener {
        void onDataTemplateFieldRemove(TemplateField field);
    }
}
