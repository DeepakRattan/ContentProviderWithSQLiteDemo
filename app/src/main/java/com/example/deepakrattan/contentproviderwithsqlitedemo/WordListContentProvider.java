package com.example.deepakrattan.contentproviderwithsqlitedemo;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Deepak Rattan on 05-Nov-17.
 */

public class WordListContentProvider extends ContentProvider {
    public static final String TAG = WordListContentProvider.class.getSimpleName();
    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private WordListOpenHelper db;

    /*Declare the codes for the URI matcher as constants.
    This puts the codes in one place and makes them easy to change.
    Use tens, so that inserting additional codes is straightforward.*/

    private static final int URI_ALL_ITEMS_CODE = 10;
    private static final int URI_ONE_ITEM_CODE = 20;
    private static final int URI_COUNT_CODE = 30;

    @Override
    public boolean onCreate() {
        db = new WordListOpenHelper(getContext());
        initializeUriMatching();
        return true;
    }

    private void initializeUriMatching() {
        uriMatcher.addURI(Contract.AUTHORITY, Contract.CONTENT_PATH, URI_ALL_ITEMS_CODE);
        uriMatcher.addURI(Contract.AUTHORITY, Contract.CONTENT_PATH + "/#", URI_ONE_ITEM_CODE);
        uriMatcher.addURI(Contract.AUTHORITY, Contract.CONTENT_PATH + "/" + Contract.COUNT, URI_COUNT_CODE);
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case URI_ALL_ITEMS_CODE:
                cursor = db.query(Contract.ALL_ITEMS);
                break;
            case URI_ONE_ITEM_CODE:
                String last = uri.getLastPathSegment();
                cursor = db.query(Integer.parseInt(last));
                break;
            case URI_COUNT_CODE:
                cursor = db.count();
                break;
            case UriMatcher.NO_MATCH:
                Log.d(TAG, "No match");
                break;
            default:
                Log.d(TAG, "Uri not recognized " + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI_ALL_ITEMS_CODE:
                return Contract.MULTIPLE_RECORDS_MIME_TYPE;
            case URI_ONE_ITEM_CODE:
                return Contract.SINGLE_RECORD_MIME_TYPE;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long id = db.insert(values);
        return Uri.parse(Contract.CONTENT_URI + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return db.delete(Integer.parseInt(selectionArgs[0]));
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return db.update(Integer.parseInt(selectionArgs[0]), values.getAsString("word"));
    }
}
