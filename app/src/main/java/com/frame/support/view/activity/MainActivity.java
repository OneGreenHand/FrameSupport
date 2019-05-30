package com.frame.support.view.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.frame.base.activity.BaseActivity;
import com.frame.support.R;
import com.frame.support.view.fragment.GameFragment;
import com.frame.support.view.fragment.HomeFragment;
import com.frame.support.view.fragment.MineFragment;
import com.frame.support.view.fragment.OnlineFragment;
import com.frame.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.navigation_view)
    BottomNavigationView navigationView;
    private HomeFragment homeFragment;
    private GameFragment gameFragment;
    private OnlineFragment onlineFragment;
    private MineFragment mineFragment;
    private List<Fragment> mDatas = new ArrayList<>();
    //记录用户首次点击返回键的时间
    private long firstTime = 0;
    private MenuItem menuItem;

    @Override
    protected void init(Bundle savedInstanceState) {
    }

    @Override
    protected void initData() {
        homeFragment = new HomeFragment();
        gameFragment = new GameFragment();
        onlineFragment = new OnlineFragment();
        mineFragment = new MineFragment();
        mDatas.add(homeFragment);
        mDatas.add(gameFragment);
        mDatas.add(onlineFragment);
        mDatas.add(mineFragment);
        viewPager.setOffscreenPageLimit(mDatas.size());
        initAdapter();
        initViewPagerChangeListener();
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                menuItem = item;
                int i = item.getItemId();
                if (i == R.id.home) {
                    viewPager.setCurrentItem(0);
                } else if (i == R.id.game) {
                    viewPager.setCurrentItem(1);
                } else if (i == R.id.core) {
                    return false;
                } else if (i == R.id.online) {
                    viewPager.setCurrentItem(2);
                } else if (i == R.id.mine) {
                    viewPager.setCurrentItem(3);
                }
                return true;
            }
        });
    }

    private void initAdapter() {
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mDatas.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mDatas.get(arg0);
            }
        };
        viewPager.setAdapter(adapter);
    }

    private void initViewPagerChangeListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //TODO 这里使用的是viewpage+framge方式，暂未实现沉浸式状态栏，如需实现，只需在此方法中实现即可，具体可参考 https://github.com/gyf-dev/ImmersionBar/blob/master/sample/src/main/java/com/gyf/immersionbar/activity/FragmentThreeActivity.java
                if (position >= 2)//第三个为悬浮按钮
                    position++;
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    navigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = navigationView.getMenu().getItem(position);
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
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                AppUtils.exitApp();
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.floating_action)
    public void onViewClicked() {
        ToastUtil.showShortToast( "MvpFrame");
    }
}