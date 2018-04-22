package com.prasad.safeboxApp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Prasad on 12/19/16.
 */

public class menu extends Activity{

    private session session;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private ImageButton vault,vaultAdd,vaultView,vaultDelete,notes,notesAdd,notesDelete,profile,logout,reset;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransitionExit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        try {

            session = new session(this);
            mAuth = FirebaseAuth.getInstance();
            vault = (ImageButton) findViewById(R.id.vault);
            profile = (ImageButton) findViewById(R.id.profile);
            notes = (ImageButton) findViewById(R.id.notes);
            reset=(ImageButton)findViewById(R.id.reset) ;
            logout = (ImageButton) findViewById(R.id.logout);

            vaultAdd=(ImageButton)findViewById(R.id.addVault);
            vaultView=(ImageButton)findViewById(R.id.viewVault);
            vaultDelete=(ImageButton)findViewById(R.id.deleteVault);

            notesAdd=(ImageButton)findViewById(R.id.addNote);
            notesDelete=(ImageButton)findViewById(R.id.deleteNote);

            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (firebaseAuth.getCurrentUser() == null) {
                        try {
                            session.setLogin(false);
                        }catch (Exception ex){
                            Log.e("menumAuthListener",ex.toString());
                        }
                        onStop();
                        intentLauncher(mainClass.class);
                    }
                }
            };

            if (!session.logged()) {
                logout();
            }

            vault.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intentLauncher(vaultViewPager.class);
                }
            });

            vaultAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intentLauncher(vaultAddNew.class);
                }
            });

            vaultView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intentLauncher(vaultContentDisplay.class);
                }
            });

            vaultDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intentLauncher(vaultDeleteEntries.class);
                }
            });

            profile.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    intentLauncher(com.prasad.safeboxApp.profile.class);
                }
            });

            notes.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    intentLauncher(com.prasad.safeboxApp.notes.class);
                }
            });

            notesAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intentLauncher(noteAdd.class);
                }
            });

            notesDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intentLauncher(notesTrash.class);
                }
            });

            reset.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    startActivity(new Intent(menu.this,setUpPassword.class));
                    overridePendingTransitionEnter();
                }
            });


            logout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    onStop();
                    logout();
                    intentLauncher(mainClass.class);

                }
            });

            logout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    logout();
                    clearApplicationData();
                    return false;
                }
            });


        }catch (Exception ex){
            Log.e("mainOnCreate",ex.toString());
        }

    }


    private void clearSharedPref()throws Exception{
            SharedPreferences sharedPreferences=getSharedPreferences("userProfile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.clear();
            editor.commit();
            finish();
    }

    private void logout(){
        try {

            session.setLogin(false);
            this.deleteDatabase("safeBox.db");
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            clearSharedPref();

        }catch (Exception ex){
            Log.e("Logout",ex.toString());
        }
    }


   private void clearApplicationData(){
        try{
            String packageName=getApplicationContext().getPackageName();
            Runtime runtime=Runtime.getRuntime();
            runtime.exec("pm clear "+packageName);
        }catch (Exception ex){
            Log.e("ClearAppData_Menu",ex.toString());
        }
    }


    private void intentLauncher(Class cls){
        startActivity(new Intent(menu.this,cls));
        overridePendingTransitionEnter();
        finish();
    }

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    protected void overridePendingTransitionExit() {
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }


}










