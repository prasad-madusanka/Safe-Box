package com.prasad.safeboxApp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


/**
 * Created by Prasad on 12/29/16.
 */

public class mainClass extends AppCompatActivity {

    //Google Authentication
    private SignInButton mGoogleBtn;
    private LoginButton loginButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CallbackManager mCallbackManager;

    private static final int RC_SIGN_IN=1;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG="";


    //Classes
    DataBaseHandler db;
    ProgressDialog progressDialog;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransitionExit();
        finish();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.main_activity);

        try {

            db=new DataBaseHandler(this,null,null,1);
            mAuth = FirebaseAuth.getInstance();
            mGoogleBtn = (SignInButton) findViewById(R.id.googleBtn);
            loginButton = (LoginButton) findViewById(R.id.login_button);
            mAuth();
            googleSignIn();
            mCallbackManager = CallbackManager.Factory.create();
            loginButton.setReadPermissions("email", "public_profile");

            loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d(TAG, "facebook:onSuccess:" + loginResult);
                    handleFacebookAccessToken(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                    Log.d(TAG, "facebook:onCancel");
                    // ...
                }

                @Override
                public void onError(FacebookException error) {
                    Log.d(TAG, "facebook:onError", error);
                    // ...
                }
            });


            if (checkConnectivity()==true){
                googleButtonEnabling(true);
                facebookButtonEnabling(true);
            }else{
                googleButtonEnabling(false);
                facebookButtonEnabling(false);
                snackDialog();
            }

        } catch (Exception ex) {
            Log.e("OnCreateMainClass",ex.toString());
        }
    }

    private void googleSignIn(){

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        snackMessage("Connection Error");
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkConnectivity() == true) {
                    googleButtonEnabling(false);
                    signIn();
                    googleButtonEnabling(true);

                } else
                    snackMessage("You are not connected");
            }
        });
    }
    public void mAuth(){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    profile();
                    intentLauncher();
                    overridePendingTransitionEnter();
                }
            }
        };
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            setProgressBar();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            } else {

                if(progressDialog.isShowing())
                    progressDialog.cancel();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        try {
            Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithCredential", task.getException());
                                snackMessage("Authentication failed");

                            }
                        }
                    });
        }catch (Exception ex){
            Log.d("firebaseAuthWithGoogle",ex.toString());
        }
    }

    public void profile() {
        try {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            SharedPreferences sharedPreferences = getSharedPreferences("userProfile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (user != null) {
                editor.putString("username", user.getDisplayName());
                editor.putString("email", user.getEmail());
                editor.putString("url", String.valueOf(user.getPhotoUrl()));
                editor.putBoolean("LS",true);
                editor.apply();
            }

        } catch (Exception ex) {
            Log.e("Profile",ex.toString());
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {

            Log.d(TAG, "handleFacebookAccessToken:" + token);
            facebookButtonEnabling(false);
            AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithCredential", task.getException());
                                snackMessage("Authentication failed.");
                                facebookButtonEnabling(true);
                            }

                            // ...
                        }
                    });

    }

    public boolean checkConnectivity() {
        boolean returnValue=false;
        try {
            ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected())
                returnValue= true;
            else
                returnValue=false;

        }catch (Exception ex){
            Log.e("CheckConnectivity",ex.toString());
        }
        return returnValue;
    }

    public void setProgressBar(){
        progressDialog = ProgressDialog.show(mainClass.this, "Authenticating", "Loading...", true);
    }

    public void googleButtonEnabling(Boolean value){
        mGoogleBtn.setEnabled(value);
    }

    private void snackMessage(String msg){
        Snackbar.make(findViewById(R.id.existingAccounts), msg, Snackbar.LENGTH_SHORT).show();
    }

    private void snackDialog()throws Exception{

        final Snackbar snackbar = Snackbar.make(findViewById(R.id.existingAccounts), "You are not connected to Internet", Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("Refresh", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              try {

                  if (checkConnectivity() == true) {
                      googleButtonEnabling(true);
                      facebookButtonEnabling(true);
                  } else
                      snackDialog();
              }catch (Exception ex){
                  Log.e("mainClass_SnackDialog",ex.toString());
              }

            }//onClick method end

        });

        snackbar.show();
    }

    public void facebookButtonEnabling(Boolean value){
        loginButton.setEnabled(value);
    }

    public void intentLauncher(){

        try {
            if (db.countEntries() == true)
                startActivity(new Intent(mainClass.this, setUpPassword.class));
            else
                startActivity(new Intent(mainClass.this, requestPassword.class));

        }catch (Exception ex) {
            Log.e("intentLauncher", ex.toString());
        }
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
