package com.example.deepakrattan.contentproviderwithsqlitedemo;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Deepak Rattan on 05-Nov-17.
 */

/**
 * Contract class for the word list content provider.
 */

public class Contract {
    public static final String TAG = Contract.class.getSimpleName();

    private Contract() {
    }

    public static final int ALL_ITEMS = -2;
    public static final String COUNT = "count";

    public static final String AUTHORITY = "com.example.deepakrattan.contentproviderwithsqlitedemo.provider";
    // Only one public table.
    public static final String CONTENT_PATH = "words";

    // Content URI for this table. Returns all items.
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + CONTENT_PATH);

    // URI to get the number of entries.
    public static final Uri ROW_COUNT_URI = Uri.parse("content://" + AUTHORITY + "/" + CONTENT_PATH + "/" + COUNT);

    static final String SINGLE_RECORD_MIME_TYPE = "vnd.android.cursor.item/vnd.com.example.provider.words";
    static final String MULTIPLE_RECORDS_MIME_TYPE = "vnd.android.cursor.dir/vnd.com.example.provider.words";

    public static final String DATABASE_NAME = "word_list";

    /**
     * Inner class that defines the table contents.
     * <p>
     * By implementing the BaseColumns interface, your inner class can inherit a primary
     * key field called _ID that some Android classes such as cursor adaptors will expect it to
     * have. It's not required, but this can help your database work harmoniously with the
     * Android framework.
     */
    public static abstract class WordList implements BaseColumns {

        public static final String WORD_LIST_TABLE = "word_entries";

        //Column names
        public static final String ID = "_id";
        public static final String WORD = "word";

    }


}
