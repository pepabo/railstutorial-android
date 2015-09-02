package com.pepabo.jodo.jodoroid;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.Session;

import java.lang.ref.WeakReference;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;

public class AccountActivationActivity extends Activity {

    DialogFragment mDialog;

    APIService mAPIService;
    Subscription mSubscription = Subscriptions.empty();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();

        mAPIService = ((JodoroidApplication) getApplication()).getAPIService();

        mDialog = ProgressDialogFragment.newInstance();

        mSubscription = processActivation(intent.getData());
    }

    @Override
    protected void onDestroy() {
        mSubscription.unsubscribe();
        super.onDestroy();
    }

    Subscription processActivation(Uri uri) {
        final String token = uri.getPathSegments().get(1); // get {id} in "/account_activations/{id}/edit"
        final String email = uri.getQueryParameter("email");

        return mAPIService.activateAccount(token, email)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ActivationSubscriber(this, email));
    }

    void showProgress(final boolean show) {
        if (show) {
            mDialog.show(getFragmentManager(), null);
        } else {
            mDialog.dismiss();
        }
    }

    static class ActivationSubscriber extends Subscriber<Session> {
        final WeakReference<AccountActivationActivity> mActivity;
        final String mEmail;

        public ActivationSubscriber(AccountActivationActivity activity, String email) {
            mActivity = new WeakReference<>(activity);
            mEmail = email;
        }

        @Override
        public void onNext(Session session) {
            final AccountActivationActivity activity = mActivity.get();
            if (activity != null) {
                if(session != null && session.getAuthToken() != null) {
                    JodoAccounts.addAccount(activity, mEmail, session);

                    activity.setResult(RESULT_OK);
                    Toast.makeText(activity, R.string.toast_account_activated, Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onCompleted() {
            final AccountActivationActivity activity = mActivity.get();
            if (activity != null) {
                final Intent intent = new Intent(activity.getApplicationContext(), WelcomeActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        }

        @Override
        public void onError(Throwable e) {
            final AccountActivationActivity activity = mActivity.get();
            if (activity != null) {
                Toast.makeText(activity, ErrorUtils.getMessage(e), Toast.LENGTH_LONG).show();
                activity.finish();
            }
        }

        @Override
        public void onStart() {
            super.onStart();
            final AccountActivationActivity activity = mActivity.get();
            if (activity != null) {
                activity.showProgress(true);
            }
        }
    }

}
