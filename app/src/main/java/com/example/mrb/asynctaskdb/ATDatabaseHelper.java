package com.example.mrb.asynctaskdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mrb on 16-05-18.
 */
public class ATDatabaseHelper extends SQLiteOpenHelper
{
    private static final String DB_NAME = "asynctaskDB";
    private static final int DB_VERSION = 1;


    public ATDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                            int version)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        updateMyDatabase(db, 0, DB_VERSION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        updateMyDatabase(db, oldVersion, newVersion);

    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("CREATE TABLE LIST (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ITEM TEXT);");
    }

    public void insertElement(SQLiteDatabase db, ContentValues newContent)
    {
        db.insert("LIST", null, newContent);

    }
}
