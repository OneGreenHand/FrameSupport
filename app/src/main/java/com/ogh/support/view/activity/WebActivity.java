package com.ogh.support.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;

import com.frame.base.activity.BaseActivity;
import com.gyf.immersionbar.ImmersionBar;
import com.ogh.support.R;
import com.ogh.support.databinding.ActivityWebBinding;

/**
 * 可以播放视频的webview
 */
public class WebActivity extends BaseActivity<ActivityWebBinding> {

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
        viewBinding.webView.setProgressBar(viewBinding.pbWebBase);
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