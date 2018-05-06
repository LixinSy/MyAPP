package com.lx.myapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

//tencent
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements OnClickListener{

    private Button btnLogin;
    private TextView textRegister;
    private TextView textFindPwd;
    private EditText loginName;
    private EditText loginPwd;
    private CheckBox checkBox;

    //private DBOpenHelper dbOpenHelper;
    private String figureurl;
    private String nickName;

    //tentcent
    private static final String APP_ID = "1106783580";//官方获取的APPID
    private Tencent mTencent;
    private LoginActivity.BaseUiListener mIUiListener;
    private UserInfo mUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mTencent = Tencent.createInstance(APP_ID,LoginActivity.this.getApplicationContext());
        this.bindView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Map<String,String> data = getRememberedUser();
        if (data.isEmpty()){
            return;
        }
        loginName.setText(data.get("name"));
        loginPwd.setText(data.get("password"));
        if (!data.get("name").isEmpty()){
            checkBox.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch(viewId){
            case R.id.btn_login:
                if (verifyLogin()){
                    Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    Bundle data = new Bundle();
                    data.putString("nickname",loginName.getText().toString().trim());
                    data.putString("figure", "@drawable/figures");
                    intent.putExtra("qq_data",data);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.text_register:
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.text_find_pwd:
                Toast.makeText(getApplicationContext(), "找回密码", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(MainActivity.this,ScanNumActivity.class));
                break;
            case R.id.img_qq:
                break;
        }
    }

    private void bindView(){
        btnLogin = this.findViewById(R.id.btn_login);
        textRegister = this.findViewById(R.id.text_register);
        textFindPwd = this.findViewById(R.id.text_find_pwd);
        loginName = findViewById(R.id.login_name);
        loginPwd = findViewById(R.id.login_pwd);
        checkBox = findViewById(R.id.checkbox1);

        btnLogin.setOnClickListener(this);
        textRegister.setOnClickListener(this);
        textFindPwd.setOnClickListener(this);
    }

    private boolean verifyLogin(){
        return true;
    }

    public void rememberUser(String username, String passwd) {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("userdata", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name", username);
        editor.putString("password", passwd);
        editor.commit();
    }

    public void removeUser(){
        SharedPreferences sp = getApplicationContext().getSharedPreferences("userdata", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if(sp.contains("name")){
            editor.remove("name");
            editor.commit();
        }
    }

    public Map<String, String> getRememberedUser() {
        Map<String, String> data = new HashMap<String, String>();
        SharedPreferences sp = getApplicationContext().getSharedPreferences("userdata", Context.MODE_PRIVATE);
        if(sp.contains("name")){
            data.put("name", sp.getString("name", ""));
            data.put("password", sp.getString("password", ""));
        }
        return data;
    }

    public void qqLogin(View v){
        /**通过这句代码，SDK实现了QQ的登录，这个方法有三个参数，第一个参数是context上下文，第二个参数SCOPO 是一个String类型的字符串，表示一些权限
         官方文档中的说明：应用需要获得哪些API的权限，由“，”分隔。例如：SCOPE = “get_user_info,add_t”；所有权限用“all”
         第三个参数，是一个事件监听器，IUiListener接口的实例，这里用的是该接口的实现类 */
        mIUiListener = new LoginActivity.BaseUiListener();
        //all表示获取所有权限
        mTencent.login(LoginActivity.this,"all", mIUiListener);
    }

    /**
     * 自定义监听器实现IUiListener接口后，需要实现的3个方法
     * onComplete完成 onError错误 onCancel取消
     */
    private class BaseUiListener implements IUiListener{

        @Override
        public void onComplete(Object response) {
            Toast.makeText(LoginActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
            //Log.e(TAG, "response:" + response);
            JSONObject obj = (JSONObject) response;
            try {
                String openID = obj.getString("openid");
                String accessToken = obj.getString("access_token");
                String expires = obj.getString("expires_in");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken,expires);
                QQToken qqToken = mTencent.getQQToken();
                mUserInfo = new UserInfo(getApplicationContext(),qqToken);
                mUserInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object object) {
                        JSONObject jb = (JSONObject) object;
                        try {
                            nickName = jb.getString("nickname");
                            figureurl = jb.getString("figureurl_qq_2");  //头像图片的url
                            String sex = jb.getString("gender");
                            User.setNickname(nickName);
                            User.setSex(sex);
                            User.setFigurePath(figureurl);
                            User.setNote("春风十里不及你...");
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            Bundle data = new Bundle();
                            data.putString("nickname",nickName);
                            data.putString("figure", figureurl);
                            intent.putExtra("qq_data",data);
                            startActivity(intent);
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(UiError uiError) {
                        //Log.e(TAG,"登录失败"+uiError.toString());
                    }

                    @Override
                    public void onCancel() {
                        //Log.e(TAG,"登录取消");
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(LoginActivity.this, "授权失败", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this, "授权取消", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 在调用Login的Activity或者Fragment中重写onActivityResult方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.REQUEST_LOGIN){
            Tencent.onActivityResultData(requestCode,resultCode,data,mIUiListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
