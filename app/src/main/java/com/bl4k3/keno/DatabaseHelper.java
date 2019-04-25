package com.bl4k3.keno;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    static final String TABLE_NAME = "INFOSUBJECT";

    // Table columns
    static final String _ID = "_id";
    static final String SUBJECTNAME = "name";
    static final String TOTALCONDUCTED = "totalConducted";
    static final String TOTALATTENDED = "totalAttended";

    // Database Information
    private static final String DB_NAME = "Subject_info.DB";

    // database version
    private static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SUBJECTNAME + " TEXT NOT NULL, " + TOTALCONDUCTED + " TEXT NOT NULL, " + TOTALATTENDED + " TEXT);";

    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
