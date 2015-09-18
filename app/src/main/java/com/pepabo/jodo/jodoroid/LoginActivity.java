package com.pepabo.jodo.jodoroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.pepabo.jodo.jodoroid.models.Session;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private Subscription mAuthTask = null;

    // UI references.
    @Bind(R.id.email) AutoCompleteTextView mEmailView;
    @Bind(R.id.password) EditText mPasswordView;
    @Bind(R.id.progress) View mProgressView;
    @Bind(R.id.form) View mFormView;

    ProgressToggle mProgressToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mProgressToggle = new ProgressToggle(this, mProgressView, mFormView);

        // Set up the login form.
        new EmailAutoCompletion().populate(mEmailView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnEditorAction(R.id.password)
    boolean onEditorAction(TextView v, int id) {
        if (id == R.id.login || id == EditorInfo.IME_NULL) {
            attemptLogin();
            return true;
        }
        return false;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    @OnClick(R.id.email_sign_in_button)
    void attemptLogin() {
        if (mAuthTask != null && !mAuthTask.isUnsubscribed()) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        FormItemValidator passwordValidator = new PasswordValidator(getApplicationContext());
        passwordValidator.validate(password);

        if (passwordValidator.hasError()) {
            mPasswordView.setError(passwordValidator.getErrorMessage());
            focusView = mPasswordView;
            cancel = true;
        }

        FormItemValidator emailValidator = new EmailValidator(getApplicationContext());
        emailValidator.validate(email);

        if (emailValidator.hasError()) {
            mEmailView.setError(emailValidator.getErrorMessage());
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            mAuthTask = ((JodoroidApplication) getApplication()).getAPIService()
                    .login(email, password)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Session>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            showProgress(false);
                            mPasswordView.setError(getString(R.string.error_incorrect_password));
                            mPasswordView.requestFocus();
                            mAuthTask.unsubscribe();
                        }

                        @Override
                        public void onNext(Session session) {
                            if(session != null && session.getAuthToken() != null) {
                                JodoAccount.addAccount(getApplicationContext(), email, session);
                                setResult(RESULT_OK);
                                finish();
                            }
                        }
                    });
        }
    }

    public void showProgress(final boolean show) {
        mProgressToggle.showProgress(show);
    }
}

