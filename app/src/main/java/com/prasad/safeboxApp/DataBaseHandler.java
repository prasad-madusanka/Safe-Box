package com.prasad.safeboxApp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Prasad on 12/27/16.
 */

public class DataBaseHandler extends SQLiteOpenHelper{

    private static final String DATABASE_NAME="safeBox.db";
    private static final String TABLE_NAME_ACCESS="login";
    private static final String TABLE_NAME_VAULT="vault";
    private static final String TABLE_NAME_TRASH_VAULT ="vault_trash";
    private static final String TABLE_NAME_NOTES="notes";
    private static final String TABLE_NAME_TRASH_NOTES="notes_trash";


    protected DataBaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String drop="DROP IF TABLE EXISTS ";
        sqLiteDatabase.execSQL(drop+TABLE_NAME_ACCESS);
        sqLiteDatabase.execSQL(drop+TABLE_NAME_VAULT);
        sqLiteDatabase.execSQL(drop+ TABLE_NAME_TRASH_VAULT);
        sqLiteDatabase.execSQL(drop+TABLE_NAME_NOTES);
        sqLiteDatabase.execSQL(drop+TABLE_NAME_TRASH_NOTES);
        onCreate(sqLiteDatabase);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String accessCheck="CREATE TABLE "+ TABLE_NAME_ACCESS + "(ID INTEGER PRIMARY KEY AUTOINCREMENT," + "PWD TEXT)";
        sqLiteDatabase.execSQL(accessCheck);

        String query_vault="CREATE TABLE "+ TABLE_NAME_VAULT + "(ID INTEGER PRIMARY KEY AUTOINCREMENT," + "UNAME TEXT," + "PASSCODE TEXT," + "DESCRIPTION TEXT)";
        sqLiteDatabase.execSQL(query_vault);

        String query_vault_trash="CREATE TABLE "+ TABLE_NAME_TRASH_VAULT + "(ID INTEGER PRIMARY KEY AUTOINCREMENT," + "UNAME TEXT," + "PASSCODE TEXT," + "DESCRIPTION TEXT)";
        sqLiteDatabase.execSQL(query_vault_trash);

        String query_notes="CREATE TABLE "+ TABLE_NAME_NOTES + "(ID INTEGER PRIMARY KEY AUTOINCREMENT," + "HEADER TEXT," + "BODY TEXT,"+"DATE TEXT)";
        sqLiteDatabase.execSQL(query_notes);

        String query_trash_notes="CREATE TABLE "+ TABLE_NAME_TRASH_NOTES + "(ID INTEGER PRIMARY KEY AUTOINCREMENT," + "HEADER TEXT," + "BODY TEXT,"+"DATE TEXT)";
        sqLiteDatabase.execSQL(query_trash_notes);

    }

    protected boolean addPWD(String pwd)throws Exception{
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put("PWD",pwd);

        long result=db.insert(TABLE_NAME_ACCESS,null,contentValues);

        if(result==-1){
            return false;
        }else{
            return true;
        }
    }

    protected boolean countEntries()throws Exception{
        String query_user_check = "SELECT * FROM " + TABLE_NAME_ACCESS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery(query_user_check,null);

        if (cursor.getCount()==0){
            return true;
        }else {
            return false;
        }
    }

    protected boolean addVault(String tableName,String add_uname,String add_pwd,String add_desc)throws Exception {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("UNAME", add_uname);
        contentValues.put("PASSCODE", add_pwd);
        contentValues.put("DESCRIPTION", add_desc);

        long result = db.insert(tableName, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            db.close();
            return true;
        }
    }

    protected boolean readPassword(String pwd) throws Exception{
        String query_user_check = "SELECT * FROM " + TABLE_NAME_ACCESS + " WHERE PWD=" + "'" + pwd + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query_user_check, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0)
            return true;
        else {
            db.close();
            return false;
        }
    }

    protected boolean updateUserPassword(String pwd)throws Exception{
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("PWD",pwd);

        String condition="ID= "+1;

        long result=db.update(TABLE_NAME_ACCESS,contentValues,condition,null);
        if(result==-1){
            return false;
        }else
            return true;
    }

    protected Cursor readVault(String tableName)throws Exception{
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor data=db.rawQuery("SELECT * FROM " + tableName,null);
        return data;
    }

    protected Cursor returnFromVault(String tableName,int id)throws Exception{
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor data=db.rawQuery("SELECT * FROM " + tableName+" WHERE ID=" + "'" + id + "'",null);
        data.moveToFirst();
        if (data.getCount()!=0)
            return data;
        else
            return null;

    }

    protected void deleteFromVault(String tableName,int id)throws Exception{
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DELETE FROM " + tableName+ " WHERE ID='"+id+"'");

    }

    protected boolean insertNotes(String tablename,String header,String body,String date)throws Exception{
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("HEADER",header);
        contentValues.put("BODY",body);
        contentValues.put("DATE",date);

        long result=db.insert(tablename,null,contentValues);

        if (result==-1){
            return false;
        }else {
            return true;
        }

    }

    protected Cursor readNotes(String tablename)throws Exception{
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor data=db.rawQuery("SELECT * FROM " + tablename,null);
        return data;
    }

    protected Cursor readNotes(String tablename,int id)throws Exception{
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor data=db.rawQuery("SELECT * FROM " + tablename + " WHERE ID=" + "'" + id + "'",null);
        data.moveToFirst();
        if(data.getCount()!=0) {
            return data;
        }else
            return null;
    }

    protected void deleteFromNotes(String tablename,int id)throws Exception{
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DELETE FROM " + tablename + " WHERE ID='" + id + "'");

    }

    protected boolean updateNotes(int id, String header, String body,String date)throws Exception{
        SQLiteDatabase db=getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put("HEADER",header);
        contentValues.put("BODY",body);
        contentValues.put("DATE",date);

        String condition="ID= "+id;

        long result=db.update(TABLE_NAME_NOTES,contentValues,condition,null);
        if(result==-1){
            return false;
        }else
            return true;
    }

}
