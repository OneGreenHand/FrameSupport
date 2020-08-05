package com.ogh.support.view.adapter;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * 当切换页面的时候
 * FragmentStatePagerAdapter会remove之前加载的fragment从而将内存释放掉。
 * FragmentStatePagerAdapter会完全销毁滑动过去的item，当需要初始化的时候，会重新初始化页面
 */
public class TabLayoutFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;
    private List<String> titles;

    public TabLayoutFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        this.mFragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null && position < titles.size())
            return titles.get(position);
        return "";
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
