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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.karacasoft.interestr.ErrorHandler;
import com.karacasoft.interestr.FloatingActionButtonHandler;
import com.karacasoft.interestr.InterestrApplication;
import com.karacasoft.interestr.R;
import com.karacasoft.interestr.network.InterestrAPI;
import com.karacasoft.interestr.network.InterestrAPIResult;
import com.karacasoft.interestr.network.models.DataTemplate;
import com.karacasoft.interestr.network.models.Post;
import com.karacasoft.interestr.pages.createpost.data.PostData;
import com.karacasoft.interestr.pages.datatemplates.data.Template;

import org.json.JSONArray;
import org.json.JSONException;

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
    private FloatingActionButtonHandler fabHandler;

    private int groupId;
    private int currentDataTemplateId;

    private OnPostSavedListener onPostSavedListener;
    private OnAddDataTemplateClickedListener onAddDataTemplateClickedListener;

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

        setHasOptionsMenu(true);
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

        fabHandler = (FloatingActionButtonHandler) context;

        errorHandler = (ErrorHandler) context;
        onPostSavedListener = (OnPostSavedListener) context;
        onAddDataTemplateClickedListener = (OnAddDataTemplateClickedListener) context;
    }

    @Override
    public void onResume() {
        super.onResume();

        updateTemplateSelector();

        fabHandler.hideFloatingActionButton();
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

        currentDataTemplateId = template.getId();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.post_create_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_save:
                if(currentDataTemplateId == 0) {
                    errorHandler.onError("You have to select a data template first");
                    return false;
                } else {
                    try {
                        ArrayList<PostData> postDataArrayList = new ArrayList<>();

                        postDataArrayList.addAll(adapter.getPostData().values());

                        Post p = new Post();

                        p.setGroupId(groupId);
                        p.setDataTemplateId(currentDataTemplateId);
                        p.setData(PostData.toJSONArrayFromList(postDataArrayList));

                        api.createPost(p, new InterestrAPI.Callback<Post>() {
                            @Override
                            public void onResult(InterestrAPIResult<Post> result) {
                                onPostSavedListener.onPostSaved(result.get());
                            }

                            @Override
                            public void onError(String error_message) {
                                errorHandler.onError(error_message);
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return true;
                }
            case R.id.action_add_template:
                onAddDataTemplateClickedListener.onAddDataTemplateClicked();
                return true;
        }

        return super.onOptionsItemSelected(item);
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

    public interface OnPostSavedListener {
        void onPostSaved(Post post);
    }

    public interface OnAddDataTemplateClickedListener {
        void onAddDataTemplateClicked();
    }

}
