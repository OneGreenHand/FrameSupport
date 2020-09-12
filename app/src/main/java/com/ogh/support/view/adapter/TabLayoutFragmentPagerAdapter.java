package com.ogh.support.view.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class TabLayoutFragmentPagerAdapter extends FragmentStateAdapter {

    private List<Fragment> mFragments;
    private List<String> titles;

    public TabLayoutFragmentPagerAdapter(FragmentActivity activity, List<Fragment> fragments, List<String> titles) {
        super(activity);
        this.mFragments = fragments;
        this.titles = titles;
    }

    public TabLayoutFragmentPagerAdapter(Fragment fragment, List<Fragment> fragments, List<String> titles) {
        super(fragment);
        this.mFragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment createFragment(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragments.size();
    }
}