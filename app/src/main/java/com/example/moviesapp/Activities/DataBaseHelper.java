package com.example.moviesapp.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "USERDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "USER";
    private static final String NAME = "NAME";
    private static final String USERNAME = "USERNAME";
    private static final String EMAIL = "EMAIL";
    private static final String PASSWORD = "PASSWORD";
    private static final String CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + "(" +
            NAME + " TEXT NOT NULL, " + USERNAME + " TEXT PRIMARY KEY NOT NULL, " + EMAIL + " TEXT NOT NULL, " + PASSWORD + " TEXT NOT NULL)";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertUser(String name, String userName, String email, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(USERNAME, userName);
        values.put(EMAIL, email);
        values.put(PASSWORD, password);
        db.insert(TABLE_NAME, null, values);
    }

    public void deleteUser(String username)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, USERNAME + " = " + username, null);
    }

    public String getPassword(String username)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + USERNAME + " = ?", new String[]{username});
        if(cursor.moveToFirst())
        {
            String password = cursor.getString(3);
            cursor.close();
            return password;
        }
        return "";
    }

    public boolean existUser(String username)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + PASSWORD + " FROM " + TABLE_NAME + " WHERE " + USERNAME + " = ?", new String[]{username});
        if (cursor != null && cursor.getCount() > 0)
        {
            cursor.close();
            return true;
        }
        return false;
    }
}