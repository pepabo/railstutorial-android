package com.pepabo.jodo.jodoroid;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignupActivity extends AppCompatActivity {

    private EditText mSignupEmailView;
    private EditText mSignupNameView;
    private EditText mSignupPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mSignupEmailView = (EditText) findViewById(R.id.signup_email);
        mSignupNameView = (EditText) findViewById(R.id.signup_name);
        mSignupPasswordView = (EditText) findViewById(R.id.signup_password);

        Button mCreateAccountButton = (Button) findViewById(R.id.action_signup);
        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignup();
            }
        });

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void attemptSignup() {
        // Reset errors.
        mSignupEmailView.setError(null);
        mSignupNameView.setError(null);
        mSignupPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mSignupEmailView.getText().toString();
        final String name = mSignupNameView.getText().toString();
        final String password = mSignupPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            mSignupPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mSignupPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(name)) {
            mSignupNameView.setError(getString(R.string.error_field_required));
            focusView = mSignupNameView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mSignupEmailView.setError(getString(R.string.error_field_required));
            focusView = mSignupEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mSignupEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mSignupEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
