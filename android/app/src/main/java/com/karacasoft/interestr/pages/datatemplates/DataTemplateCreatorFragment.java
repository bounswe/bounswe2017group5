package com.karacasoft.interestr.pages.datatemplates;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.karacasoft.interestr.R;
import com.karacasoft.interestr.pages.datatemplates.data.Template;
import com.karacasoft.interestr.pages.datatemplates.data.dummy.DummyValues;

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

        fieldList = v.findViewById(R.id.fields_list);
        fieldListAdapter = new DataTemplateRecyclerViewAdapter(template.getFields());

        fieldList.setAdapter(fieldListAdapter);

        return v;
    }

}
