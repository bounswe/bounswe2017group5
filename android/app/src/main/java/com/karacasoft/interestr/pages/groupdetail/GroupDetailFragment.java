package com.karacasoft.interestr.pages.groupdetail;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.karacasoft.interestr.ErrorHandler;
import com.karacasoft.interestr.FloatingActionButtonHandler;
import com.karacasoft.interestr.InterestrApplication;
import com.karacasoft.interestr.R;
import com.karacasoft.interestr.ToolbarHandler;
import com.karacasoft.interestr.network.InterestrAPI;
import com.karacasoft.interestr.network.InterestrAPIImpl;
import com.karacasoft.interestr.network.InterestrAPIResult;
import com.karacasoft.interestr.network.models.Group;
import com.karacasoft.interestr.network.models.Post;
import com.karacasoft.interestr.network.models.Tag;
import com.karacasoft.interestr.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_GROUP_ID = "group_id";

    // TODO: Rename and change types of parameters
    private int mGroupId;

    private InterestrAPI api;
    private RecyclerView groupPostsList;
    private PostRecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<Post> posts = new ArrayList<>();

    private View root;
    private TextView gMemberNum;
    private Button gBtnJoin;
    private Button gBtnMore;
    private TextView gDescription;
    private TextView gTags;

    private ErrorHandler errorHandler;
    private ToolbarHandler toolbarHandler;

    private FloatingActionButtonHandler fabHandler;

    private OnAddPostButtonClicked onAddPostButtonClicked;

    public GroupDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a new instance of this fragment with the given parameters
     *
     * @param groupId Group ID to view on fragment
     * @return A new instance of fragment GroupDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupDetailFragment newInstance(int groupId) {
        GroupDetailFragment fragment = new GroupDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_GROUP_ID, groupId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.fragment_group_detail, container, false);
        gBtnJoin = root.findViewById(R.id.btnJoinGroup);
        gBtnMore = root.findViewById(R.id.btnMoreGroup);
        gDescription = root.findViewById(R.id.tvAboutGroup);
        gMemberNum = root.findViewById(R.id.tvMemberNum);
        gTags = root.findViewById(R.id.tvTagList);



        groupPostsList = root.findViewById(R.id.rvGroupPosts);
        recyclerViewAdapter = new PostRecyclerViewAdapter(posts);
        groupPostsList.setLayoutManager(new LinearLayoutManager(getContext()));
        groupPostsList.setAdapter(recyclerViewAdapter);

        return root;
    }

    private void setupFloatingActionButton(FloatingActionButton floatingActionButton) {
        floatingActionButton.setImageResource(R.drawable.ic_add_white_24dp);
        floatingActionButton.setOnClickListener((view) -> onAddPostButtonClicked.onAddPostButtonClicked());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        api = ((InterestrApplication)getActivity().getApplication()).getApi();

        if (getArguments() != null) {
            mGroupId = getArguments().getInt(ARG_GROUP_ID);
        }

        errorHandler = (ErrorHandler) context;
        toolbarHandler = (ToolbarHandler) context;

        fabHandler = (FloatingActionButtonHandler) context;

        onAddPostButtonClicked = (OnAddPostButtonClicked) context;

        setupFloatingActionButton(fabHandler.getFloatingActionButton());

        fillGroupDetail();
    }

    private void fillGroupDetail() {
        api.getGroupDetail(mGroupId, new InterestrAPI.Callback<Group>() {
            @Override
            public void onResult(InterestrAPIResult<Group> result) {
                Group g = result.get();

                gDescription.setText(g.getDescription());
                gMemberNum.setText(StringUtils.pluralize(g.getMemberCount(), "Member"));
                gTags.setText("");
                for (Tag tag : g.getTags()) {
                    gTags.append(tag.getLabel() + ", ");
                }

                toolbarHandler.setTitle(g.getName());
            }

            @Override
            public void onError(String error_message) {
                errorHandler.onError(error_message);
            }
        });

        api.getPosts(mGroupId, new InterestrAPI.Callback<ArrayList<Post>>() {
            @Override
            public void onResult(InterestrAPIResult<ArrayList<Post>> result) {
                posts.clear();
                posts.addAll(result.get());

                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error_message) {
                errorHandler.onError(error_message);
            }
        });



    }

    public interface OnAddPostButtonClicked {
        public void onAddPostButtonClicked();
    }

}
