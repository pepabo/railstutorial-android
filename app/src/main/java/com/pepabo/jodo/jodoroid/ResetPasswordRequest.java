package com.pepabo.jodo.jodoroid;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class ResetPasswordRequest extends AppCompatActivity {

    private Activity activity;

    @Bind(R.id.reset_password_request_email) EditText ResetPasswordRequestEmail;
    @Bind(R.id.reset_password_request_button) Button ResetPasswordRequestButton;
    @Bind(R.id.progress) ProgressBar ProgressView;
    @Bind(R.id.reset_password_request_form) View ResetPasswordRequestForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_request);

        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

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

    void showProgress(final boolean show) {
        final int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        ResetPasswordRequestForm.setVisibility(show ? View.GONE : View.VISIBLE);
        ResetPasswordRequestForm.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ResetPasswordRequestForm.setVisibility(show ? View.GONE : View.VISIBLE);
                    }
                });

        ProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        ProgressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    }
                });
    }

    @OnClick(R.id.reset_password_request_button)
    void sendResetEmail() {
        final String email = ResetPasswordRequestEmail.getText().toString();
        showProgress(true);
        ((JodoroidApplication) getApplication()).getAPIService()
                .requestPasswordReset(email)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Void>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showProgress(false);
                        Toast.makeText(ResetPasswordRequest.this, ErrorUtils.getMessage(e), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        setResult(RESULT_OK);
                        Toast.makeText(ResetPasswordRequest.this, R.string.toast_check_email_reset_password, Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }

}
