package com.prasad.safeboxApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Prasad on 3/9/17.
 */

public class noteAdd extends AppCompatActivity {

    private DataBaseHandler db;

    private EditText note;
    private String header,body;
    private String datetime;
    private static final String TABLE_NAME_NOTES="notes";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(noteAdd.this,notesAVDMain.class));
        overridePendingTransitionExit();
    }

    @Override
    protected void onStop() {
        try {
            noteSave();
        } catch (Exception e) {
            Log.e("mainLogout",e.toString());
        }
        super.onStop();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_add_textview);

        try {

            db = new DataBaseHandler(this, null, null, 1);
            note = (EditText) findViewById(R.id.mtxt_notes);

            datetime = DateFormat.getDateTimeInstance().format(new Date());

        }catch (Exception ex){
            Log.e("noteAdd",ex.toString());
        }


    }

    public void noteSave() throws Exception{
            body = note.getText().toString();
            if (body.length() != 0) {
                if (body.length() > 10)
                    header = body.substring(0, 10);
                else
                    header = body;
                boolean flag = db.insertNotes(TABLE_NAME_NOTES, header, body,datetime);
                if (flag == true) {
                    startActivity(new Intent(noteAdd.this, notesAVDMain.class));
                    overridePendingTransitionExit();
                } else {

                }

            } else {

            }
    }

    protected void overridePendingTransitionExit() {
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

}

