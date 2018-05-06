package com.lx.myapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

public class UserInfoActivity extends AppCompatActivity{

    private RelativeLayout figureLayout;
    private RelativeLayout nickNameLayout;
    private RelativeLayout sexLayout;
    private RelativeLayout noteLayout;

    private String figurePath;
    private CircleImageView figure;
    private TextView nickName;
    private TextView sex;
    private TextView note;
    private EditText et = null;

    private String inputRes = "";
    private String sexItems[] = {"男", "女"};
    private String selectedSex = "";
    private AlertDialog dialog;
    private Context context;
    private int IMAGE = 1;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        context = getApplicationContext();
        bindView();
    }

    void bindView(){

        figureLayout = findViewById(R.id.figure_layout);
        nickNameLayout = findViewById(R.id.nick_name_layout);
        sexLayout = findViewById(R.id.sex_layout);
        noteLayout = findViewById(R.id.note_layout);
        figure = findViewById(R.id.figure);
        nickName = findViewById(R.id.nick_name);
        sex = findViewById(R.id.sex);
        note = findViewById(R.id.note);

        nickNameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et =  new EditText(context);
                et.setTextColor(Color.parseColor("#222222"));
                dialog = new AlertDialog.Builder(UserInfoActivity.this)
                        .setTitle("输入昵称")//设置对话框的标题
                        .setView(et)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                inputRes = "";
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                inputRes = et.getText().toString().trim();
                                if (!inputRes.isEmpty()){
                                    nickName.setText(inputRes);
                                }
                                Toast.makeText(UserInfoActivity.this, inputRes, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            }
        });

        sexLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(UserInfoActivity.this)
                        .setTitle("选择性别")//设置对话框的标题
                        .setSingleChoiceItems(sexItems, 1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedSex = sexItems[which];
                                Toast.makeText(UserInfoActivity.this, sexItems[which], Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!selectedSex.isEmpty()){
                                    sex.setText(selectedSex);
                                }
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            }
        });

        noteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et =  new EditText(context);
                et.setTextColor(Color.parseColor("#222222"));
                et.setMinLines(3);
                dialog = new AlertDialog.Builder(UserInfoActivity.this)
                        .setTitle("输入签名")//设置对话框的标题
                        .setView(et)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                inputRes = "";
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                inputRes = et.getText().toString().trim();
                                if (!inputRes.isEmpty()){
                                    note.setText(inputRes);
                                }
                                Toast.makeText(UserInfoActivity.this, inputRes, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            }
        });

        figureLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用相册
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            figurePath = c.getString(columnIndex);
            if (figurePath != null && !figurePath.isEmpty()){
                Uri u = Uri.parse(figurePath);
                figure.setImageURI(u);
                User.setFigurePath(figurePath);
            }
            c.close();
        }
    }

    @Override
    protected void onStart() {
        figurePath = User.getFigurePath();
        imageLoader.displayImage(figurePath,figure);
        nickName.setText(User.getNickname());
        sex.setText(User.getSex());
        note.setText(User.getNote());
        super.onStart();
    }
    @Override
    public void onBackPressed(){
        User.setFigurePath(figurePath);
        User.setNickname(nickName.getText().toString().trim());
        User.setSex(sex.getText().toString().trim());
        User.setNote(note.getText().toString().trim());
        super.onBackPressed();
    }
}
