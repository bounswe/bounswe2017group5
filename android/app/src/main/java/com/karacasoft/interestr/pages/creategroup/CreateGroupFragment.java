package com.karacasoft.interestr.pages.creategroup;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.karacasoft.interestr.ErrorHandler;
import com.karacasoft.interestr.FloatingActionButtonHandler;
import com.karacasoft.interestr.InterestrApplication;
import com.karacasoft.interestr.R;
import com.karacasoft.interestr.network.InterestrAPI;
import com.karacasoft.interestr.network.InterestrAPIResult;
import com.karacasoft.interestr.network.models.Group;

import mabbas007.tagsedittext.TagsEditText;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateGroupFragment extends Fragment {

    public static final String ARG_GROUP = "com.karacasoft.interestr.GROUP";

    private Group group;

    private OnGroupSavedListener onGroupSavedListener;

    private ImageView edtGroupImage;
    private EditText edtGroupName;
    private EditText edtGroupDescription;
    private TagsEditText edtGroupTags;

    private InterestrAPI api;
    private ErrorHandler errorHandler;

    private FloatingActionButtonHandler fabHandler;

    public static CreateGroupFragment newInstance(Group group) {
        CreateGroupFragment createGroupFragment = new CreateGroupFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_GROUP, group);
        createGroupFragment.setArguments(args);

        return createGroupFragment;
    }

    public CreateGroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if(args != null) {
            Group group;
            if((group = (Group) args.getSerializable(ARG_GROUP)) != null) {
                // TODO add group information to form
            }
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_group, container, false);

        edtGroupImage = v.findViewById(R.id.edt_group_image);
        edtGroupName = v.findViewById(R.id.edt_group_name);
        edtGroupDescription = v.findViewById(R.id.edt_group_description);
        edtGroupTags = v.findViewById(R.id.group_tags);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        onGroupSavedListener = (OnGroupSavedListener) context;

        fabHandler = (FloatingActionButtonHandler) context;
        errorHandler = (ErrorHandler) context;

        fabHandler.hideFloatingActionButton();

        api = ((InterestrApplication) getActivity().getApplication()).getApi();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_with_save, menu);
    }

    private Group getGroup() {
        Group g = new Group();

        g.setName(edtGroupName.getText().toString());
        g.setDescription(edtGroupDescription.getText().toString());
        g.getTags().addAll(edtGroupTags.getTags());

        return g;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_save) {
            Group g = getGroup();

            api.createGroup(g, new InterestrAPI.Callback<Group>() {
                @Override
                public void onResult(InterestrAPIResult<Group> result) {
                    getActivity().runOnUiThread(() -> onGroupSavedListener.onGroupSaved(result.get()));
                }

                @Override
                public void onError(String error_message) {
                    getActivity().runOnUiThread(() -> errorHandler.onError(error_message));
                }
            });


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public interface OnGroupSavedListener {
        public void onGroupSaved(Group group);
    }

}
