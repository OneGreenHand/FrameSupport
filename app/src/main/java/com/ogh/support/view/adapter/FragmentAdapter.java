package com.ogh.support.view.adapter;


import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * 当切换页面的时候
 * FragmentPagerAdapter不会remove掉fragment而只是detach，仅仅是在页面上让fragment的UI脱离Activity的UI，但是fragment仍然保存在内存里，并不会回收内存。
 * FragmentPagerAdapter会保留页面的状态，并不会完全销毁掉。
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;

    public FragmentAdapter(FragmentManager fm, List<Fragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

}