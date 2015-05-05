package com.finnishverbix.SQL;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * SQLite Handling Class
 * Include : UPDATE, DELETE, SEARCH ...
 */
//Class for handling sqlite such as querying, insert, delete
public class SqliteHandler {
    public static  final String DATABASE_NAME = "FINNISH";
    public static final int DATABASE_VERSION = 1;
    Context context ;
    SqlDbHelper sqlDbHelper;
    SQLiteDatabase sqLiteDatabase;

    //CONSTRUCTOR
    public SqliteHandler(Context context){
        sqlDbHelper = new SqlDbHelper(context,DATABASE_NAME,null,DATABASE_VERSION);
        sqLiteDatabase = sqlDbHelper.getWritableDatabase();
    }

    // For executing query such as insert, delete
    public void executeQuery(String query){
        try {
            if(sqLiteDatabase.isOpen()){ // if it is open then close first
                sqLiteDatabase.close();
            }
            sqLiteDatabase = sqlDbHelper.getWritableDatabase();
            sqLiteDatabase.execSQL(query);

        }catch (Exception e){
            Log.e("Execute query", "" + e);
            e.printStackTrace();
        }
    }

    //For searching
    public Cursor selectQuery(String query){
        Cursor c1 = null;
        try {
            if(sqLiteDatabase.isOpen()){
                sqLiteDatabase.close();
            }
            sqLiteDatabase = sqlDbHelper.getWritableDatabase();
            c1 = sqLiteDatabase.rawQuery(query,null);
        }catch (Exception e){
            Log.e(" database querying", "" + e);
            e.printStackTrace();
        }
        return  c1;
    }
}
