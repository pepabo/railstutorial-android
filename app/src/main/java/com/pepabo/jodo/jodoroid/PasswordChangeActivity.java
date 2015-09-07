package com.pepabo.jodo.jodoroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pepabo.jodo.jodoroid.models.APIService;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PasswordChangeActivity extends AppCompatActivity implements PasswordChangeView {
    APIService mAPIService;
    PasswordChangePresenter mPresenter;

    ProgressToggle mProgressToggle;

    @Bind(R.id.password)
    TextView mPasswordView;

    @Bind(R.id.form)
    View mFormView;

    @Bind(R.id.progress)
    View mProgressView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        ButterKnife.bind(this);

        mProgressToggle = new ProgressToggle(this, mProgressView, mFormView);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);

        mAPIService = ((JodoroidApplication) getApplication()).getAPIService();
        mPresenter = new PasswordChangePresenter(getApplicationContext(), mAPIService);
        mPresenter.setView(this);
    }

    @Override
    protected void onDestroy() {
        mPresenter.setView(null);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_done:
                updatePassword();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_password_change, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void updatePassword() {
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
    public void showProgress(boolean show) {
        mProgressToggle.showProgress(show);
    }

    @Override
    public void onError(Throwable e) {
        Toast.makeText(this, ErrorUtils.getMessage(e), Toast.LENGTH_SHORT).show();
    }
}
