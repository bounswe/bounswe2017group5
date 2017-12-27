package com.karacasoft.interestr.pages.datatemplates;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.karacasoft.interestr.ErrorHandler;
import com.karacasoft.interestr.FloatingActionButtonHandler;
import com.karacasoft.interestr.InterestrApplication;
import com.karacasoft.interestr.MenuHandler;
import com.karacasoft.interestr.R;
import com.karacasoft.interestr.network.InterestrAPI;
import com.karacasoft.interestr.network.InterestrAPIResult;
import com.karacasoft.interestr.network.models.DataTemplate;
import com.karacasoft.interestr.network.models.User;
import com.karacasoft.interestr.pages.datatemplates.data.MultipleChoiceTemplateField;
import com.karacasoft.interestr.pages.datatemplates.data.Template;
import com.karacasoft.interestr.pages.datatemplates.data.TemplateField;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataTemplateCreatorFragment extends Fragment {

    public static final String ARG_TEMPLATE = "arg_template";
    public static final String ARG_CREATE_NEW_TEMPLATE = "arg_create_new_template";
    public static final String ARG_GROUP_ID = "com.karacasoft.interestr.group_id";

    private EditText templateName;
    private RecyclerView fieldList;

    private Template template;
    private boolean createNewTemplate;

    private DataTemplateRecyclerViewAdapter fieldListAdapter;
    private OnDataTemplateFieldRemoveListener onDataTemplateFieldClickListener;
    private OnDataTemplateSavedListener onDataTemplateSavedListener;

    private FloatingActionButtonHandler fabHandler;

    private ErrorHandler errorHandler;
    private MenuHandler menuHandler;

    private InterestrAPI api;

    private int groupId;

    public DataTemplateCreatorFragment() {
        // Required empty public constructor
    }

    public static DataTemplateCreatorFragment newInstance(int groupId) {
        DataTemplateCreatorFragment fragment = new DataTemplateCreatorFragment();

        Bundle args = new Bundle();
        args.putBoolean(ARG_CREATE_NEW_TEMPLATE, true);
        args.putInt(ARG_GROUP_ID, groupId);
        fragment.setArguments(args);

        return fragment;
    }

    public static DataTemplateCreatorFragment newInstance(int groupId, Template t) {
        DataTemplateCreatorFragment fragment = new DataTemplateCreatorFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_TEMPLATE, t);
        args.putBoolean(ARG_CREATE_NEW_TEMPLATE, false);
        args.putInt(ARG_GROUP_ID, groupId);
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
            this.groupId = args.getInt(ARG_GROUP_ID);
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

        templateName = v.findViewById(R.id.template_name);

        return v;
    }

    @SuppressWarnings("ConstantConditions")
    private void setupFloatingActionsButton(FloatingActionButton menu) {

        PopupMenu popupMenu = new PopupMenu(this.getContext(), menu);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.data_template_creator_add_field_menu, popupMenu.getMenu());
        menu.setOnClickListener(view -> popupMenu.show());

        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            TemplateField field;
            int index;
            switch (id) {
                case R.id.action_add_checkbox:
                    field = new TemplateField();
                    field.setType(TemplateField.Type.CHECKBOX);

                    index = template.getFields().size();
                    template.getFields().add(field);

                    fieldListAdapter.notifyItemInserted(index);
                    return true;
                case R.id.action_add_date:
                    field = new TemplateField();
                    field.setType(TemplateField.Type.DATE);

                    index = template.getFields().size();
                    template.getFields().add(field);

                    fieldListAdapter.notifyItemInserted(index);
                    return true;
                case R.id.action_add_email:
                    field = new TemplateField();
                    field.setType(TemplateField.Type.EMAIL);

                    index = template.getFields().size();
                    template.getFields().add(field);

                    fieldListAdapter.notifyItemInserted(index);
                    return true;
                case R.id.action_add_multisel:
                    field = new MultipleChoiceTemplateField();
                    field.setType(TemplateField.Type.MULTISEL);

                    index = template.getFields().size();
                    template.getFields().add(field);

                    fieldListAdapter.notifyItemInserted(index);
                    return true;
                case R.id.action_add_number:
                    field = new TemplateField();
                    field.setType(TemplateField.Type.NUMBER);

                    index = template.getFields().size();
                    template.getFields().add(field);

                    fieldListAdapter.notifyItemInserted(index);
                    return true;
                case R.id.action_add_select:
                    field = new MultipleChoiceTemplateField();
                    field.setType(TemplateField.Type.SELECT);

                    index = template.getFields().size();
                    template.getFields().add(field);

                    fieldListAdapter.notifyItemInserted(index);
                    return true;
                case R.id.action_add_telephone:
                    field = new TemplateField();
                    field.setType(TemplateField.Type.TEL);

                    index = template.getFields().size();
                    template.getFields().add(field);

                    fieldListAdapter.notifyItemInserted(index);
                    return true;
                case R.id.action_add_text:
                    field = new TemplateField();
                    field.setType(TemplateField.Type.TEXT);

                    index = template.getFields().size();
                    template.getFields().add(field);

                    fieldListAdapter.notifyItemInserted(index);
                    return true;
                case R.id.action_add_textarea:
                    field = new TemplateField();
                    field.setType(TemplateField.Type.TEXTAREA);

                    index = template.getFields().size();
                    template.getFields().add(field);

                    fieldListAdapter.notifyItemInserted(index);
                    return true;
                case R.id.action_add_url:
                    field = new TemplateField();
                    field.setType(TemplateField.Type.URL);

                    index = template.getFields().size();
                    template.getFields().add(field);

                    fieldListAdapter.notifyItemInserted(index);
                    return true;
            }
            return super.onOptionsItemSelected(item);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        fabHandler.showFloatingActionButton();
        setupFloatingActionsButton(fabHandler.getFloatingActionButton());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        api = ((InterestrApplication) getContext().getApplicationContext()).getApi();

        fabHandler = (FloatingActionButtonHandler) context;

        errorHandler = (ErrorHandler) context;

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
                DataTemplate dataTemplate = new DataTemplate();

                dataTemplate.setName(templateName.getText().toString());
                dataTemplate.setTemplate(fieldListAdapter.getTemplate());
                dataTemplate.setGroupId(groupId);

                api.getProfile(new InterestrAPI.Callback<User>() {
                    @Override
                    public void onResult(InterestrAPIResult<User> result) {
                        User u = result.get();

                        dataTemplate.setUserId(u.getId());

                        api.createDataTemplate(dataTemplate, new InterestrAPI.Callback<DataTemplate>() {
                            @Override
                            public void onResult(InterestrAPIResult<DataTemplate> result) {
                                onDataTemplateSavedListener.onDataTemplateSaved(result.get());
                            }

                            @Override
                            public void onError(String error_message) {
                                errorHandler.onError(error_message);
                            }
                        });
                    }

                    @Override
                    public void onError(String error_message) {

                    }
                });


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
        void onDataTemplateSaved(DataTemplate template);
    }
}
