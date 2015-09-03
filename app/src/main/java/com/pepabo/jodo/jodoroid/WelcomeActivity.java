package com.pepabo.jodo.jodoroid;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class WelcomeActivity extends Activity {
    private static final int REQUEST_LOGIN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final JodoAccount account = JodoAccount.getAccount(getApplicationContext());

        if(account == null) {
            startLoginActivity();
        } else {
            startMainActivity();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_LOGIN) {
            if(resultCode == RESULT_OK) {
                startMainActivity();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        finish();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
    }

    private void startMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
