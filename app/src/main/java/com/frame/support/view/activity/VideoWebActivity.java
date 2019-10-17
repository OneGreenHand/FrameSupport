package com.frame.support.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;

import com.frame.base.activity.BaseActivity;
import com.frame.support.R;
import com.frame.support.webview.X5WebView;

import butterknife.BindView;

/**
 * 可以播放视频的webview
 */
public class VideoWebActivity extends BaseActivity {

    @BindView(R.id.web_view)
    X5WebView webView;

    public static void openActivity(Context context) {
        context.startActivity(new Intent(context, VideoWebActivity.class));
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initTitleBar("可播放视频的WebView");
        getWindow().setFormat(PixelFormat.TRANSLUCENT);//为了避免视频闪屏和透明问题
        webView.isShowLoading(false);//不显示加载框(默认为显示)
        webView.loadUrl("https://www.baidu.com/");
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();//为了使WebView退出时音频或视频关闭
            webView = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (webView != null)
                webView.onResume();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (webView != null)
                webView.onPause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_video_web;
    }
}