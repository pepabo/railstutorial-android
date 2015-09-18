package com.pepabo.jodo.jodoroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.pepabo.jodo.jodoroid.models.APIService;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;

public class SignupActivity extends AppCompatActivity {
    private static final int REQUEST_SIGNIN = 100;

    @Bind(R.id.email)
    EditText mEmailView;
    @Bind(R.id.name)
    EditText mNameView;
    @Bind(R.id.password)
    EditText mPasswordView;

    @Bind(R.id.form)
    View mFormView;
    @Bind(R.id.progress)
    View mProgressView;

    ProgressToggle mProgressToggle;

    EmailValidator mEmailValidator;
    NameValidator mNameValidator;
    PasswordValidator mPasswordValidator;

    APIService mAPIService;
    Subscription mAPISubscription = Subscriptions.empty();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        mProgressToggle = new ProgressToggle(this, mProgressView, mFormView);

        mEmailValidator = new EmailValidator(getApplicationContext());
        mNameValidator = new NameValidator(getApplicationContext());
        mPasswordValidator = new PasswordValidator(getApplicationContext());

        mAPIService = ((JodoroidApplication) getApplication()).getAPIService();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_SIGNIN) {
            if(resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        mAPISubscription.unsubscribe();

        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @OnClick(R.id.action_sign_up)
    void attemptSignup() {
        // Reset errors.
        mEmailView.setError(null);
        mNameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String name = mNameView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        mPasswordValidator.validate(password);
        if (mPasswordValidator.hasError()) {
            mPasswordView.setError(mPasswordValidator.getErrorMessage());
            focusView = mPasswordView;
            cancel = true;
        }

        mNameValidator.validate(name);
        if (mNameValidator.hasError()) {
            mNameView.setError(mNameValidator.getErrorMessage());
            focusView = mNameView;
            cancel = true;
        }

        mEmailValidator.validate(email);
        if (mEmailValidator.hasError()) {
            mEmailView.setError(mEmailValidator.getErrorMessage());
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            mAPISubscription = mAPIService.signup(name, email, password)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SignupSubscriber(this));
        }
    }

    @OnClick(R.id.sign_in)
    void gotoLogin() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent, REQUEST_SIGNIN);
    }

    void showProgress(final boolean show) {
        mProgressToggle.showProgress(show);
    }

    static class SignupSubscriber extends Subscriber<Void> {
        final WeakReference<SignupActivity> mActivity;

        public SignupSubscriber(SignupActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void onNext(Void unused) {
            final SignupActivity activity = mActivity.get();
            if (activity != null) {
                Toast.makeText(activity, R.string.toast_check_email, Toast.LENGTH_LONG).show();
                activity.finish();
            }
        }

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            final SignupActivity activity = mActivity.get();
            if (activity != null) {
                activity.showProgress(false);
                Toast.makeText(activity, ErrorUtils.getMessage(e), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onStart() {
            super.onStart();
            final SignupActivity activity = mActivity.get();
            if (activity != null) {
                activity.showProgress(true);
            }
        }
    }
}
