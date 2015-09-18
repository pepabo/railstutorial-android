package com.pepabo.jodo.jodoroid;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pepabo.jodo.jodoroid.models.Micropost;
import com.pepabo.jodo.jodoroid.models.User;
import com.squareup.picasso.Picasso;

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
    public View getView(int position, View view, final ViewGroup parent) {
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
            mPicasso.load(user.getAvatarUrl()).fit()
                    .transform(new StarTransformation(micropost.isStarred())).into(holder.avatar);
            holder.username.setText(user.getName());
        } else {
            holder.avatar.setImageDrawable(null);
            holder.username.setText("");
        }

        holder.content.setText(micropost.getContent());
        holder.timestamp.setText(formatDate(micropost.getCreatedAt()));

        final Uri pictureUrl = micropost.getPictureUrl();
        if (pictureUrl != null) {
            mPicasso.load(pictureUrl).resize(400, 400).onlyScaleDown().centerInside().into(holder.picture);

            holder.picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView image = new ImageView(getContext());
                    image.setMaxHeight(1000);
                    image.setMaxWidth(1000);
                    mPicasso.load(pictureUrl).resize(1000, 1000).centerInside().into(image);

                    Dialog dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(image);
                    dialog.show();
                }
            });
        } else {
            holder.picture.setImageDrawable(null);
        }

        return view;
    }

    CharSequence formatDate(Date date) {
        final long now = System.currentTimeMillis();
        long time = date.getTime();

        // Display "0 seconds ago" when given date is in future (due to inaccurate system clock).
        if(time > now) {
            time = now;
        }

        return DateUtils.getRelativeTimeSpanString(time, now, DateUtils.SECOND_IN_MILLIS);
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
