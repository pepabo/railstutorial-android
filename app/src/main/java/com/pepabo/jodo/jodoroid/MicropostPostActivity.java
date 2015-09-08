package com.pepabo.jodo.jodoroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
            case R.id.action_Attach:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, REQUEST_GALLERY);
                break;
            case R.id.action_Send:
                showProgress(true);
                postMicropost();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK && null != intent) {

            File file = new File(imagePath(intent));
            mAttachmentImage = new TypedFile("image/*", file);

            try {
                InputStream stream = getContentResolver().openInputStream(intent.getData());
                Bitmap bmp = BitmapFactory.decodeStream(stream);
                stream.close();
                mAttachmentImageView.setImageBitmap(bmp);
            } catch (Exception e) {
            }
        }
    }

    private String imagePath(Intent intent) {
        String[] columns = {MediaStore.Images.Media.DATA};
        Cursor c = getContentResolver().query(intent.getData(), columns, null, null, null);
        c.moveToFirst();
        return c.getString(0);
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
