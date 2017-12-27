package com.karacasoft.interestr;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.karacasoft.interestr.errorhandling.ErrorDialogFragment;
import com.karacasoft.interestr.network.models.Post;
import com.karacasoft.interestr.pages.createpost.CreatePostFragment;
import com.karacasoft.interestr.pages.datatemplates.DataTemplateCreatorFragment;
import com.karacasoft.interestr.pages.datatemplates.data.Template;
import com.karacasoft.interestr.pages.groupdetail.GroupDetailFragment;
import com.karacasoft.interestr.pages.profile.ProfileFragment;

public class CoordinatorLayoutActivity extends AppCompatActivity
    implements ErrorHandler,
        ToolbarHandler,
        FloatingActionButtonHandler,
        GroupDetailFragment.OnAddPostButtonClicked,
        CreatePostFragment.OnPostSavedListener,
        DataTemplateCreatorFragment.OnDataTemplateSavedListener {

    public static final String ACTION_DISPLAY_USER ="com.karacasoft.interestr.display_user";
    public static final String EXTRA_USER_ID = "com.karacasoft.interestr.extra_user_id";

    public static final String ACTION_DISPLAY_GROUP ="com.karacasoft.interestr.display_group";
    public static final String EXTRA_GROUP_ID = "com.karacasoft.interestr.extra_group_id";

    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private NestedScrollView nestedScrollView;

    private FloatingActionButton fab;

    private int userId;
    private int groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_layout);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appBarLayout = findViewById(R.id.app_bar_layout);

        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);

        nestedScrollView = findViewById(R.id.nested_scroll_view);

        fab = findViewById(R.id.fab);

        FragmentManager fm = getSupportFragmentManager();
        if(getIntent()!=null && getIntent().getAction()!=null){
            if(getIntent().getAction().equals(ACTION_DISPLAY_USER)){
                fm.beginTransaction()
                        .replace(R.id.coordinator_layout_activity_content, ProfileFragment.newInstance(getIntent().getIntExtra(EXTRA_USER_ID,0)))
                        .commit();
            }else if(getIntent().getAction().equals(ACTION_DISPLAY_GROUP)){
                fm.beginTransaction()
                        .replace(R.id.coordinator_layout_activity_content, GroupDetailFragment.newInstance(groupId = getIntent().getIntExtra(EXTRA_GROUP_ID,0)))
                        .commit();
            }
        }else{
            //todo remove this else after putting a link to group page it is just for debug
            fm.beginTransaction()
                    .replace(R.id.coordinator_layout_activity_content,GroupDetailFragment.newInstance(0))
                    .commit();

        }
    }

    @Override
    public void onError(Throwable t) {
        ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.ErrorDialogFragment(t.getMessage());
        errorDialogFragment.show(getSupportFragmentManager(), "ErrorDialog:" + t.toString());
    }

    @Override
    public void onError(String errorMessage) {
        ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.ErrorDialogFragment(errorMessage);
        errorDialogFragment.show(getSupportFragmentManager(), "ErrorDialog:" + errorMessage);
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void setTitle(String title) {
        collapsingToolbarLayout.setTitle(title);
    }

    @Override
    public void showFloatingActionButton() {
        fab.show(true);
    }

    @Override
    public void hideFloatingActionButton() {
        fab.hide(true);
    }

    @Override
    public FloatingActionButton getFloatingActionButton() {
        return fab;
    }

    @Override
    public void onAddPostButtonClicked() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.coordinator_layout_activity_content, CreatePostFragment.newInstance(groupId))
                .addToBackStack(null)
                .commit();

        appBarLayout.setExpanded(false, false);
        appBarLayout.setActivated(false);

        nestedScrollView.setNestedScrollingEnabled(false);
    }

    @Override
    public void onDataTemplateSaved(Template template) {
        Snackbar.make(nestedScrollView, "Data Template Saved", Snackbar.LENGTH_SHORT)
                // TODO maybe add an action to go edit that template more??
                .show();

        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
    }

    @Override
    public void onPostSaved(Post post) {
        getSupportFragmentManager().popBackStack();

        Snackbar.make(nestedScrollView, "Post saved successfully", Snackbar.LENGTH_SHORT)
                .show();
    }
}
