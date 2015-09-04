package com.pepabo.jodo.jodoroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.pepabo.jodo.jodoroid.models.APIService;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileEditActivity extends AppCompatActivity
        implements ProfileEditView {

    APIService mAPIService;
    ProfileEditPresenter mPresenter;

    @Bind(R.id.email)
    AutoCompleteTextView mEmailView;

    @Bind(R.id.name)
    TextView mNameView;

    @Bind(R.id.progress)
    View mProgressView;

    @Bind(R.id.form)
    View mFormView;

    ProgressToggle mProgressToggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        ButterKnife.bind(this);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);

        new EmailAutoCompletion().populate(mEmailView);

        mProgressToggle = new ProgressToggle(this, mProgressView, mFormView);

        mAPIService = ((JodoroidApplication) getApplication()).getAPIService();
        mPresenter = new ProfileEditPresenter(getApplicationContext(), this,
                mAPIService, JodoAccount.getAccount(getApplicationContext()));

        mPresenter.start();
    }

    @Override
    protected void onDestroy() {
        mPresenter.stop();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_done:
                updateProfile();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void setNameError(String message) {
        mNameView.setError(message);
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
    public String getName() {
        return mNameView.getText().toString();
    }

    @Override
    public void setEmail(String value) {
        mEmailView.setText(value);
    }

    @Override
    public void setName(String value) {
        mNameView.setText(value);
    }

    @Override
    public void showProgress(boolean show) {
        mProgressToggle.showProgress(show);
    }

    private void updateProfile() {
        mPresenter.submit();
    }

    @Override
    public void onError(Throwable e) {
        Toast.makeText(this, ErrorUtils.getMessage(e), Toast.LENGTH_SHORT).show();
    }
}
