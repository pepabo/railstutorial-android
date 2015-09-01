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

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

class MicropostsAdapter extends ArrayAdapter<Micropost> {
    final LayoutInflater mInflater;
    final Picasso mPicasso;

    public MicropostsAdapter(Context context, Picasso picasso, List<Micropost> objects) {
        super(context, 0, objects);
        mInflater = LayoutInflater.from(context);
        mPicasso = picasso;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.view_micropost, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final Micropost micropost = getItem(position);
        final User user = micropost.getUser();

        if (user != null) {
            mPicasso.load(user.getAvatarUrl()).fit().into(holder.avatar);
            holder.username.setText(user.getName());
        } else {
            holder.avatar.setImageDrawable(null);
            holder.username.setText("");
        }

        holder.content.setText(micropost.getContent());
        holder.timestamp.setText(formatDate(micropost.getCreatedAt()));

        final Uri pictureUrl = micropost.getPictureUrl();
        if (pictureUrl != null) {
            mPicasso.load(pictureUrl)
                    .resize(400, 400).onlyScaleDown().centerInside()
                    .into(holder.picture);
        } else {
            holder.picture.setImageDrawable(null);
        }

        return view;
    }

    String formatDate(Date date) {
        DateFormat f = DateFormat.getDateTimeInstance();
        return f.format(date);
    }

    static class ViewHolder {
        @Bind(R.id.content)
        TextView content;

        @Bind(R.id.timestamp)
        TextView timestamp;

        @Bind(R.id.username)
        TextView username;

        @Bind(R.id.avatar)
        ImageView avatar;

        @Bind(R.id.picture)
        ImageView picture;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
