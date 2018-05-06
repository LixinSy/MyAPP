package com.lx.myapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{
    private static final int REQUEST_CODE = 1;
    private RadioGroup radioGroup;
    private RadioButton rb_Near;
    private RadioButton rb_Scan;
    private RadioButton rb_baike;
    private RadioButton rb_Find;
    private RadioButton currentRadio;

    private String figureUrl;
    private String nickStr;
    private CircleImageView qqFigure;
    private TextView nickName;
    private TextView qqNote;

    private FragmentTransaction fTransaction;
    private FragmentManager fManager;
    private DrawerLayout drawerLayout;

    private NearFragment nearFragment;
    private ScanFragment scanFragment;
    private BaikeFragment baikeFragment;
    private FindFragment findFragment;
    private Fragment currentFragment;

    private LinearLayout userPenal;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        fManager = getFragmentManager();
        fTransaction = fManager.beginTransaction();

        fTransaction.add(R.id.fg_content, findFragment);
        fTransaction.show(findFragment);
        fTransaction.commit();
        currentFragment = findFragment;
        currentRadio = rb_Find;
        rb_Find.setChecked(true);
        radioGroup.check(currentRadio.getId());
        checkRunTimePermissionAndLocateOnTheMap();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.tab_near:
                switchFragment(nearFragment, rb_Near);
                break;
            case R.id.tab_scan:
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
                break;
            case R.id.tab_baike:
                switchFragment(baikeFragment,rb_baike);
                break;
            case R.id.tab_find:
                switchFragment(findFragment,rb_Find);
                break;
        }
    }

    //初始化所有View
    private void initView(){
        drawerLayout = findViewById(R.id.drawer_layout);
        userPenal = findViewById(R.id.user_penal);
        radioGroup = findViewById(R.id.tab_menu_bar);
        qqFigure = findViewById(R.id.qq_figure);
        qqNote = findViewById(R.id.qq_note);
        nickName = findViewById(R.id.nickname);
        rb_Near = findViewById(R.id.tab_near);
        rb_Scan = findViewById(R.id.tab_scan);
        rb_baike = findViewById(R.id.tab_baike);
        rb_Find = findViewById(R.id.tab_find);
        nearFragment = new NearFragment();
        scanFragment =new ScanFragment();
        baikeFragment = new BaikeFragment();
        findFragment = new FindFragment();

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
        radioGroup.setOnCheckedChangeListener(this);
        userPenal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
                startActivity(intent);
            }
        });

        // 创建DisplayImageOptions对象并进行相关选项配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.img_broken)// 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.img_broken)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.img_broken)// 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                //.displayer(new RoundedBitmapDisplayer(20))// 设置成圆角图片
                .build();// 创建DisplayImageOptions对象
        try{
            Intent intent = this.getIntent();
            Bundle data = intent.getBundleExtra("qq_data");
            nickStr = data.getString("nickname");
            figureUrl = data.getString("figure");
            nickName.setText(nickStr);
            if(!figureUrl.isEmpty()){
                //Uri uri = Uri.parse(figureUrl);
                imageLoader.displayImage(figureUrl,qqFigure);
            }
            qqNote.setText(User.getNote());
            User.setFigurePath(figureUrl);
        }catch (Exception e){
            //nickName.setText("");
        }
    }

    //处理权限问题
    private  void checkRunTimePermissionAndLocateOnTheMap() {
        if(Build.VERSION.SDK_INT >= 23) {
        /*
         *  判断一下，在6.0以上版本的Android系统中需要申请权限
         *  以下这些权限都是百度地图Api当中所用到的，且必须动态申请的权限
         */
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                requestRunTimePermission();
            } else {
                Toast.makeText(this, "已经授予了权限", Toast.LENGTH_LONG);
            }
        }
        else {
        }
    }

    private void requestRunTimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_PHONE_STATE)
                || ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)) {
        /*
         *  如果没有获得过用户的权限许可，则向用户申请
         */
            Snackbar.make(this.findViewById(R.id.drawer_layout), "应用程序需要您的授权",
                    Snackbar.LENGTH_INDEFINITE).setAction("bb", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_PHONE_STATE,
                                    Manifest.permission.CAMERA
                            },REQUEST_CODE);
                }
            }).show();
        } else {
        /*
         *  如果已经获得过用户的权限许可了，则直接申请运行时权限即可
         */
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.CAMERA
                    },REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (verifyPermissions(grantResults)) {
                Snackbar.make(this.findViewById(R.id.drawer_layout), "没有得到允许",Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(this.findViewById(R.id.drawer_layout), "得到允许",Snackbar.LENGTH_SHORT).show();
            }
        } else {
            //
        }
    }

    boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if(grantResults.length < 1){
            return false;
        }
        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    //切换Fragment的方法
    public void switchFragment(Fragment fragment, RadioButton rb) {
        fTransaction = fManager.beginTransaction();
        if (currentFragment.equals(fragment))
            return;
        if (fragment.isAdded()){
            fTransaction.hide(currentFragment).show(fragment);
        }else {
            fTransaction.hide(currentFragment).add(R.id.fg_content, fragment);
        }
        currentFragment = fragment;
        currentRadio = rb;
        fTransaction.commit();
        //rb.setChecked(true);
    }

    @Override
    protected void onDestroy() {
        // 回收该页面缓存在内存中的图片
        imageLoader.clearMemoryCache();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){

    }

    @Override
    protected void onStart() {
        currentRadio.setChecked(true);
        radioGroup.check(currentRadio.getId());
        imageLoader.displayImage(User.getFigurePath(),qqFigure);
        nickName.setText(User.getNickname());
        super.onStart();
    }
}
