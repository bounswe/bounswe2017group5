package com.karacasoft.interestr;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.github.clans.fab.FloatingActionButton;
import com.karacasoft.interestr.errorhandling.ErrorDialogFragment;
import com.karacasoft.interestr.network.models.DataTemplate;
import com.karacasoft.interestr.network.models.Post;
import com.karacasoft.interestr.pages.createpost.CreatePostFragment;
import com.karacasoft.interestr.pages.datatemplates.DataTemplateCreatorFragment;
import com.karacasoft.interestr.pages.groupdetail.GroupDetailFragment;
import com.karacasoft.interestr.pages.profile.ProfileFragment;

public class CoordinatorLayoutActivity extends AppCompatActivity
    implements ErrorHandler,
        ToolbarHandler,
        FloatingActionButtonHandler,
        GroupDetailFragment.OnAddPostButtonClicked,
        CreatePostFragment.OnPostSavedListener,
        DataTemplateCreatorFragment.OnDataTemplateSavedListener,
        CreatePostFragment.OnAddDataTemplateClickedListener {

    public static final String ACTION_DISPLAY_USER ="com.karacasoft.interestr.display_user";
    public static final String EXTRA_USER_ID = "com.karacasoft.interestr.extra_user_id";

    public static final String ACTION_DISPLAY_GROUP ="com.karacasoft.interestr.display_group";
    public static final String EXTRA_GROUP_ID = "com.karacasoft.interestr.extra_group_id";

    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private FrameLayout nestedScrollView;

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

        nestedScrollView = findViewById(R.id.coordinator_layout_outer_frame);

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
            throw new UnsupportedOperationException("Give an action to the activity.");
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

        fm.addOnBackStackChangedListener(() -> {

        });

        appBarLayout.setExpanded(false, true);
        appBarLayout.setActivated(false);

        ViewCompat.setNestedScrollingEnabled(nestedScrollView, false);
    }

    @Override
    public void onPostSaved(Post post) {
        getSupportFragmentManager().popBackStack();

        Snackbar.make(nestedScrollView, R.string.post_saved, Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onAddDataTemplateClicked() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.coordinator_layout_activity_content, DataTemplateCreatorFragment.newInstance(groupId))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDataTemplateSaved(DataTemplate template) {
        Snackbar.make(nestedScrollView, R.string.data_template_saved, Snackbar.LENGTH_SHORT)
                // TODO maybe add an action to go edit that template more??
                .show();

        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
    }
}
