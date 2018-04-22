package com.prasad.safeboxApp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Prasad on 12/28/16.
 */

public class session {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    protected session(Context context)throws Exception{
        this.context=context;
        sharedPreferences=context.getSharedPreferences("safeBox",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }
    protected void setLogin(boolean login_status)throws Exception{
        editor.putBoolean("logged",login_status);
        editor.commit();
    }
    protected boolean logged()throws Exception{
        return sharedPreferences.getBoolean("logged",false);
    }



}
