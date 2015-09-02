package com.pepabo.jodo.jodoroid;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class EmailAutoCompletion {
    public static Observable<List<String>> getEmails(final Context context) {
        return Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                try {
                    subscriber.onNext(readCursor(query()));
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }

            Cursor query() {
                return context.getContentResolver().query(
                        Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                                ContactsContract.Contacts.Data.CONTENT_DIRECTORY),
                        ProfileQuery.PROJECTION,
                        ContactsContract.Contacts.Data.MIMETYPE + " = ?",
                        new String[]{ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE},
                        ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
            }

            List<String> readCursor(Cursor cursor) {
                List<String> list = new ArrayList<>();

                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    list.add(cursor.getString(ProfileQuery.ADDRESS));
                }

                return list;
            }
        });
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
        };

        int ADDRESS = 0;
    }

    public Subscription populate(final AutoCompleteTextView view) {
        return getEmails(view.getContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> emails) {
                        final ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                view.getContext(),
                                android.R.layout.simple_dropdown_item_1line,
                                emails);
                        view.setAdapter(adapter);
                    }
                });
    }
}
