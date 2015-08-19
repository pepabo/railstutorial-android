package com.pepabo.jodo.jodoroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pepabo.jodo.jodoroid.models.Micropost;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

class MicropostsAdapter extends ArrayAdapter<Micropost> {
    LayoutInflater mInflater;
    List<Micropost> mObjects;

    public MicropostsAdapter(Context context, List<Micropost> objects) {
        super(context, 0, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mObjects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = convertView != null ? convertView :
                mInflater.inflate(R.layout.view_micropost, parent, false);

        final Micropost micropost = mObjects.get(position);

        new FetchAvatarTask((ImageView) view.findViewById(R.id.avatar)).execute(micropost.getUser().getAvatar());

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
