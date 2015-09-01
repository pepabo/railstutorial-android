package com.pepabo.jodo.jodoroid;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

public class JodoAccounts {
    public static Account getAccount(Context context) {
        final AccountManager accountManager = AccountManager.get(context);
        final Account[] accounts = accountManager.getAccountsByType(JodoAuthenticator.ACCOUNT_TYPE);

        if(accounts.length == 0) {
            return null;
        }
        return accounts[0];
    }

    public static void addAccount(Context context, String email, String authToken) {
        final Account account = new Account(email, JodoAuthenticator.ACCOUNT_TYPE);
        final AccountManager accountManager = AccountManager.get(context);
        accountManager.addAccountExplicitly(account, null, null);
        accountManager.setAuthToken(account, JodoAuthenticator.ACCOUNT_TOKEN_TYPE, authToken);
    }
}
