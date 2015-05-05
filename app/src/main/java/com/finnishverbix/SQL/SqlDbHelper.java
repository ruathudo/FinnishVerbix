package com.finnishverbix.SQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * A Class of creating database for the application
 */
//A Class for creating SQLITE DATABASE
public class SqlDbHelper extends SQLiteOpenHelper {

    //DECLARE VARIABLES , Make a table name, columns.
    public static final String DATABASE_TABLE =  "FINNISH_WORDS";
    public static final String COLUMN_0 = "id";
    public static final String COLUMN_1 = "Verb";
    public static final String COLUMN_2 = "Meaning";
    public static final String COLUMN_3 = "Type";
    public static final String COLUMN_4 = "Present";
    public static final String COLUMN_5 = "Perfect";
    public static final String COLUMN_6 = "Imperfect";
    public static final String COLUMN_7 = "Pluperfect";
    public static final String COLUMN_8 = "Potential";
    public static final String COLUMN_9 = "PotentialPerfect";
    public static final String COLUMN_10 = "Conditional";
    public static final String COLUMN_11 = "Infinitive2";
    public static final String COLUMN_12 = "Infinitive3";


    //CREATE SCRIPTS
    //SET UP THE DATABASE
    public static final String SCRIPT_CREATE_DATABASE = "create table "+ DATABASE_TABLE + " ("
            + COLUMN_0 +" integer primary key  autoincrement, "
            + COLUMN_1 +" text not null, "
            + COLUMN_2 +" text, "
            + COLUMN_3 +" text, "
            + COLUMN_4 +" text, "
            + COLUMN_5 +" text, "
            + COLUMN_6 +" text, "
            + COLUMN_7 +" text, "
            + COLUMN_8 +" text, "
            + COLUMN_9 +" text, "
            + COLUMN_10 +" text, "
            + COLUMN_11 +" text, "
            + COLUMN_12 +" text);";

    //CONSTRUCTOR
    public SqlDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version ){
        super(context,name,factory,version);

    }

    //CREATE A DABASE HERE
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SCRIPT_CREATE_DATABASE);
    }

    //UPGRADE A DATABASE
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(sqLiteDatabase);
    }
}
