package com.frame.support.view.activity;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
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
import com.meituan.android.walle.WalleChannelReader;

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
    private MenuItem menuItem;

    @Override
    protected void init(Bundle savedInstanceState) {
    }

    @Override
    protected void initData() {
        mDatas.add(new HomeFragment());
        mDatas.add(new GameFragment());
        mDatas.add(new OnlineFragment());
        mDatas.add(new MineFragment());
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
                //实现沉浸式有两种，一种是在onPageSelected()根据position处理，另一种是直接在每个fragment实现(本示例未做处理)
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
        ToastUtil.showShortToast("渠道号为：" + WalleChannelReader.get(this, "CHANNEL"));//需要通过walle手动注入渠道信息，不然获取不到
        //WalleChannelReader.get(this, "CHANNEL");//使用walle获取到的渠道号
        // ChannelUtils.getChannel();//这里获取到的信息是build中manifestPlaceholders对应的CHANNEL信息
    }
}