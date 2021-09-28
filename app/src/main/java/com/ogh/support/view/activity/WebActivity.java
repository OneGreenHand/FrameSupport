package com.ogh.support.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.WindowManager;

import com.frame.base.activity.BaseActivity;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.ogh.support.R;
import com.ogh.support.databinding.ActivityWebBinding;

/**
 * 可以播放视频的WebView
 */
public class WebActivity extends BaseActivity<ActivityWebBinding> {

    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
    }

    public static void openActivity(Context context) {
        context.startActivity(new Intent(context, WebActivity.class));
    }

    public static void openActivity(Context context, boolean isHorizontalScreen) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("isHorizontalScreen", isHorizontalScreen);
        context.startActivity(intent);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);//为了避免视频闪屏和透明问题
        boolean isHorizontalScreen = getIntent().getBooleanExtra("isHorizontalScreen", false);
        viewBinding.webView.setProgressBar(viewBinding.pbWebBase);
        if (isHorizontalScreen) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);//横屏可翻转
            ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR).init();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//保持屏幕常亮
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
            ImmersionBar.with(this).statusBarColor(R.color.c_FFFFFF).autoStatusBarDarkModeEnable(true, 0.2f).fitsSystemWindows(true).init();
        }
        viewBinding.webView.loadUrl("https://www.baidu.com");
        //  mWebView.loadUrl("http://debugtbs.qq.com");//用来检测X5内核是否安装
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK)
            viewBinding.webView.doFileChoose(data);
    }

    @Override
    public void onBackPressed() {
        if (viewBinding.webView.canGoBack()) {
            viewBinding.webView.goBack();
        } else
            finish();
    }

    @Override
    protected void onDestroy() {
        viewBinding.webView.destroy();//为了使WebView退出时音频或视频关闭
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            viewBinding.webView.onResume();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            viewBinding.webView.onPause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}