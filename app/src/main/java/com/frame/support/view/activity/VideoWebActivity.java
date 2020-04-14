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
    protected void initImmersionBar(int color) {
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
        mWebView.loadUrl("https://rayapi.livet.cn/m3u8url/raybet?url=https%3A%2F%2Fwww.huomao.com%2F8438&teams=Team%20Aspirations-Team%20Venture");
        //  mWebView.loadUrl("http://debugtbs.qq.com");//检测X5内核是否安装
    }

    @Override
    protected void initData() {

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