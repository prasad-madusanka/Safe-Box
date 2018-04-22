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

/**
 * Created by Prasad on 1/2/17.
 */

public class vaultAddNew extends AppCompatActivity {

//Classes
    private Crypto crypto1,crypto2;
    private DataBaseHandler db;

//UI components
    private EditText uname1,uname2,pwd1,pwd2,desc;
    private FloatingActionButton save;

//Variables
    private String var_uname1,var_uname2,var_pwd1,var_pwd2,var_desc;
    private static final String TABLE_NAME_VAULT="vault";


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransitionExit();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vault_add_passwords);

        try {

            db = new DataBaseHandler(this, null, null, 1);
            crypto1 = new Crypto("9#lP3tl7!3ab4^5*3stdio74");
            crypto2 = new Crypto("#$%1Rt56*io9!ab4st&rac9t");


            uname1 = (EditText) findViewById(R.id.vault_uname1);
            uname2 = (EditText) findViewById(R.id.vault_uname2);
            pwd1 = (EditText) findViewById(R.id.vault_pwd1);
            pwd2 = (EditText) findViewById(R.id.vault_pwd2);
            desc = (EditText) findViewById(R.id.vault_description);

            save = (FloatingActionButton) findViewById(R.id.vault_done);


            //Override onClick method with FloatingActionButton 'save'

            save.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    //Getting inputs from UI components to variables

                    try {

                        var_uname1 = uname1.getText().toString();
                        var_uname2 = uname2.getText().toString();
                        var_desc = desc.getText().toString();

                        //Validate inputs
                        if (var_uname1.length() != 0 && var_uname2.length() != 0 && pwd1.length() != 0 && pwd2.length() != 0 && var_desc.length() != 0) {
                            if (var_uname1.length()<=30 && var_uname2.length()<=30 && var_desc.length()<=30 && pwd1.getText().toString().length()<=30 && pwd2.getText().toString().length()<=30) {
                                if (var_uname1.equals(var_uname2)) {

                                    var_pwd1 = crypto2.Encrypt(crypto1.Encrypt(pwd1.getText().toString()));
                                    var_pwd2 = crypto2.Encrypt(crypto1.Encrypt(pwd2.getText().toString()));

                                    if (var_pwd1.equals(var_pwd2)) {
                                        try {
                                            sendToDatabase(var_uname1, var_pwd1, var_desc);                     //Invoke sendToDatabase method by sending params.
                                        } catch (Exception e) {
                                            Log.e("InsertingPassword", e.toString());
                                        }
                                    } else {
                                        snack("Password is not match");
                                    }
                                } else {
                                    snack("Username is not match");
                                }
                            }else {
                                snack("Maximum limit is 30 characters");
                            }
                        }else {
                            snack("You cannot leave anything empty ");
                        }

                    }catch (Exception ex){}
                }
            });



        } catch (Exception ex) {
           Log.e("vaultAddNew",ex.toString());
        }


    }

    //Save current user inputs to local database
    private void sendToDatabase(String send_uname,String send_pwd,String send_desc){
        try {
            boolean result = db.addVault(TABLE_NAME_VAULT,send_uname, send_pwd, send_desc);
            //feedRTD();          //Feed cloud database

            if (result == true) {

                snackDialog("Saved success !!! ", "Show me");
                clearFields();

            } else if (result == false)
                snack("Something wrong,Looks like memory full");


        } catch (Exception ex) {
            Log.e("Send.DatabaseFailure",ex.toString());
        }
    }

    private void snackDialog(String msg, String buttonText)throws Exception{

        final Snackbar snackbar = Snackbar.make(findViewById(R.id.vault_add_password), msg, Snackbar.LENGTH_LONG);

        snackbar.setAction(buttonText, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(vaultAddNew.this,vaultContentDisplay.class));
                overridePendingTransitionEnter();

            }//onClick method end

        });

        snackbar.show();
    }

    private void snack(String message)throws Exception{
        Snackbar.make(findViewById(R.id.vault_add_password),message,Snackbar.LENGTH_LONG).show();
    }

    private void clearFields()throws Exception{
        uname1.setText("");
        uname2.setText("");
        pwd1.setText("");
        pwd2.setText("");
        desc.setText("");

    }


    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    protected void overridePendingTransitionExit() {
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

}
