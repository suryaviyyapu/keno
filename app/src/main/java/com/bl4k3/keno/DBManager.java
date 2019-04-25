package com.bl4k3.keno;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String name, String email, String pass) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.SUBJECTNAME, name);
        contentValue.put(DatabaseHelper.TOTALCONDUCTED, email);
        contentValue.put(DatabaseHelper.TOTALATTENDED, pass);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.SUBJECTNAME, DatabaseHelper.TOTALCONDUCTED, DatabaseHelper.TOTALATTENDED};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String name, String email, String pass) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.SUBJECTNAME, name);
        contentValues.put(DatabaseHelper.TOTALCONDUCTED, email);
        contentValues.put(DatabaseHelper.TOTALATTENDED, pass);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

}
