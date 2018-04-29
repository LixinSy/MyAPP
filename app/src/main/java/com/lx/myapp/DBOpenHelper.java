package com.lx.myapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 李莘 on 2018/4/2.
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "myapp.db", null, 1);
    }
    @Override
    //数据库第一次创建时被调用
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("DROP TABLE Tuser");
        db.execSQL("CREATE TABLE Tuser" +
                "(userid INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name VARCHAR(20)," +
                "password VARCHAR(20)," +
                "phone VARCHAR(20)," +
                "email VARCHAR(20)," +
                "remembered CHAR(2))");

    }
    //软件版本号发生改变时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("ALTER TABLE Tuser ADD remembered CHAR(2)");
    }
}
