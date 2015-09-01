package com.pepabo.jodo.jodoroid;

import com.pepabo.jodo.jodoroid.models.APIService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pepabo.jodo.jodoroid.models.Micropost;

import java.io.File;
import java.io.InputStream;
import java.net.URI;

import retrofit.mime.TypedFile;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class PostMicropost extends AppCompatActivity {
    public final static int REQUEST_GALLERY = 0;
    private EditText txt;
    private TypedFile imgtype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_micropost);

        txt = (EditText) findViewById(R.id.editText);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_cancel);
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
                postMicropost();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(requestCode == REQUEST_GALLERY && resultCode == RESULT_OK && null != intent) {

            TextView imguri = (TextView) findViewById(R.id.imguri);
            imguri.setText(intent.getData().getPath());
            File file = new File(intent.getDataString());
            String type = intent.getType();
            imgtype = new TypedFile("image/*", file);

            ImageView imgview = (ImageView) findViewById(R.id.imgview);
            try {
                InputStream stream = getContentResolver().openInputStream(intent.getData());
                Bitmap bmp = BitmapFactory.decodeStream(stream);
                stream.close();
                imgview.setImageBitmap(bmp);
            } catch (Exception e) {
            }
        }
    }

    private void postMicropost() {

        ((JodoroidApplication) getApplication()).getAPIService()
                .createMicropost(txt.getText().toString(), imgtype)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Micropost>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Micropost micropost) {

                    }
                });
    }

}
