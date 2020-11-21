package com.ogh.support.view.activity;


import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.blankj.utilcode.util.AppUtils;
import com.frame.base.activity.BaseActivity;
import com.frame.util.ToastUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ogh.support.R;
import com.ogh.support.databinding.ActivityMainBinding;
import com.ogh.support.util.ChannelUtils;
import com.ogh.support.view.adapter.FragmentAdapter;
import com.ogh.support.view.fragment.HomeFragment;
import com.ogh.support.view.fragment.MineFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private List<Fragment> mDatas = new ArrayList<>();
    private long firstTime = 0;  //记录用户首次点击返回键的时间

    @Override
    protected void init(Bundle savedInstanceState) {
        mDatas.add(new HomeFragment());
        mDatas.add(new MineFragment());
        //   viewBinding.viewPager.setUserInputEnabled(false);//禁止滑动
        //    viewBinding.viewPager.setOffscreenPageLimit(mDatas.size());//设置缓存,数量超过2可设置
        viewBinding.viewPager.setAdapter(new FragmentAdapter(this, mDatas));
        initViewPagerChangeListener();
        viewBinding.navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.home) {
                    viewBinding.viewPager.setCurrentItem(0);
                } else if (i == R.id.core) {
                    return false;
                } else if (i == R.id.mine)
                    viewBinding.viewPager.setCurrentItem(1);
                return true;
            }
        });
        viewBinding.floatingAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShortToast("当前渠道号为: " + ChannelUtils.getChannel());
            }
        });
    }

    private void initViewPagerChangeListener() {
        viewBinding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 1)//第二个为悬浮按钮
                    position++;
                MenuItem menuItem = viewBinding.navigationView.getMenu().getItem(position);
                if (menuItem != null)
                    menuItem.setChecked(true);
            }
        });
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

}