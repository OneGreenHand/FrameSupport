package com.ogh.support.view.activity;


import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.AppUtils;
import com.frame.base.activity.BaseActivity;
import com.frame.util.ToastUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ogh.support.R;
import com.ogh.support.util.ChannelUtils;
import com.ogh.support.view.adapter.FragmentAdapter;
import com.ogh.support.view.fragment.HomeFragment;
import com.ogh.support.view.fragment.MineFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.navigation_view)
    BottomNavigationView navigationView;
    private List<Fragment> mDatas = new ArrayList<>();
    //记录用户首次点击返回键的时间
    private long firstTime = 0;

    @Override
    protected void init(Bundle savedInstanceState) {
        mDatas.add(new HomeFragment());
        mDatas.add(new MineFragment());
        viewPager.setOffscreenPageLimit(mDatas.size());
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), mDatas));
        initViewPagerChangeListener();
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.home) {
                    viewPager.setCurrentItem(0);
                } else if (i == R.id.core) {
                    return false;
                } else if (i == R.id.mine)
                    viewPager.setCurrentItem(1);
                return true;
            }
        });
    }

    private void initViewPagerChangeListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1)//第二个为悬浮按钮
                    position++;
                MenuItem menuItem = navigationView.getMenu().getItem(position);
                if (menuItem != null)
                    menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else
                AppUtils.exitApp();
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.floating_action)
    public void onViewClicked() {
        ToastUtil.showShortToast("当前渠道号为: " + ChannelUtils.getChannel());
    }
}