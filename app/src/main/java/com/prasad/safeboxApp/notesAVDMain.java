package com.prasad.safeboxApp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Prasad on 3/9/17.
 */

public class notesAVDMain extends AppCompatActivity {


//Classes
    private DataBaseHandler db;
    private ListAdapter listAdapter;
    private Cursor data;

//UI components
    private ListView listView;
    private Snackbar snackbar;
    private FloatingActionButton add;

//Variables
    private String id;
    private List<notesGetterSetter> arrayList;
    private static final String TABLE_NAME_NOTES="notes";
    private static final String TABLE_NAME_TRASH_NOTES="notes_trash";


    //Override methods

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(notesAVDMain.this,notes.class));
        overridePendingTransitionExit();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_avd);

        try {

            add = (FloatingActionButton) findViewById(R.id.btn_add);
            db = new DataBaseHandler(this, null, null, 1);
            data = db.readNotes(TABLE_NAME_NOTES);
            arrayList = new ArrayList<>();
            listView = (ListView) findViewById(R.id.list_notes);

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(notesAVDMain.this, noteAdd.class));
                    overridePendingTransitionEnter();
                    finish();
                }
            });


            if (data.getCount() == 0) {
             snackbar.make(findViewById(R.id.notes_list), "Notes empty, Click \"PLUS\" to add", snackbar.LENGTH_LONG).show();

            } else {
                while (data.moveToNext()) {
                    try {

                        arrayList.add(new notesGetterSetter(data.getInt(0),data.getString(1),data.getString(3)));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    listAdapter = new customAdapterNotes(this, arrayList);
                    listView.setAdapter(listAdapter);
                }
                db.close();
            }


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                notesAVDMain.this.id = String.valueOf(parent.getItemIdAtPosition(position));
                            startActivity(new Intent(notesAVDMain.this, notesDisplay.class).putExtra("index",String.valueOf(view.getId())));
                            overridePendingTransitionEnter();
                            finish();
                    }
            });


            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                notesAVDMain.this.id = String.valueOf(parent.getItemIdAtPosition(position));

                        try {
                            Cursor cursor = db.readNotes(TABLE_NAME_NOTES,view.getId());

                            String at1 = cursor.getString(1);
                            String at2 = cursor.getString(2);
                            String at3 = cursor.getString(3);

                            db.insertNotes(TABLE_NAME_TRASH_NOTES, at1, at2, at3);

                            db.deleteFromNotes(TABLE_NAME_NOTES,view.getId());// listShow[0][i]
                            arrayList.remove(position);
                            ((BaseAdapter) listAdapter).notifyDataSetChanged();
                            snackDialog();

                        }catch (Exception ex){
                            Log.e("Notes_temporary_delete",ex.toString());
                        }
                return false;

                }

             });

            }catch (Exception ex){
                Log.e("notesAVDMain",ex.toString());
        }

    }

    private void snackDialog()throws Exception{

        final Snackbar snackbar = Snackbar.make(findViewById(R.id.notes_list), "Delete success !!!", Snackbar.LENGTH_LONG);

        snackbar.setAction("Show me", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(notesAVDMain.this,notesTrash.class));
                overridePendingTransitionEnter();

            }//onClick method end

        });

        snackbar.show();
    }

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }
}
