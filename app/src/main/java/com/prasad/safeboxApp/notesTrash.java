package com.prasad.safeboxApp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class notesTrash extends AppCompatActivity {

    private DataBaseHandler db;

    private List<notesGetterSetter> arrayList;
    private Snackbar snackbar;
    private ListAdapter listAdapter;

    private static final String TABLE_NAME_NOTES="notes";
    private static final String TABLE_NAME_TRASH_NOTES="notes_trash";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //startActivity(new Intent(notesTrash.this,notesAVDMain.class));
        overridePendingTransitionExit();
        //finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_delete_items_listview);

        try {

            db = new DataBaseHandler(this, null, null, 1);
            ListView notesTrash = (ListView) findViewById(R.id.notesTrashList);
            arrayList = new ArrayList<>();
            Cursor data = db.readNotes(TABLE_NAME_TRASH_NOTES);

            if (data.getCount() == 0) {
                snackbar.make(findViewById(R.id.notesTrash), "Trash Empty", snackbar.LENGTH_LONG).show();
            } else {
                while (data.moveToNext()) {
                    try {
                        arrayList.add(new notesGetterSetter(data.getInt(0), data.getString(1),data.getString(3)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    listAdapter = new customAdapterNotes(this, arrayList);
                    notesTrash.setAdapter(listAdapter);
                }
                db.close();
            }

            notesTrash.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    try {
                        Cursor cursor = db.readNotes(TABLE_NAME_TRASH_NOTES, view.getId());
                        db.insertNotes(TABLE_NAME_NOTES, cursor.getString(1), cursor.getString(2), cursor.getString(3));
                        db.deleteFromNotes(TABLE_NAME_TRASH_NOTES,view.getId());
                        arrayList.remove(position);
                        ((BaseAdapter)listAdapter).notifyDataSetChanged();
                        snackDialog();
                    }catch (Exception ex){

                    }
                }
            });

            notesTrash.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        db.deleteFromNotes(TABLE_NAME_TRASH_NOTES, view.getId());
                    } catch (Exception e) {
                        Log.e("noteAddLongClick",e.toString());
                    }
                    arrayList.remove(position);
                            ((BaseAdapter)listAdapter).notifyDataSetChanged();
                            snackbar.make(findViewById(R.id.notesTrash), "Permanently deleted !!!", snackbar.LENGTH_SHORT).show();

                    return false;
                }
            });

        } catch (Exception ex) {
            Log.e("notesTrash",ex.toString());
        }
    }

    private void snackDialog()throws Exception{

        final Snackbar snackbar = Snackbar.make(findViewById(R.id.notesTrash), "Note restored ", Snackbar.LENGTH_LONG);

        snackbar.setAction("Show me", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(notesTrash.this,notesAVDMain.class));
                overridePendingTransitionExit();
            }//onClick method end

        });

        snackbar.show();
    }

    protected void overridePendingTransitionExit() {
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

}
