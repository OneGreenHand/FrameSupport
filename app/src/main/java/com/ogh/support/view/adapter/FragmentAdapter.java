package com.ogh.support.view.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

/**
 * viewpage2
 * https://developer.android.google.cn/training/animation/screen-slide-2?hl=zh-cn
 */
public class FragmentAdapter extends FragmentStateAdapter {
    private List<Fragment> mFragments;

    public FragmentAdapter(FragmentActivity activity, List<Fragment> mFragments) {
        super(activity);
        this.mFragments = mFragments;
    }

    public FragmentAdapter(Fragment fragment, List<Fragment> mFragments) {
        super(fragment);
        this.mFragments = mFragments;
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