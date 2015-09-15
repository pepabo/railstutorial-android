package com.pepabo.jodo.jodoroid;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.OnAccountsUpdateListener;
import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.modules.AndroidModule;
import com.squareup.picasso.Picasso;

public class JodoroidApplication extends Application implements OnAccountsUpdateListener {

    public static final String ACTION_LOGGED_OUT = "com.pepabo.jodo.jodoroid.LOGGED_OUT";

    ApplicationComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mComponent = DaggerApplicationComponent.builder()
                .androidModule(new AndroidModule(this))
                .build();

        AccountManager.get(this).addOnAccountsUpdatedListener(this, null, true);
    }

    public ApplicationComponent component() {
        return mComponent;
    }

    public Picasso getPicasso() {
        return component().picasso();
    }

    public APIService getAPIService() {
        return component().apiService();
    }

    @Override
    public void onAccountsUpdated(Account[] accounts) {
        boolean found = false;

        for (Account account : accounts) {
            if (account.type.equals(JodoAuthenticator.ACCOUNT_TYPE)) {
                found = true;
            }
        }

        if (!found) {
            sendBroadcast(new Intent(ACTION_LOGGED_OUT));
        }
    }

    public static IntentFilter createLoggedOutIntentFilter() {
        return new IntentFilter(ACTION_LOGGED_OUT);
    }
}
