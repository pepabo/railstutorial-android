package com.pepabo.jodo.jodoroid;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.User;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.navigation_drawer)
    NavigationView mDrawer;

    private Subscription mAccountSubscription;
    private User mSelf;

    private APIService mAPIService;
    private Picasso mPicasso;

    @Bind(R.id.drawer_email)
    TextView mDrawerEmail;
    @Bind(R.id.drawer_name)
    TextView mDrawerName;
    @Bind(R.id.drawer_avatar)
    ImageView mDrawerAvatar;

    BroadcastReceiver mLogoutReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawer.setNavigationItemSelectedListener(this);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mDrawerEmail.setText(JodoAccount.getAccount(this).getEmail());

        mAPIService = ((JodoroidApplication) getApplication()).getAPIService();
        mPicasso = ((JodoroidApplication) getApplication()).getPicasso();

        mAccountSubscription = mAPIService
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
                        mPicasso.load(mSelf.getAvatarUrl()).fit()
                                .transform(new StarTransformation(mSelf.isStar()))
                                .into(mDrawerAvatar);
                    }
                });

        registerReceiver(mLogoutReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        }, JodoroidApplication.createLoggedOutIntentFilter());

        if (savedInstanceState == null) {
            processIntent(getIntent());
        }
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        unregisterReceiver(mLogoutReceiver);
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
                .newInstance(userId, UserFollowersPresenter.TYPE_FOLLOWERS));
    }

    private void showFollowing(long userId) {
        showFragment(UserFollowersFragment
                .newInstance(userId, UserFollowersPresenter.TYPE_FOLLOWING));
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

    @OnClick(R.id.fab)
    void newPost() {
        final Intent intent = new Intent(getApplicationContext(), MicropostPostActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.drawer_avatar)
    void openSelfProfile() {
        mDrawerLayout.closeDrawer(mDrawer);
        final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setAction(ACTION_VIEW_SELF_PROFILE);
        startActivity(intent);
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
        if (mDrawerLayout.isDrawerOpen(mDrawer)) {
            mDrawerLayout.closeDrawer(mDrawer);
            return;
        }

        final FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();
            return;
        }

        super.onBackPressed();
    }
}
