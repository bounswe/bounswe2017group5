package com.karacasoft.interestr;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.karacasoft.interestr.errorhandling.ErrorDialogFragment;
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
        GroupsFragment.OnGroupsListItemClickedListener,
        LoginFragment.OnLoginFragmentInteractionListener,
        SignUpFragment.OnSignupSuccessfulListener,
        FloatingActionsMenuHandler,
        FloatingActionButtonHandler,
        ErrorHandler {

    private FloatingActionButton fab;
    private FloatingActionMenu fam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.hide(false);
        fam = findViewById(R.id.fam);
        fam.hideMenu(false);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//      Fragment operations
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
                .replace(R.id.content, LoginFragment.newInstance())
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
            DataTemplateCreatorFragment fragment = DataTemplateCreatorFragment.newInstance();

            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.content, fragment)
                    .commit();

        } else if (id == R.id.nav_slideshow) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.content, SignUpFragment.newInstance())
                    .commit();
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
    public void onSignUpPressed() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.content, SignUpFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void onSignupSuccessful(User user) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.content,LoginFragment.newInstance())
                .commit();
    }

    @Override
    public FloatingActionMenu getFloatingActionsMenu() {
        return fam;
    }

    @Override
    public void clearFloatingActionsMenu() {
        fam.removeAllMenuButtons();
    }

    @Override
    public void hideFloatingActionsMenu() {
        fam.hideMenu(true);
    }

    @Override
    public void showFloatingActionsMenu() {
        fam.showMenu(true);
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
    public void onError(Throwable t) {
        ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.ErrorDialogFragment(t.getMessage());
        errorDialogFragment.show(getSupportFragmentManager(), "ErrorDialog:" + t.toString());
    }

    @Override
    public void onError(String errorMessage) {
        ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.ErrorDialogFragment(errorMessage);
        errorDialogFragment.show(getSupportFragmentManager(), "ErrorDialog:" + errorMessage);
    }

}
