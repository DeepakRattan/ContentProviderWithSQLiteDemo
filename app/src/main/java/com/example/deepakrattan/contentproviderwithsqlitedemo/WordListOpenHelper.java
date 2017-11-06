package com.example.deepakrattan.contentproviderwithsqlitedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.example.deepakrattan.contentproviderwithsqlitedemo.Contract.ALL_ITEMS;
import static com.example.deepakrattan.contentproviderwithsqlitedemo.Contract.DATABASE_NAME;
import static com.example.deepakrattan.contentproviderwithsqlitedemo.Contract.WordList.ID;
import static com.example.deepakrattan.contentproviderwithsqlitedemo.Contract.WordList.WORD;
import static com.example.deepakrattan.contentproviderwithsqlitedemo.Contract.WordList.WORD_LIST_TABLE;

/**
 * Created by Deepak Rattan on 05-Nov-17.
 */

public class WordListOpenHelper extends SQLiteOpenHelper {

    public static final String TAG = WordListOpenHelper.class.getSimpleName();
    private Context context;

    //Schema

    public static final int DATABASE_VERSION = 1;


    //Queries
    public static final String CREATE_TABLE_WORD_LIST = "CREATE TABLE " + WORD_LIST_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + WORD + " TEXT );";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS" + WORD_LIST_TABLE;

    private SQLiteDatabase writableDB;
    private SQLiteDatabase readableDB;

    public WordListOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        Log.d(TAG, "WordListOpenHelper: ");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: ");
        db.execSQL(CREATE_TABLE_WORD_LIST);
        fillDatabaseWithData(db);

    }

    private void fillDatabaseWithData(SQLiteDatabase db) {
        String[] words = {"Android", "Adapter", "ListView", "AsyncTask", "Android Studio",
                "SQLiteDatabase", "SQLOpenHelper", "Data model", "ViewHolder",
                "Android Performance", "OnClickListener"};

        ContentValues cv = new ContentValues();
        for (int i = 0; i < words.length; i++) {
            cv.put(WORD, words[i]);
            db.insert(WORD_LIST_TABLE, null, cv);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: ");
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public Cursor query(int position) {
        String query;
        if (position != ALL_ITEMS) {
            position++; // Because database starts counting at 1.
            query = "SELECT " + ID + "," + WORD + " FROM " + WORD_LIST_TABLE +
                    " WHERE " + ID + "=" + position + ";";
        } else {
            query = "SELECT  * FROM " + WORD_LIST_TABLE + " ORDER BY " + WORD + " ASC ";
        }

        Cursor cursor = null;
        try {
            if (readableDB == null) {
                readableDB = this.getReadableDatabase();
            }
            cursor = readableDB.rawQuery(query, null);
        } catch (Exception e) {
            Log.d(TAG, "QUERY EXCEPTION! " + e);
        } finally {
            return cursor;
        }
    }


    public long insert(ContentValues values) {
        long added = 0;
        try {
            if (writableDB == null) {
                writableDB = getWritableDatabase();
            }
            added = writableDB.insert(WORD_LIST_TABLE, null, values);
        } catch (Exception e) {
            Log.d(TAG, "INSERT EXCEPTION " + e);
        }
        return added;
    }

    public Cursor count() {
        MatrixCursor cursor = new MatrixCursor(new String[]{Contract.CONTENT_PATH});
        try {
            if (readableDB == null) {
                readableDB = getReadableDatabase();
            }
            // queryNumEntries returns a long, but we need to pass up an int.
            // With the small number of entries, no worries about losing precision.
            // queryNumEntries return number of rows in the database
            int count = (int) DatabaseUtils.queryNumEntries(readableDB, WORD_LIST_TABLE);
            cursor.addRow(new Object[]{count});
        } catch (Exception e) {
            Log.d(TAG, "COUNT EXCEPTION " + e);
        }
        return cursor;
    }

    public int delete(int id) {
        int deleted = 0;
        try {
            if (writableDB == null) {
                writableDB = getWritableDatabase();
            }
            deleted = writableDB.delete(WORD_LIST_TABLE, //table name
                    ID + " = ? ", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            Log.d(TAG, "DELETE EXCEPTION! " + e.getMessage());
        }
        return deleted;
    }

    public int update(int id, String word) {
        int mNumberOfRowsUpdated = -1;
        try {
            if (writableDB == null) {
                writableDB = getWritableDatabase();
            }
            ContentValues values = new ContentValues();
            values.put(WORD, word);

            mNumberOfRowsUpdated = writableDB.update(WORD_LIST_TABLE, //table to change
                    values, // new values to insert
                    ID + " = ?", // selection criteria for row (in this case, the _id column)
                    new String[]{String.valueOf(id)}); //selection args; the actual value of the id

        } catch (Exception e) {
            Log.d(TAG, "UPDATE EXCEPTION! " + e.getMessage());
        }
        return mNumberOfRowsUpdated;
    }


}
