package com.example.deepakrattan.contentproviderwithsqlitedemo;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rv;
    private RecyclerView.LayoutManager layoutManager;
    private WordListAdapter adapter;
    private WordListOpenHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new WordListOpenHelper(this);

        //findViewByID
        rv = (RecyclerView) findViewById(R.id.rv);
        layoutManager = new LinearLayoutManager(this);
        adapter = new WordListAdapter(this, db);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                String word = data.getStringExtra("reply_word");
                int id = data.getIntExtra("reply_id", -99);

                //update the database
                if (word.length() != 0) {
                    ContentValues cv = new ContentValues();
                    cv.put(Contract.WordList.WORD, word);
                    String[] selectionArgs = {String.valueOf(id)};
                    getContentResolver().update(Contract.CONTENT_URI, cv, Contract.WordList.ID, selectionArgs);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, "Word Empty", Toast.LENGTH_SHORT).show();
                }

            }

        }
    }
}
