package com.karacasoft.interestr.pages.datatemplates;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.karacasoft.interestr.MainActivity;
import com.karacasoft.interestr.R;
import com.karacasoft.interestr.pages.datatemplates.data.Template;
import com.karacasoft.interestr.pages.datatemplates.data.TemplateField;
import com.karacasoft.interestr.pages.datatemplates.data.dummy.DummyValues;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataTemplateCreatorFragment extends Fragment implements MainActivity.OnFamActionClickedListener{

    public static final String ARG_TEMPLATE = "arg_template";
    public static final String ARG_CREATE_NEW_TEMPLATE = "arg_create_new_template";

    private RecyclerView fieldList;
    private Button btnAddField;

    private Template template;
    private boolean createNewTemplate;

    private DataTemplateRecyclerViewAdapter fieldListAdapter;
    private OnDataTemplateFieldRemoveListener onDataTemplateFieldClickListener;
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
                this.template = DummyValues.getDummyTemplate();
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

    public void setOnDataTemplateFieldClickListener(OnDataTemplateFieldRemoveListener onDataTemplateFieldClickListener) {
        this.onDataTemplateFieldClickListener = onDataTemplateFieldClickListener;
    }


    @Override
    public void onFamActionClicked(String action) {
        TemplateField field = new TemplateField();
        if(action.equals(MainActivity.ACTION_ADD_SHORT_TEXT)) {
            field.setType(TemplateField.Type.SHORT_TEXT);
        } else if(action.equals(MainActivity.ACTION_ADD_BOOLEAN)) {
            field.setType(TemplateField.Type.BOOLEAN);
        }
        int index = template.getFields().size();
        template.getFields().add(field);

        fieldListAdapter.notifyItemInserted(index);
    }

    public interface OnDataTemplateFieldRemoveListener {
        void onDataTemplateFieldRemove(TemplateField field);
    }
}
