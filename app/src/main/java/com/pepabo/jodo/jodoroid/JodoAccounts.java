package com.pepabo.jodo.jodoroid;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import com.pepabo.jodo.jodoroid.models.Session;
import com.pepabo.jodo.jodoroid.models.User;

public class JodoAccounts {
    public static Account getAccount(Context context) {
        final AccountManager accountManager = AccountManager.get(context);
        final Account[] accounts = accountManager.getAccountsByType(JodoAuthenticator.ACCOUNT_TYPE);

        if(accounts.length == 0) {
            return null;
        }
        return accounts[0];
    }

    public static void addAccount(Context context, String email, Session session) {
        final Account account = new Account(email, JodoAuthenticator.ACCOUNT_TYPE);
        final AccountManager accountManager = AccountManager.get(context);
        accountManager.addAccountExplicitly(account, null, null);
        accountManager.setAuthToken(account, JodoAuthenticator.ACCOUNT_TOKEN_TYPE, session.getAuthToken());

        final User user = session.getUser();
        accountManager.setUserData(account, JodoAuthenticator.ACCOUNT_ID_TYPE, Long.toString(user.getId()));
    }

    public static boolean isMe(Context context, long userId) {
        final Account account = getAccount(context);
        final AccountManager accountManager = AccountManager.get(context);

        long id = Long.parseLong(accountManager.getUserData(account, JodoAuthenticator.ACCOUNT_ID_TYPE));

        return (userId == id);
    }
}
