package com.lx.myapp;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Created by 李莘 on 2018/5/4.
 */

public class PlantPagerAdapter extends FragmentPagerAdapter {
    private final int PAGER_COUNT = 2;
    public PlantInfoFragment plantInfoFragment = null;
    public PlantPhotosFragment plantPhotosFragment = null;


    public PlantPagerAdapter(FragmentManager fm) {
        super(fm);
        plantInfoFragment = new PlantInfoFragment();
        plantPhotosFragment = new PlantPhotosFragment();
    }


    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case PlantActivity.PAGE_ONE:
                fragment = plantInfoFragment;
                break;
            case PlantActivity.PAGE_TWO:
                fragment = plantPhotosFragment;
                break;
        }
        return fragment;
    }
}
