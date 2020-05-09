package com.frame.support.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.frame.base.activity.BaseActivity;
import com.frame.support.R;
import com.frame.support.webview.VideoWebView;
import com.frame.support.widget.TitleBarLayout;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;

/**
 * 可以播放视频的webview
 */
public class VideoWebActivity extends BaseActivity {

    @BindView(R.id.web_view)
    FrameLayout mViewParent;
    @BindView(R.id.pb_web_base)
    ProgressBar mProgressBar;
    @BindView(R.id.titlebar)
    TitleBarLayout titlebar;
    private VideoWebView mWebView;

    @Override
    protected void initImmersionBar(int color) {//不这样写，如果播放视频全屏，状态栏显示异常
        ImmersionBar.with(this).statusBarColor(R.color.colorAccent).init();//状态栏颜色(布局文件设置了fitsSystemWindows="true")
    }

    public static void openActivity(Context context) {
        context.startActivity(new Intent(context, VideoWebActivity.class));
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);//为了避免视频闪屏和透明问题
        mWebView = new VideoWebView(this, null);
        mViewParent.addView(mWebView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
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
        return R.layout.activity_video_web;
    }

}