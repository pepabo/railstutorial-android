package com.pepabo.jodo.jodoroid;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pepabo.jodo.jodoroid.models.APIService;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResetPasswordRequestActivity extends AppCompatActivity implements ResetPasswordRequestView {

    APIService mAPIService;
    ResetPasswordRequestPresenter mPresenter;

    ProgressToggle mProgressToggle;

    @Bind(R.id.reset_password_request_email)
    EditText mEmailView;

    @Bind(R.id.progress)
    ProgressBar mProgressView;

    @Bind(R.id.reset_password_request_form)
    View mFormView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_request);
        ButterKnife.bind(this);

        mProgressToggle = new ProgressToggle(this, mProgressView, mFormView);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mAPIService = ((JodoroidApplication) getApplication()).getAPIService();
        mPresenter = new ResetPasswordRequestPresenter(getApplicationContext(), mAPIService);
        mPresenter.setView(this);

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

    @OnClick(R.id.reset_password_request_button)
    void submit() {
        sendResetEmail();
    }

    public void sendResetEmail() {
        mPresenter.submit();
    }

    @Override
    public void setEmailError(String message) {
        mEmailView.setError(message);
    }

    @Override
    public String getEmail() {
        return mEmailView.getText().toString();
    }

    @Override
    public void setEmail(String value) {
        mEmailView.setText(value);
    }

    @Override
    public void showProgress(boolean show) {
        mProgressToggle.showProgress(show);
    }

    @Override
    public void onError(Throwable e) {
        Toast.makeText(this, ErrorUtils.getMessage(e), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess() {
        setResult(RESULT_OK);
        Toast.makeText(this, R.string.toast_check_email_reset_password, Toast.LENGTH_LONG).show();
    }

}
