package com.example.myapplication;

import static com.example.myapplication.Constants.DATABASE_NAME;
import static com.example.myapplication.Constants.DATABASE_VERSION;
import static com.example.myapplication.Constants.KEY_AGE;
import static com.example.myapplication.Constants.KEY_HEIGHT;
import static com.example.myapplication.Constants.KEY_ID;
import static com.example.myapplication.Constants.KEY_NAME;
import static com.example.myapplication.Constants.KEY_WEIGHT;
import static com.example.myapplication.Constants.TABLE_STUDENTS;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                TABLE_STUDENTS, KEY_ID, KEY_NAME, KEY_AGE, KEY_WEIGHT, KEY_HEIGHT));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*db.execSQL("DROP TABLE IF exists " + TABLE_STUDENTS);
        onCreate(db);*/
    }
}