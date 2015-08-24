package com.pepabo.jodo.jodoroid;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class FetchAvatarTask extends AsyncTask<URI, Void, Drawable> {
    ImageView mImageView;

    public FetchAvatarTask(ImageView imageView) {
        mImageView = imageView;
    }

    @Override
    protected Drawable doInBackground(URI... params) {
        URI avatarUri = params[0];
        Log.i(FetchAvatarTask.class.getName(), String.format("Downloading %s", avatarUri));
        try {
            InputStream stream = (InputStream) avatarUri.toURL().getContent();
            try {
                return Drawable.createFromStream(stream, avatarUri.toString());
            } finally {
                stream.close();
            }
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Drawable drawable) {
        mImageView.setImageDrawable(drawable);
    }
}
