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
 * Created by Prasad on 1/2/17.
 */

public class vaultContentDisplay extends AppCompatActivity {

//Classes
    private Crypto crypto1,crypto2;
    private DataBaseHandler db;

//UI components
    private ListView listView;
    private Snackbar snackbar;

//Variables
    String id;
    private static final String TABLE_NAME_VAULT_TRASH="vault_trash";
    private static final String TABLE_NAME_VAULT="vault";
    private List<vaultGetterSetter> arrayList;
    private ListAdapter listAdapter;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransitionExit();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vault_passwords_list);

        try {

            db = new DataBaseHandler(this, null, null, 1);
            crypto1 = new Crypto("9#lP3tl7!3ab4^5*3stdio74");
            crypto2 = new Crypto("#$%1Rt56*io9!ab4st&rac9t");
            Cursor data = db.readVault(TABLE_NAME_VAULT);
            listView = (ListView) findViewById(R.id.vault_list_view);
            arrayList = new ArrayList<>();

            if (data.getCount() == 0) {
                snackbar.make(findViewById(R.id.vault_list_view), "Your vault is empty", snackbar.LENGTH_LONG).show();

            } else {
                while (data.moveToNext()) {
                    try {
                        arrayList.add(new vaultGetterSetter(data.getInt(0), data.getString(1), crypto1.Decrypt(crypto2.Decrypt(data.getString(2))), data.getString(3)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    listAdapter = new customAdapterVault(this, arrayList);
                    listView.setAdapter(listAdapter);

                }
                data.close();

            }
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    try {

                        Cursor cur = db.returnFromVault(TABLE_NAME_VAULT, view.getId());
                        db.addVault(TABLE_NAME_VAULT_TRASH, cur.getString(1), cur.getString(2), cur.getString(3));
                        db.deleteFromVault(TABLE_NAME_VAULT, view.getId());

                        arrayList.remove(position);
                        ((BaseAdapter) listAdapter).notifyDataSetChanged();
                        snackDialog();

                    } catch (Exception ex) {
                        Log.e("vaultSetOnItemLongClick", ex.toString());
                    }
                    return false;

                }//OnItemLongClick end

            });


        }catch (Exception ex){
            Log.e("vaultContentOnCreate",ex.toString());
        }

    }

    private void snackDialog()throws Exception{

        final Snackbar snackbar = Snackbar.make(findViewById(R.id.vaultContent), "Delete success !!!", Snackbar.LENGTH_LONG);

        snackbar.setAction("Show me", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(vaultContentDisplay.this,vaultDeleteEntries.class));
                overridePendingTransitionEnter();

            }//onClick method end

        });

        snackbar.show();
    }

    private void setSnackBar(String msg)throws Exception{
        snackbar.make(findViewById(R.id.vaultContent),msg,Snackbar.LENGTH_SHORT).show();
    }
    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    protected void overridePendingTransitionExit() {
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

}
