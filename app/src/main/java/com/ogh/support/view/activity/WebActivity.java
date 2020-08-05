package com.ogh.support.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.frame.base.activity.BaseActivity;
import com.ogh.support.R;
import com.ogh.support.webview.BaseWebView;
import com.ogh.support.widget.TitleBarLayout;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;

/**
 * 可以播放视频的webview
 */
public class WebActivity extends BaseActivity {

    @BindView(R.id.web_view)
    BaseWebView mWebView;
    @BindView(R.id.pb_web_base)
    ProgressBar mProgressBar;
    @BindView(R.id.titlebar)
    TitleBarLayout titlebar;

    @Override
    protected void initImmersionBar(int color) {//不这样写，如果播放视频全屏，状态栏显示异常
        ImmersionBar.with(this).statusBarColor(R.color.colorPrimary).statusBarDarkFont(true, 0.2f).init();//状态栏颜色(布局文件设置了fitsSystemWindows="true")
    }

    public static void openActivity(Context context) {
        context.startActivity(new Intent(context, WebActivity.class));
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);//为了避免视频闪屏和透明问题
        mWebView.setTextView(titlebar.getTitleText());
        mWebView.setProgressBar(mProgressBar);
        mWebView.loadUrl("https://www.baidu.com");
        //  mWebView.loadUrl("http://debugtbs.qq.com");//用来检测X5内核是否安装
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.destroy();//为了使WebView退出时音频或视频关闭
            mWebView = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (mWebView != null)
                mWebView.onResume();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (mWebView != null)
                mWebView.onPause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_web;
    }

}