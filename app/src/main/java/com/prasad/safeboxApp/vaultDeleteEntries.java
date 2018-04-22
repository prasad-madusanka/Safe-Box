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
 * Created by Prasad on 1/8/17.
 */

public class vaultDeleteEntries extends AppCompatActivity {


//Classes
    private DataBaseHandler db;
    private ListAdapter listAdapter;

//UI components
    private Snackbar snackbar;
    private ListView listView;

//Variables
    private List<vaultDeleteGetterSetter> arrayList;
    private static final String TABLE_NAME_VAULT="vault";
    private static final String TABLE_NAME_VAULT_TRASH="vault_trash";


    //Override methods

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransitionExit();
        finish();
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vault_rd_passwords);

        try {

            db = new DataBaseHandler(this, null, null, 1);
            listView = (ListView) findViewById(R.id.vault_listview_delete);
            arrayList = new ArrayList<>();
            Cursor data = db.readVault(TABLE_NAME_VAULT_TRASH);

            if (data.getCount() == 0) {
                snackbar.make(findViewById(R.id.vault_delete_entries),"Looks like your trash is empty", snackbar.LENGTH_LONG).show();
            } else {
                while (data.moveToNext()) {
                    arrayList.add(new vaultDeleteGetterSetter(data.getInt(0), data.getString(1), data.getString(3)));
                    listAdapter = new customAdapterVaultDelete(this, arrayList);
                    listView.setAdapter(listAdapter);

                }//While loop end

                data.close();

            }//end of else part

            //setOnItemClickListener will restore an item
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        Cursor cursor = db.returnFromVault(TABLE_NAME_VAULT_TRASH, view.getId());
                        db.addVault(TABLE_NAME_VAULT, cursor.getString(1), cursor.getString(2), cursor.getString(3));
                        db.deleteFromVault(TABLE_NAME_VAULT_TRASH, view.getId());

                        arrayList.remove(position);
                        ((BaseAdapter) listAdapter).notifyDataSetChanged();
                        snackDialog();

                    }catch (Exception ex){
                        Log.e("vaultDeleteEntOnClick",ex.toString());
                    }
                }//end of if

            });//end click listener


            //setOnItemLongClickListener will delete an item item permanently
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    try {
                        db.deleteFromVault(TABLE_NAME_VAULT_TRASH, view.getId());
                        arrayList.remove(position);
                        ((BaseAdapter) listAdapter).notifyDataSetChanged();
                        setSnackBar("Permanently deleted !!!");
                    }catch (Exception ex){
                        Log.e("vaultDeleteEntriesLong",ex.toString());
                    }
                    return false;
                }//onItemLingClick end....

            });//end click listener

        }catch (Exception ex){
            Log.e("vaultDeleteEntries",ex.toString());
        }

    }

    private void setSnackBar(String msg)throws Exception{
        snackbar.make(findViewById(R.id.vault_delete_entries),msg,Snackbar.LENGTH_SHORT).show();
    }

    private void snackDialog()throws Exception{

        final Snackbar snackbar = Snackbar.make(findViewById(R.id.vault_delete_entries), "Restored success", Snackbar.LENGTH_LONG);

        snackbar.setAction("Show me", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(vaultDeleteEntries.this,vaultContentDisplay.class));
                overridePendingTransitionExit();

            }//onClick method end

        });

        snackbar.show();
    }

    //Intent Animations

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
