package com.prasad.safeboxApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.kosalgeek.android.md5simply.MD5;

/**
 * Created by Prasad on 12/29/16.
 */

public class requestPassword extends AppCompatActivity{

    private session session;
    private DataBaseHandler db;
    private Snackbar snackbar;

    private EditText getPassword;
    private FloatingActionButton gotoMenu;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransitionExit();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_password);

        try {

            db = new DataBaseHandler(this, null, null, 1);
            session = new session(this);
            session.setLogin(true);

            getPassword = (EditText) findViewById(R.id.txtreqPasssword);
            getPassword.requestFocus();
            gotoMenu = (FloatingActionButton) findViewById(R.id.wb_btn_gotoMenu);

            gotoMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        if (db.readPassword(MD5.encrypt(getPassword.getText().toString())) == true)
                            intentLauncher(menu.class);
                        else
                            snackDialog();
                    } catch (Exception e) {
                        Log.e("requestPasswordGotoMenu", e.toString());
                    }
                }
            });

            gotoMenu.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    intentLauncher(setUpPassword.class);
                    return false;
                }
            });


        }catch (Exception ex){
            Log.e("requestPasswordOnCreate",ex.toString());
        }
    }

    private void snackDialog(){
        try {
            final Snackbar snackbar = Snackbar.make(findViewById(R.id.reqPassword), "Password incorrect", Snackbar.LENGTH_LONG);

            snackbar.setAction("RESET", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intentLauncher(setUpPassword.class);
                }//onClick method end

            });

            snackbar.show();
        }catch (Exception ex){
            Log.e("snackDialog_reqPassword",ex.toString());
        }
    }

    private void intentLauncher(Class cls){
        startActivity(new Intent(requestPassword.this,cls));
        overridePendingTransitionEnter();
    }

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

    protected void overridePendingTransitionExit() {
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
