package com.pepabo.jodo.jodoroid;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Configuration;
import android.media.Image;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.pepabo.jodo.jodoroid.models.Micropost;
import com.pepabo.jodo.jodoroid.models.User;

import de.psdev.licensesdialog.LicenseResolver;
import de.psdev.licensesdialog.LicensesDialog;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String ACTION_VIEW_HOME = "com.pepabo.jodo.jodoroid.VIEW_HOME";
    public static final String ACTION_VIEW_SELF_PROFILE = "com.pepabo.jodo.jodoroid.VIEW_SELF_PROFILE";
    public static final String ACTION_VIEW_USER_PROFILE = "com.pepabo.jodo.jodoroid.VIEW_USER_PROFILE";
    public static final String ACTION_VIEW_ALL_USERS = "com.pepabo.jodo.jodoroid.VIEW_ALL_USERS";
    public static final String ACTION_VIEW_FOLLOWERS = "com.pepabo.jodo.jodoroid.VIEW_FOLLOWERS";
    public static final String ACTION_VIEW_FOLLOWING = "com.pepabo.jodo.jodoroid.VIEW_FOLLOWING";
    public static final String ACTION_VIEW_LICENSES = "com.pepabo.jodo.jodoroid.VIEW_LICENSES";
    public static final String EXTRA_USER_ID = "userId";

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationView mDrawer;

    private Subscription mAccountSubscription;
    private User mSelf;
    private TextView mDrawerEmail;
    private TextView mDrawerName;
    private ImageView mDrawerAvatar;

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
        mDrawer.inflateHeaderView(R.layout.view_drawer_accounts);
        mDrawer.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PostMicropost.class);
                startActivity(intent);
            }
        });

        mDrawerEmail = (TextView) mDrawer.findViewById(R.id.email);
        mDrawerName = (TextView) mDrawer.findViewById(R.id.name);
        mDrawerAvatar = (ImageView) mDrawer.findViewById(R.id.avatar);

        mDrawerEmail.setText(JodoroidApplication.getAccount(this).name);
        mDrawerAvatar.setClickable(true);
        mDrawerAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setAction(ACTION_VIEW_SELF_PROFILE);
                mDrawerLayout.closeDrawer(mDrawer);
                startActivity(intent);
            }
        });

        final JodoroidApplication app = (JodoroidApplication) getApplication();
        mAccountSubscription = app.getAPIService()
                .fetchMe(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(User user) {
                        mSelf = user;
                        mDrawerName.setText(mSelf.getName());
                        app.getPicasso().load(mSelf.getAvatarUrl()).fit().into(mDrawerAvatar);
                    }
                });

        processIntent(getIntent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mAccountSubscription != null) {
            mAccountSubscription.unsubscribe();
        }
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
            case ACTION_VIEW_SELF_PROFILE:
                showSelf();
                return true;
            case ACTION_VIEW_USER_PROFILE:
                showUser(getUserIdFromIntent(intent));
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

    static {
        LicenseResolver.registerLicense(new CreativeCommonsAttribution40());
    }

    private void showLicenses() {
        new LicensesDialog.Builder(this)
                .setNotices(R.raw.notices)
                .build()
                .show();
    }

    private void showUser(long userId) {
        showFragment(UserProfileFragment.newInstance(userId));
    }

    private void showSelf() {
        if (mSelf != null) {
            showFragment(UserProfileFragment.newInstance(mSelf.getId()));
        }
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
            case R.id.action_edit_profile:
                intent = new Intent(getApplicationContext(), ProfileEditActivity.class);
                break;
            case R.id.action_change_password:
                intent = new Intent(getApplicationContext(), PasswordChangeActivity.class);
                break;
        }

        if (intent != null) {
            mDrawerLayout.closeDrawers();
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        final FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
