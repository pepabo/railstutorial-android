package com.pepabo.jodo.jodoroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.Session;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;

public class ResetPassword extends AppCompatActivity {

    private APIService mAPIService;
    private String token;
    private String email;
    //Subscription mSubscription = Subscriptions.empty();


    @Bind(R.id.new_password) EditText NewPasswordView;
    @Bind(R.id.reset_password_button) Button ResetPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        ButterKnife.bind(this);

        final Intent intent = getIntent();

        mAPIService = ((JodoroidApplication) getApplication()).getAPIService();
        token = intent.getData().getPathSegments().get(1);
        System.out.println("################" + token);
        email = intent.getData().getQueryParameter("email");

        //mSubscription = processResetPassword(intent.getData());
    }

    /*Subscription processResetPassword(Uri uri) {
        final String password = NewPasswordView.getText().toString();
        final String email = uri.getQueryParameter("email");

        return mAPIService.resetPassword(password, email)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Session>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Session session) {

                    }
                });
    }*/


    @OnClick(R.id.reset_password_button)
    void resetPassword() {
        final String password = NewPasswordView.getText().toString();

        mAPIService
                .resetPassword(token, email, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Session>() {
                    @Override
                    public void onCompleted() {
                        final Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ResetPassword.this, ErrorUtils.getMessage(e), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Session session) {
                        Activity activity = ResetPassword.this;
                        JodoAccounts.addAccount(activity, email, session);
                        Toast.makeText(activity, R.string.toast_account_activated, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
