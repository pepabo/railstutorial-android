package com.pepabo.jodo.jodoroid;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Intent;
import android.os.Bundle;

public class JodoAuthenticator extends AbstractAccountAuthenticator {
    public static final String ACCOUNT_TYPE = "com.pepabo.jodo";
    public static final String ACCOUNT_TOKEN_TYPE = "com.pepabo.jodo.auth_token";
    public static final String ACCOUNT_ID_TYPE    = "com.pepabo.jodo.user_id";

    final JodoroidApplication mApp;

    public JodoAuthenticator(JodoroidApplication app) {
        super(app);
        mApp = app;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        final AccountManager accountManager = AccountManager.get(mApp);
        final Account[] accounts = accountManager.getAccountsByType(JodoAuthenticator.ACCOUNT_TYPE);

        if(accounts.length == 0) {
            final Intent intent = new Intent(mApp, LoginActivity.class);
            intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

            final Bundle bundle = new Bundle();
            bundle.putParcelable(AccountManager.KEY_INTENT, intent);
            return bundle;
        } else {
            // TODO: Show message
            return null;
        }
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        return null;
    }
}
