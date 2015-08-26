package com.pepabo.jodo.jodoroid;

import android.app.Fragment;
import android.content.res.Configuration;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.content.Intent;

import com.pepabo.jodo.jodoroid.models.Micropost;
import com.pepabo.jodo.jodoroid.models.User;

import de.psdev.licensesdialog.LicensesDialog;

public class MainActivity extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener,
        MicropostListFragment.OnFragmentInteractionListener,
        UserListFragment.OnFragmentInteractionListener {

    public static final String ACTION_VIEW_HOME = "com.pepabo.jodo.jodoroid.VIEW_HOME";
    public static final String ACTION_VIEW_ALL_USERS = "com.pepabo.jodo.jodoroid.VIEW_ALL_USERS";
    public static final String ACTION_VIEW_FOLLOWERS = "com.pepabo.jodo.jodoroid.VIEW_FOLLOWERS";
    public static final String ACTION_VIEW_FOLLOWING = "com.pepabo.jodo.jodoroid.VIEW_FOLLOWING";
    public static final String ACTION_VIEW_LICENSES = "com.pepabo.jodo.jodoroid.VIEW_LICENSES";
    public static final String EXTRA_USER_ID = "userId";

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationView mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mDrawer = (NavigationView) findViewById(R.id.navigation_drawer);
        mDrawer.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PostMicropost.class);
                startActivity(intent);
            }
        });


        processIntent(getIntent());
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) ||
                super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Micropost micropost) {
        onFragmentInteraction(micropost.getUser());
    }

    @Override
    public void onFragmentInteraction(User user) {
        showFragment(UserProfileFragment.newInstance(user.getId()));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (processIntent(intent)) return;
        super.onNewIntent(intent);
    }

    private boolean processIntent(Intent intent) {
        final String action = intent.getAction() != null
                ? intent.getAction() : ACTION_VIEW_HOME;

        switch (action) {
            case ACTION_VIEW_HOME:
                showHome();
                return true;
            case ACTION_VIEW_ALL_USERS:
                showAllUsers();
                return true;
            case ACTION_VIEW_FOLLOWERS:
                showFollowers(getUserIdFromIntent(intent));
                return true;
            case ACTION_VIEW_FOLLOWING:
                showFollowing(getUserIdFromIntent(intent));
                return true;
            case ACTION_VIEW_LICENSES:
                showLicenses();
                return true;
        }
        return false;
    }

    private void showHome() {
        showFragment(HomeFeedFragment.newInstance());
    }

    private void showAllUsers() {
        showFragment(AllUsersFragment.newInstance());
    }

    private void showFollowers(long userId) {
        showFragment(UserFollowersFragment
                .newInstance(userId, UserFollowersFragment.TYPE_FOLLOWERS));
    }

    private void showFollowing(long userId) {
        showFragment(UserFollowersFragment
                .newInstance(userId, UserFollowersFragment.TYPE_FOLLOWING));
    }

    private void showLicenses() {
        new LicensesDialog.Builder(this)
                .setNotices(R.raw.notices)
                .build()
                .show();
    }

    private void showFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private static long getUserIdFromIntent(Intent intent) {
        long userId = intent.getLongExtra(EXTRA_USER_ID, -1);
        if (userId == -1) {
            throw new RuntimeException("Intent has no EXTRA_USER_ID");
        }
        return userId;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        Intent intent = null;

        switch (menuItem.getItemId()) {
            case R.id.action_view_home:
                intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setAction(ACTION_VIEW_HOME);
                break;
            case R.id.action_view_all_users:
                intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setAction(ACTION_VIEW_ALL_USERS);
                break;
            case R.id.action_view_oss_license:
                intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setAction(ACTION_VIEW_LICENSES);
                break;

        }

        if (intent != null) {
            mDrawerLayout.closeDrawers();
            startActivity(intent);
            return true;
        }
        return false;
    }
}
