package com.frame.support.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.frame.base.activity.BaseActivity;
import com.frame.support.R;
import com.frame.support.webview.X5WebView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 可以播放视频的webview
 */
public class VideoWebActivity extends BaseActivity {

    @BindView(R.id.web_view)
    X5WebView webView;
    @BindView(R.id.pb_web_base)
    ProgressBar mProgressBar;
    @BindView(R.id.app_title)
    TextView appTitle;

    public static void openActivity(Context context) {
        context.startActivity(new Intent(context, VideoWebActivity.class));
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);//为了避免视频闪屏和透明问题
        webView.setTextView(appTitle);
        webView.setProgressBar(mProgressBar);
        webView.loadUrl("https://www.baidu.com/");
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onBackPressed() {
        commonFinish();
    }

    private void commonFinish() {
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


    @OnClick(R.id.img_finish)
    public void onViewClicked() {
        commonFinish();
    }
}