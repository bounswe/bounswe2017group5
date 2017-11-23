package com.karacasoft.interestr;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.karacasoft.interestr.network.models.Group;
import com.karacasoft.interestr.network.models.Token;
import com.karacasoft.interestr.network.models.User;
import com.karacasoft.interestr.pages.datatemplates.DataTemplateCreatorFragment;
import com.karacasoft.interestr.pages.groups.GroupsFragment;
import com.karacasoft.interestr.pages.login.LoginFragment;
import com.karacasoft.interestr.pages.newsfeed.NewsFeedFragment;
import com.karacasoft.interestr.pages.search.SearchFragment;
import com.karacasoft.interestr.pages.signup.SignUpFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GroupsFragment.OnListFragmentInteractionListener,
        LoginFragment.OnLoginSuccessfulListener,
        SignUpFragment.OnSignupSuccessfulListener{

    public static final String ACTION_ADD_SHORT_TEXT = "short_text";
    public static final String ACTION_ADD_BOOLEAN = "boolean";

    private OnFabClickedListener listener;
    private OnFamActionClickedListener onFamActionClickedListener;

    private void setupFloatingActionsMenu(FloatingActionsMenu menu) {
        com.getbase.floatingactionbutton.FloatingActionButton buttonAddShortText =
                new com.getbase.floatingactionbutton.FloatingActionButton(getBaseContext());
        buttonAddShortText.setTitle("Short Text");
        buttonAddShortText.setIcon(R.drawable.ic_short_text_white_24dp);
        buttonAddShortText.setOnClickListener((view) -> {
            if(onFamActionClickedListener != null) {
                onFamActionClickedListener.onFamActionClicked(ACTION_ADD_SHORT_TEXT);
            }
        });

        com.getbase.floatingactionbutton.FloatingActionButton buttonAddBooleanField =
                new com.getbase.floatingactionbutton.FloatingActionButton(getBaseContext());
        buttonAddBooleanField.setTitle("Check Box");
        buttonAddBooleanField.setIcon(R.drawable.ic_check_box_white_24dp);
        buttonAddBooleanField.setOnClickListener((view) -> {
            onFamActionClickedListener.onFamActionClicked(ACTION_ADD_BOOLEAN);
        });

        menu.addButton(buttonAddShortText);
        menu.addButton(buttonAddBooleanField);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            if(listener != null) {
                listener.onFabClicked();
            }
        });
        fab.hide();



        FloatingActionsMenu fam = findViewById(R.id.fam);
        setupFloatingActionsMenu(fam);
        fam.setVisibility(View.GONE);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // Fragment operations
//        FragmentManager fm = getSupportFragmentManager();
//        DataTemplateCreatorFragment fragment = DataTemplateCreatorFragment.newInstance();
//
//        fm.beginTransaction()
//                .replace(R.id.content, fragment)
//                .commit();
//
//        onFamActionClickedListener = fragment;

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.content, DataTemplateCreatorFragment.newInstance())
                .commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id ==R.id.action_search){
            //redirect to search page
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.content, SearchFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_groups) {
            // redirect to groups page
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.content, GroupsFragment.newInstance(1))
                    .commit();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(Group item) {

    }

    @Override
    public void onLoginSuccessful(Token token) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.content, new NewsFeedFragment())
                .commit();
    }


    @Override
    public void onSignupSuccessful(User user) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.content,LoginFragment.newInstance())
                .commit();
    }

    public interface OnFabClickedListener {
        void onFabClicked();
    }

    public interface OnFamActionClickedListener {
        void onFamActionClicked(String action);
    }
}
