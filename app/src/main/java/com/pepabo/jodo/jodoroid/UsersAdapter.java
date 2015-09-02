package com.pepabo.jodo.jodoroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pepabo.jodo.jodoroid.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

class UsersAdapter extends ArrayAdapter<User> {
    final LayoutInflater mInflater;
    final Picasso mPicasso;

    public UsersAdapter(Context context, Picasso picasso, List<User> objects) {
        super(context, 0, objects);
        mInflater = LayoutInflater.from(context);
        mPicasso = picasso;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.view_user, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final User user = getItem(position);

        mPicasso.load(user.getAvatarUrl()).fit().into(holder.avatar);
        holder.userName.setText(user.getName());

        return view;
    }

    static class ViewHolder {
        @Bind(R.id.imageView_user_avatar)
        ImageView avatar;

        @Bind(R.id.textView_user_name)
        TextView userName;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
