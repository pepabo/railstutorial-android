package com.pepabo.jodo.jodoroid;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.os.Handler;

import com.pepabo.jodo.jodoroid.models.Session;
import com.pepabo.jodo.jodoroid.models.User;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;

public class JodoAccount {

    Account mAccount;
    final AccountManager mAccountManager;

    JodoAccount(Account account, AccountManager accountManager) {
        mAccount = account;
        mAccountManager = accountManager;
    }

    public static JodoAccount getAccount(Context context) {
        final AccountManager accountManager = AccountManager.get(context);
        final Account[] accounts = accountManager.getAccountsByType(JodoAuthenticator.ACCOUNT_TYPE);

        if (accounts.length == 0) {
            return null;
        }

        return new JodoAccount(accounts[0], accountManager);
    }

    public static void addAccount(Context context, String email, Session session) {
        final AccountManager accountManager = AccountManager.get(context);
        final Account account = new Account(email, JodoAuthenticator.ACCOUNT_TYPE);

        accountManager.addAccountExplicitly(account, null, null);
        accountManager.setAuthToken(account, JodoAuthenticator.ACCOUNT_TOKEN_TYPE, session.getAuthToken());

        final User user = session.getUser();
        accountManager.setUserData(account, JodoAuthenticator.ACCOUNT_ID_TYPE, Long.toString(user.getId()));
    }

    public static boolean isMe(Context context, long userId) {
        final JodoAccount account = getAccount(context);
        final AccountManager accountManager = AccountManager.get(context);

        return userId == account.getUserId();
    }

    public String getEmail() {
        return mAccount.name;
    }

    public Observable<JodoAccount> changeEmail(final String newEmail) {
        return Observable.create(new Observable.OnSubscribe<JodoAccount>() {
            @Override
            public void call(Subscriber<? super JodoAccount> subscriber) {
                try {
                    final String email = getEmail();
                    final long userId = getUserId();
                    final String authToken = getAuthToken();

                    final boolean removed = mAccountManager.removeAccount(mAccount, null, null).getResult();
                    if(removed) {
                        mAccount = new Account(email, JodoAuthenticator.ACCOUNT_TYPE);
                        mAccountManager.addAccountExplicitly(mAccount, null, null);
                        mAccountManager.setAuthToken(mAccount, JodoAuthenticator.ACCOUNT_TOKEN_TYPE, authToken);
                        mAccountManager.setUserData(mAccount, JodoAuthenticator.ACCOUNT_ID_TYPE, Long.toString(userId));
                    }
                    subscriber.onNext(JodoAccount.this);
                    subscriber.onCompleted();
                } catch (AuthenticatorException | OperationCanceledException | IOException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    public String getAuthToken() {
        return mAccountManager.peekAuthToken(mAccount, JodoAuthenticator.ACCOUNT_TOKEN_TYPE);
    }

    public long getUserId() {
        return Long.parseLong(mAccountManager.getUserData(mAccount,
                JodoAuthenticator.ACCOUNT_ID_TYPE));
    }
}
