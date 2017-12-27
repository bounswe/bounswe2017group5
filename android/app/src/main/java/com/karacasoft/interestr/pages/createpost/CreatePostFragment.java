package com.karacasoft.interestr.pages.createpost;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.karacasoft.interestr.ErrorHandler;
import com.karacasoft.interestr.InterestrApplication;
import com.karacasoft.interestr.R;
import com.karacasoft.interestr.network.InterestrAPI;
import com.karacasoft.interestr.network.InterestrAPIResult;
import com.karacasoft.interestr.network.models.DataTemplate;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreatePostFragment extends Fragment {

    public static final String ARG_GROUP_ID = "com.karacasoft.interestr.group_id";

    private Spinner templateSelector;
    private RecyclerView formFieldsView;

    private TemplateFormGenerationAdapter adapter;

    private InterestrAPI api;

    private ErrorHandler errorHandler;

    private int groupId;

    public static CreatePostFragment newInstance(int groupId) {
        CreatePostFragment postFragment = new CreatePostFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_GROUP_ID, groupId);
        postFragment.setArguments(args);

        return postFragment;
    }

    public CreatePostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getContext() != null) {
            api = ((InterestrApplication) getContext().getApplicationContext()).getApi();
        }

        groupId = getArguments().getInt(ARG_GROUP_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_post, container, false);

        templateSelector = view.findViewById(R.id.spChooseTemplate);
        formFieldsView = view.findViewById(R.id.rvPostFields);

        formFieldsView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        adapter = new TemplateFormGenerationAdapter(
                null, getActivity().getSupportFragmentManager()
        );

        formFieldsView.setAdapter(adapter);
        ViewCompat.setNestedScrollingEnabled(formFieldsView, true);

        templateSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateForm(((DataTemplateWrapper)adapterView.getSelectedItem()).dataTemplate);
                formFieldsView.setVisibility(View.VISIBLE);

                Log.d("CreatePostFrag", "Wow");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                formFieldsView.setVisibility(View.GONE);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        errorHandler = (ErrorHandler) context;
    }

    @Override
    public void onResume() {
        super.onResume();

        updateTemplateSelector();
    }

    private void updateTemplateSelector() {
        api.getDataTemplates(groupId, new InterestrAPI.Callback<ArrayList<DataTemplate>>() {
            @Override
            public void onResult(InterestrAPIResult<ArrayList<DataTemplate>> result) {
                ArrayList<DataTemplate> results = result.get();

                ArrayList<DataTemplateWrapper> wrapperList = new ArrayList<>(results.size());

                for (DataTemplate template : results) {
                    wrapperList.add(new DataTemplateWrapper(template));
                }

                ArrayAdapter<DataTemplateWrapper> spinnerAdapter = new ArrayAdapter<>(
                        getContext(), android.R.layout.simple_spinner_item, wrapperList
                );

                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                templateSelector.setAdapter(spinnerAdapter);
            }

            @Override
            public void onError(String error_message) {
                errorHandler.onError(error_message);
            }
        });
    }

    private void updateForm(DataTemplate template) {
        adapter.setDataTemplate(template.getTemplate());

        adapter.notifyDataSetChanged();
    }

    private static class DataTemplateWrapper {
        @NonNull
        final DataTemplate dataTemplate;

        private DataTemplateWrapper(@NonNull DataTemplate dataTemplate) {
            this.dataTemplate = dataTemplate;
        }

        @Override
        public String toString() {
            return dataTemplate.getName();
        }
    }

}
