package com.example.deepakrattan.contentproviderwithsqlitedemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Deepak Rattan on 05-Nov-17.
 */

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {
    public static final String TAG = WordListAdapter.class.getSimpleName();
    private Context context;
    private LayoutInflater inflater;
    private WordListOpenHelper db;

    // Query parameters are very similar to SQL queries.
    private String queryUri = Contract.CONTENT_URI.toString();
    private static final String[] projection = new String[]{Contract.CONTENT_PATH};
    private String selectionClause = null;
    private String selectionArgs[] = null;
    private String sortOrder = "ASC";


    public WordListAdapter(Context context, WordListOpenHelper db) {
        this.context = context;
        this.db = db;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.word_list_item, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        // Create a reference to the view holder for the click listener
        // Must be final for use in callback
        final WordViewHolder h = holder;

        String word = "";
        int id = -1;

        Cursor cursor = context.getContentResolver().query(Uri.parse(queryUri), null, null, null, sortOrder);
        if (cursor != null) {
            if (cursor.moveToPosition(position)) {
                int indexWord = cursor.getColumnIndex(Contract.WordList.WORD);
                word = cursor.getString(indexWord);
                holder.txtWord.setText(word);
                int indexId = cursor.getColumnIndex(Contract.WordList.ID);
                id = cursor.getInt(indexId);
            } else {
                holder.txtWord.setText(R.string.error_no_word);
            }
            cursor.close();
        } else {
            Log.e(TAG, "onBindViewHolder: Cursor is null.");
        }

        holder.btnDelete.setOnClickListener(new MyButtonOnClickListener(id, word) {
            @Override
            public void onClick(View v) {
                selectionArgs = new String[]{Integer.toString(id)};

                int deleted = context.getContentResolver().delete(Contract.CONTENT_URI, Contract.CONTENT_PATH,
                        selectionArgs);
                if (deleted > 0) {
                    // Need both calls
                    notifyItemRemoved(h.getAdapterPosition());
                    notifyItemRangeChanged(h.getAdapterPosition(), getItemCount());
                } else {
                    Log.d(TAG, context.getString(R.string.not_deleted) + deleted);
                }

            }
        });

        holder.btnEdit.setOnClickListener(new MyButtonOnClickListener(id, word) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditWordActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("word", word);
                intent.putExtra("position", h.getAdapterPosition());
                ((Activity) (context)).startActivityForResult(intent, 100);
            }
        });

    }

    @Override
    public int getItemCount() {
        Cursor cursor = context.getContentResolver().query(Contract.ROW_COUNT_URI, new String[]{"count(*) AS count"}, selectionClause, selectionArgs, sortOrder);
        try {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            return count;
        } catch (Exception e) {
            Log.d(TAG, "EXCEPTION getItemCount: " + e);
            return -1;
        }
    }

    class WordViewHolder extends RecyclerView.ViewHolder {
        private TextView txtWord;
        private Button btnDelete, btnEdit;

        public WordViewHolder(View itemView) {
            super(itemView);
            txtWord = (TextView) itemView.findViewById(R.id.txtWord);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
            btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
        }
    }




}
