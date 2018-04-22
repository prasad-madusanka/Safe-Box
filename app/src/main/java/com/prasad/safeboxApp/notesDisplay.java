package com.prasad.safeboxApp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Prasad on 3/10/17.
 */

public class notesDisplay extends AppCompatActivity {

    private DataBaseHandler db;
    private Bundle bundle;

    private EditText note;
    private TextView DT;

    private static final String TABLE_NAME_NOTES="notes";
    private String temp_note;
    private String datetime;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(notesDisplay.this,notesAVDMain.class));
        overridePendingTransitionExit();
    }

    @Override
    protected void onStop() {
        try {
            notesUpdate();
        } catch (Exception e) {
            Log.e("noteAdd",e.toString());
        }
        super.onStop();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_show_textview);

        try {

            db = new DataBaseHandler(this, null, null, 1);
            bundle = getIntent().getExtras();
            note = (EditText) findViewById(R.id.notesDisplay_mlText);
            datetime = DateFormat.getDateTimeInstance().format(new Date());
            DT=(TextView)findViewById(R.id.datetime);
            Cursor cursor = db.readNotes(TABLE_NAME_NOTES, Integer.valueOf((bundle.getString("index"))));
            DT.setText(cursor.getString(3));
            temp_note = cursor.getString(2).toString();
            note.setText(cursor.getString(2).toString());

        }catch (Exception ex){}
    }

    private void notesUpdate()throws Exception{
        if(temp_note.equals(note.getText().toString())){

        }else{
            String tmp_header;
            if(note.getText().toString().length()>10)
                tmp_header=note.getText().toString().substring(0,10);
            else
                tmp_header=note.getText().toString();

            db.updateNotes(Integer.valueOf((bundle.getString("index"))),tmp_header,note.getText().toString(),datetime);
        }
    }

    private void edit(Boolean bool){
        note.setFocusable(bool);
        note.setTextColor(Color.BLACK);
    }

    protected void overridePendingTransitionExit() {
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

}
