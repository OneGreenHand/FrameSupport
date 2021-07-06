package com.ogh.support.view.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.blankj.utilcode.util.AppUtils;
import com.frame.base.activity.BaseActivity;
import com.frame.util.ToastUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.ogh.support.R;
import com.ogh.support.databinding.ActivityMainBinding;
import com.ogh.support.util.ChannelUtils;
import com.ogh.support.view.adapter.FragmentAdapter;
import com.ogh.support.view.fragment.HomeFragment;
import com.ogh.support.view.fragment.MineFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    protected void init(Bundle savedInstanceState) {
        if (!this.isTaskRoot()) {//第一次安装成功点击“打开”后Home键切出应用后再点击桌面图标返回导致应用重启问题(在配置了Intent.ACTION_MAIN的Activity中添加)
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                finish();
                return;
            }
        }
        List<Fragment> mFragment = new ArrayList<>();
        mFragment.add(new HomeFragment());
        mFragment.add(new MineFragment());
        //   viewBinding.viewPager.setUserInputEnabled(false);//禁止滑动
        //    viewBinding.viewPager.setOffscreenPageLimit(mFragment.size());//设置缓存,数量超过2可设置
        viewBinding.viewPager.setAdapter(new FragmentAdapter(this, mFragment));
        initViewPagerChangeListener();
        viewBinding.navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.home) {
                    viewBinding.viewPager.setCurrentItem(0, false);
                } else if (i == R.id.core) {
                    return false;
                } else if (i == R.id.mine)
                    viewBinding.viewPager.setCurrentItem(1, false);
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

    private long firstTime = 0;  //记录用户首次点击返回键的时间

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                ToastUtil.showShortToast("再按一次退出");
                firstTime = secondTime;
                return true;
            } else
                AppUtils.exitApp();
        }
        return super.onKeyDown(keyCode, event);
    }

}