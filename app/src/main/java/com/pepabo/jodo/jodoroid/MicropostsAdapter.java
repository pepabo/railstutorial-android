package com.pepabo.jodo.jodoroid;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pepabo.jodo.jodoroid.models.Micropost;
import com.pepabo.jodo.jodoroid.models.User;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

class MicropostsAdapter extends ArrayAdapter<Micropost> {
    LayoutInflater mInflater;
    Picasso mPicasso;

    public MicropostsAdapter(Context context, Picasso picasso, List<Micropost> objects) {
        super(context, 0, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPicasso = picasso;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = convertView != null ? convertView :
                mInflater.inflate(R.layout.view_micropost, parent, false);

        final Micropost micropost = getItem(position);

        final User user = micropost.getUser();
        if (user != null) {
            mPicasso.load(user.getAvatarUrl()).fit().into((ImageView) view.findViewById(R.id.avatar));
            ((TextView) view.findViewById(R.id.username)).setText(user.getName());
        } else {
            ((ImageView) view.findViewById(R.id.avatar)).setImageDrawable(null);
            ((TextView) view.findViewById(R.id.username)).setText("");
        }

        ((TextView) view.findViewById(R.id.content)).setText(micropost.getContent());
        ((TextView) view.findViewById(R.id.timestamp)).setText(formatDate(micropost.getCreatedAt()));

        if (micropost.getPictureUrl() != null) {
            // Workaround
            final Uri uri = Uri.parse(URI.create(JodoroidApplication.ENDPOINT)
                    .resolve(micropost.getPictureUrl().toString()).toString());

            mPicasso.load(uri)
                    .resize(400, 400).onlyScaleDown().centerInside()
                    .into((ImageView) view.findViewById(R.id.picture));
        } else {
            ((ImageView) view.findViewById(R.id.picture)).setImageDrawable(null);
        }

        return view;
    }

    String formatDate(Date date) {
        DateFormat f = DateFormat.getDateTimeInstance();
        return f.format(date);
    }
}
