package com.pepabo.jodo.jodoroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pepabo.jodo.jodoroid.models.Micropost;
import com.squareup.picasso.Picasso;

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

        mPicasso.load(micropost.getUser().getAvatar()).fit().into((ImageView) view.findViewById(R.id.avatar));

        ((TextView) view.findViewById(R.id.username)).setText(micropost.getUser().getName());
        ((TextView) view.findViewById(R.id.content)).setText(micropost.getContent());
        ((TextView) view.findViewById(R.id.timestamp)).setText(formatDate(micropost.getCreatedAt()));

        return view;
    }

    String formatDate(Date date) {
        DateFormat f = DateFormat.getDateTimeInstance();
        return f.format(date);
    }
}
