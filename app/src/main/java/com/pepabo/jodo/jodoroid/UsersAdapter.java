package com.pepabo.jodo.jodoroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pepabo.jodo.jodoroid.models.Micropost;
import com.pepabo.jodo.jodoroid.models.User;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

class UsersAdapter extends ArrayAdapter<User> {
    LayoutInflater mInflater;

    public UsersAdapter(Context context, List<User> objects) {
        super(context, 0, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = convertView != null ? convertView :
                mInflater.inflate(R.layout.view_user, parent, false);

        final User user = getItem(position);

        new FetchAvatarTask((ImageView) view.findViewById(R.id.imageView_user_avatar)).execute(user.getAvatar());
        ((TextView) view.findViewById(R.id.textView_user_name)).setText(user.getName());

        return view;
    }
}
