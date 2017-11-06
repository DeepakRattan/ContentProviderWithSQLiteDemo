package com.example.deepakrattan.contentproviderwithsqlitedemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Deepak Rattan on 06-Nov-17.
 */

public class EditWordActivity extends AppCompatActivity {

    private Button btnSaveWord;
    private EditText edtWord;
    private int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_word);

        //findViewByID
        edtWord = (EditText) findViewById(R.id.edtWord);
        btnSaveWord = (Button) findViewById(R.id.btnSaveWord);

        //Get data sent from calling activity
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getInt("id", 0);
            String word = bundle.getString("word", null);
            edtWord.setText(word);
        }

        btnSaveWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = edtWord.getText().toString().trim();
                Intent replyIntent = new Intent();
                replyIntent.putExtra("reply_word", word);
                replyIntent.putExtra("reply_id", id);
                setResult(RESULT_OK, replyIntent);
                finish();
            }
        });

    }
}
