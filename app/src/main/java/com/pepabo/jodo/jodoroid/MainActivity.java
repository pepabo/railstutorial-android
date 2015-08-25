package com.pepabo.jodo.jodoroid;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.content.Intent;

import com.pepabo.jodo.jodoroid.models.Micropost;
import com.pepabo.jodo.jodoroid.models.User;

public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        MicropostListFragment.OnFragmentInteractionListener,
        UserListFragment.OnFragmentInteractionListener {

    public static final String ACTION_VIEW_FOLLOWERS = "com.pepabo.jodo.jodoroid.VIEW_FOLLOWERS";
    public static final String ACTION_VIEW_FOLLOWING = "com.pepabo.jodo.jodoroid.VIEW_FOLLOWING";
    public static final String EXTRA_USER_ID = "userId";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(NavigationDrawerFragment.Section section) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, section.getFragment(fragmentManager))
                .addToBackStack(null)
                .commit();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Micropost micropost) {
        onFragmentInteraction(micropost.getUser());
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.post_button:
                Intent intent = new Intent(this, PostMicropost.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onFragmentInteraction(User user) {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, UserProfileFragment.newInstance(user.getId()))
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        switch (intent.getAction()) {
            case ACTION_VIEW_FOLLOWERS: {
                long userId = intent.getExtras().getLong(EXTRA_USER_ID);
                if(userId != -1) {
                    showFollowers(userId);
                }
                return;
            }
            case ACTION_VIEW_FOLLOWING: {
                long userId = intent.getExtras().getLong(EXTRA_USER_ID);
                if(userId != -1) {
                    showFollowing(userId);
                }
                return;
            }
        }
        super.onNewIntent(intent);
    }

    private void showFollowers(long userId) {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, UserFollowersFragment
                        .newInstance(userId, UserFollowersFragment.TYPE_FOLLOWERS))
                .addToBackStack(null)
                .commit();
    }

    private void showFollowing(long userId) {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, UserFollowersFragment
                        .newInstance(userId, UserFollowersFragment.TYPE_FOLLOWING))
                .addToBackStack(null)
                .commit();
    }
}
