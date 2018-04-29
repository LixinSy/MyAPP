package com.lx.myapp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

public class RegisterActivity extends AppCompatActivity {

    private Button btnRegister;
    private EditText userName;
    private EditText userPwd;
    private EditText userPwd1;
    private EditText userPhone;
    private EditText userEmail;

    private SQLiteDatabase db;
    private DBOpenHelper myDBHelper;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = this.findViewById(R.id.btn_register);
        userName = this.findViewById(R.id.user_name);
        userPwd = this.findViewById(R.id.user_pwd);
        userPwd1 = this.findViewById(R.id.user_pwd1);
        userPhone = this.findViewById(R.id.user_phone);
        userEmail = this.findViewById(R.id.user_email);

        mContext = getApplicationContext();
        myDBHelper = new DBOpenHelper(mContext, "myapp.db", null, 1);

        btnRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyRegister()){
                    saveUserMsg();
                    finish();
                }
            }
        });
    }

    private void saveUserMsg(){
        db = myDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", userName.getText().toString().trim());
        values.put("password", userPwd.getText().toString().trim());
        values.put("phone", userPhone.getText().toString().trim());
        values.put("email", userEmail.getText().toString().trim());
        values.put("remembered", "0");
        //参数依次是：表名，强行插入null值得数据列的列名，一行记录的数据
        try{
            db.insert("Tuser", null, values);
        }catch (Exception e){
            new AlertDialog.Builder(this).setMessage(e.getMessage());
        }
    }
    public boolean verifyRegister(){
        if (userName.length() == 0
                || userPwd.length() == 0
                || userPwd1.length() == 0
                || userPhone.length() == 0
                || userEmail.length() == 0){
            Toast.makeText(getApplicationContext(), "数据不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        String na = userName.getText().toString().trim();
        db = myDBHelper.getReadableDatabase();
        Cursor cursor = db.query("Tuser", null, "name=?", new String[] { na }, null, null, null);
        if(cursor.getCount() > 0){
            Toast.makeText(getApplicationContext(), "该用户名已经被注册过", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (userPwd.getText().toString().equals(userPwd1.getText().toString())){
            Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
            return true;
        }
        else{
            Toast.makeText(getApplicationContext(), "两次密码不一样", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
