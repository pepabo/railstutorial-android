package com.pepabo.jodo.jodoroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.pepabo.jodo.jodoroid.models.Micropost;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.mime.TypedFile;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class MicropostPostActivity extends AppCompatActivity {
    public final static int REQUEST_GALLERY = 0;

    @Bind(R.id.article)
    EditText mArticleView;

    @Bind(R.id.post_form)
    View mFormView;

    @Bind(R.id.post_progress)
    View mProgressView;

    @Bind(R.id.imgview)
    ImageView mAttachmentImageView;

    private TypedFile mAttachmentImage;

    BroadcastReceiver mLoggoutReceiver;

    ProgressToggle mProgressToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_micropost);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);

        mProgressToggle = new ProgressToggle(this, mProgressView, mFormView);

        registerReceiver(mLoggoutReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        }, JodoroidApplication.createLoggedOutIntentFilter());
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mLoggoutReceiver);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_micropost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_Attach: {
                final Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_GALLERY);
                break;
            }
            case R.id.action_Send:
                showProgress(true);
                postMicropost();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_GALLERY) {
            if (resultCode == RESULT_OK && null != intent) {
                mAttachmentImage = asTypedFile(intent.getData());

                try {
                    final InputStream in = mAttachmentImage.in();
                    try {
                        final Bitmap bitmap = BitmapFactory.decodeStream(mAttachmentImage.in());
                        mAttachmentImageView.setImageBitmap(bitmap);
                    } finally {
                        in.close();
                    }
                } catch (IOException e) {
                    // ignore
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }

    private TypedFile asTypedFile(Uri uri) {
        final String[] columns = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.MIME_TYPE};
        final Cursor c = getContentResolver().query(uri, columns, null, null, null);
        try {
            c.moveToFirst();
            final String path = c.getString(0);
            final String mimeType = c.getString(1);
            return new TypedFile(mimeType, new File(path));
        } finally {
            c.close();
        }
    }

    private void postMicropost() {
        ((JodoroidApplication) getApplication()).getAPIService()
                .createMicropost(mArticleView.getText().toString(), mAttachmentImage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Micropost>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showProgress(false);
                    }

                    @Override
                    public void onNext(Micropost micropost) {
                        setResult(RESULT_OK);
                        finish();
                    }
                });
    }

    public void showProgress(final boolean show) {
        mProgressToggle.showProgress(show);
    }

}
