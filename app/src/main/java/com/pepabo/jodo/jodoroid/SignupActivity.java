package com.pepabo.jodo.jodoroid;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        final FormItemValidator passwordValidator = new PasswordValidator(getApplicationContext());
        passwordValidator.validate(password);

        if (passwordValidator.hasError()) {
            mSignupPasswordView.setError(passwordValidator.getErrorMessage());
            focusView = mSignupPasswordView;
            cancel = true;
        }

        final FormItemValidator nameValidator = new NameValidator(getApplicationContext());
        nameValidator.validate(name);

        if (nameValidator.hasError()) {
            mSignupNameView.setError(nameValidator.getErrorMessage());
            focusView = mSignupNameView;
            cancel = true;
        }

        final FormItemValidator emailValidator = new EmailValidator(getApplicationContext());
        emailValidator.validate(email);

        if (emailValidator.hasError()) {
            mSignupEmailView.setError(emailValidator.getErrorMessage());
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
