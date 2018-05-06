package com.lx.myapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class PlantActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener{

    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;

    private Context context;
    private RadioGroup radioGroup;
    private RadioButton rbInfo;
    private RadioButton rbPhotos;
    private ViewPager pager;

    private PlantPagerAdapter plantPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant);

        context = getApplicationContext();
        plantPagerAdapter = new PlantPagerAdapter(getSupportFragmentManager());
        initView();
        rbInfo.setChecked(true);
    }

    private void initView(){
        radioGroup = findViewById(R.id.top_bar);
        rbInfo = findViewById(R.id.tab_info);
        rbPhotos = findViewById(R.id.tab_photos);
        pager = findViewById(R.id.Page_plant_content);

        pager.setAdapter(plantPagerAdapter);
        pager.setCurrentItem(0);
        pager.addOnPageChangeListener(this);
        radioGroup.setOnCheckedChangeListener(this);

        try{
            Intent intent = this.getIntent();
            Bundle data = intent.getBundleExtra("plant_data");
            String plantName = data.getString("plant_key");
            Bundle infoBundle = new Bundle();
            infoBundle.putString("plant_name",plantName);
            plantPagerAdapter.plantInfoFragment.setArguments(infoBundle);
        }catch (Exception e){
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.tab_info:
                pager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.tab_photos:
                pager.setCurrentItem(PAGE_TWO);
                break;
        }
    }
    //重写ViewPager页面切换的处理方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (pager.getCurrentItem()) {
                case PAGE_ONE:
                    rbInfo.setChecked(true);
                    break;
                case PAGE_TWO:
                    rbPhotos.setChecked(true);
                    break;
            }
        }
    }
}
