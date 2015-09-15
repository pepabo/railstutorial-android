package com.pepabo.jodo.jodoroid;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pepabo.jodo.jodoroid.models.APIService;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResetPasswordActivity extends AppCompatActivity implements ResetPasswordView{

    APIService mAPIService;
    ResetPasswordPresenter mPresenter;

    ProgressToggle mProgressToggle;

    private String token;
    private String email;

    @Bind(R.id.new_password)
    EditText mPasswordView;

    @Bind(R.id.progress)
    ProgressBar mProgressView;

    @Bind(R.id.reset_password_form)
    View mFormView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);

        mProgressToggle = new ProgressToggle(this, mProgressView, mFormView);

        mAPIService = ((JodoroidApplication) getApplication()).getAPIService();
        mPresenter = new ResetPasswordPresenter(getApplicationContext(), mAPIService);
        mPresenter.setView(this);

        final Intent intent = getIntent();
        token = intent.getData().getPathSegments().get(1);
        email = intent.getData().getQueryParameter("email");

    }

    @OnClick(R.id.reset_password_button)
    void submit() {
        resetPassword();
    }

    public void resetPassword() {
        mPresenter.submit();
    }

    @Override
    public void setPasswordError(String message) {
        mPasswordView.setError(message);
    }

    @Override
    public String getPassword() {
        return mPasswordView.getText().toString();
    }

    @Override
    public void setPassword(String value) {
        mPasswordView.setText(value);
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override

    public Activity getActivity() {
        return this;
    }

    @Override
    public JodoroidApplication getJodoroidApplication() {
        return (JodoroidApplication) getApplication();
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
        Toast.makeText(this, R.string.toast_password_reset, Toast.LENGTH_LONG).show();
    }

}
