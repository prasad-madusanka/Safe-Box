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
import android.widget.TextView;
import com.kosalgeek.android.md5simply.MD5;

/**
 * Created by Prasad on 4/6/17.
 */

public class setUpPassword extends AppCompatActivity {

    private DataBaseHandler db;
    private Snackbar snackbar;

    private EditText pass1,pass2,pass3;
    private FloatingActionButton go;
    private TextView status;
    private boolean flag;
    private String pwdInput1,pwdInput2,pwdInput3;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransitionExit();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_password);

        try {

            db = new DataBaseHandler(this, null, null, 1);
            flag = db.countEntries();

            if (flag == true) {
                initialize("Set up a password", true);
            } else {
                initialize("Update password", false);
            }

            go.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag == true) {
                        pwdInput1 = MD5.encrypt(pass1.getText().toString());
                        pwdInput2 = MD5.encrypt(pass2.getText().toString());
                        if (pwdInput1.length() != 0 && pwdInput2.length() != 0) {
                            if (pass1.getText().toString().length() >=4 && pass2.getText().toString().length() >=4){
                                if (pwdInput1.equals(pwdInput2)) {
                                    try {
                                        db.addPWD(pwdInput1);
                                    } catch (Exception ex) {
                                        Log.e("setPassword", ex.toString());
                                    }
                                    startIntent();

                                } else {
                                    snack("Password mismatch :)");
                                }
                        }else
                            snack("Password must contain at least 4 characters");
                        } else {
                            snack("Password cannot be empty ...");
                        }
                    } else {
                        pwdInput1 = MD5.encrypt(pass1.getText().toString());
                        pwdInput2 = MD5.encrypt(pass2.getText().toString());
                        pwdInput3 = MD5.encrypt(pass3.getText().toString());

                        try {
                            if (pwdInput1.length() != 0 && pwdInput2.length() != 0 && pwdInput3.length() != 0) {
                                if (pass2.getText().toString().length()>=4 && pass3.getText().toString().length()>= 4) {
                                    if (pwdInput2.equals(pwdInput3)) {
                                        if (db.readPassword(pwdInput1) == true) {
                                            if (db.updateUserPassword(pwdInput3) == true) {
                                                startIntent();
                                            } else {
                                                snack("Update failed");
                                            }
                                        } else {
                                            snack("No matching password found...");
                                        }
                                    } else
                                        snack("Password mismatch :)");
                                } else
                                    snack("Password must contain at least 4 characters");
                            }else
                                snack("Password cannot be empty ...");

                        }catch (Exception ex){
                            Log.e("setPassword_update",ex.toString());
                        }
                    }
                }
            });

        }catch (Exception ex){
            Log.e("setUpPassword",ex.toString());
        }
    }

    private void getId(){

    }

    private void initialize(String stateMsg,boolean stateFlag) throws Exception{

        pass1 = (EditText) findViewById(R.id.password1);
        pass2 = (EditText) findViewById(R.id.password2);
        pass3 = (EditText) findViewById(R.id.password3);
        if (stateFlag == true) {
            pass3.setVisibility(View.INVISIBLE);
            setHint("Password",pass1);
            setHint("Re-enter",pass2);
        }else {
            setHint("Old password", pass1);
            setHint("New password", pass2);
            setHint("Re-enter", pass3);
        }
        go = (FloatingActionButton) findViewById(R.id.btnGo);

        status = (TextView) findViewById(R.id.txtViewPasswordStateMessage);
        status.setText(stateMsg);

    }
    private void startIntent(){
        startActivity(new Intent(setUpPassword.this, registerDone.class));
        overridePendingTransitionEnter();
        finish();
    }

    private void setHint(String hint,EditText editText)throws Exception{
        editText.setHint(hint);
    }

    private void snack(String msg){
        snackbar.make(findViewById(R.id.setPassword),msg,snackbar.LENGTH_LONG).show();
    }

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    protected void overridePendingTransitionExit() {
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
