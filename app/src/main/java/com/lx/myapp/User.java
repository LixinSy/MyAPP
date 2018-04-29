package com.lx.myapp;

import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by 李莘 on 2018/4/5.
 */

public class User {
    private String name;
    private String password;
    private String remembered;

    public User(String name, String password, String remembered) {
        this.name = name;
        this.password = password;
        this.remembered = remembered;
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public static ArrayList<User> getUsers(DBOpenHelper dbOpenHelper){
        ArrayList<User> arr = new ArrayList<User>();
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query("Tuser", new String[] { "name","password", "remembered"}, "remembered=?", new String[] { "1" }, null, null, null);
        try{
            while(cursor.moveToNext()) {
                String uName = cursor.getString(cursor.getColumnIndex("name"));
                String uPwd = cursor.getString(cursor.getColumnIndex("password"));
                String uRem = cursor.getString(cursor.getColumnIndex("remembered"));
                arr.add(new User(uName, uPwd, uRem));
            }
        }catch (Exception e){

        }
        return arr;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getRemembered() {
        return remembered;
    }


}
