package com.frame.support.webview;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * 基类 WebView(集成腾讯X5)
 * 备注: 使用的时候不要设置 android:scrollbars="none"，不然部分机型会显示空白
 */
public class BaseWebView extends WebView {
    private TextView mTextView;
    private ProgressBar mProgressBar;

    public BaseWebView(Context context) {
        super(context);
        init(context);
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        WebSettings webSettings = getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不缓存
        webSettings.setLoadsImagesAutomatically(Build.VERSION.SDK_INT >= 19);//图片自动缩放 打开
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//设置允许js弹出alert对话框
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//自适应屏幕
        webSettings.setLoadWithOverviewMode(true);//缩放至屏幕的大小
        webSettings.setAllowFileAccess(true);  // 设置是否允许 WebView 使用 File 协议
        webSettings.setDatabaseEnabled(true);//数据库存储API是否可用，默认值false
        webSettings.setDomStorageEnabled(true);//是否开启本地DOM存储
        setDownloadListener(new DownloadListener() {//下载事件
            @Override
            public void onDownloadStart(String s, String s1, String s2, String s3, long l) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(s));
                getContext().startActivity(intent);
            }
        });
        setWebChromeClient(new WebChromeClient() {

            View myVideoView;
            View myNormalView;
            IX5WebChromeClient.CustomViewCallback callback;

            /**
             * 全屏播放配置
             */
            @Override
            public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback customViewCallback) {
                FrameLayout normalView = BaseWebView.this;
                ViewGroup viewGroup = (ViewGroup) normalView.getParent();
                viewGroup.removeView(normalView);
                viewGroup.addView(view);
                myVideoView = view;
                myNormalView = normalView;
                callback = customViewCallback;
            }

            /**
             * 退出全屏播放配置
             */
            @Override
            public void onHideCustomView() {
                if (callback != null) {
                    callback.onCustomViewHidden();
                    callback = null;
                }
                if (myVideoView != null) {
                    ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
                    viewGroup.removeView(myVideoView);
                    viewGroup.addView(myNormalView);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (mTextView != null)
                    mTextView.setText(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (mProgressBar != null)
                    mProgressBar.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public boolean onJsAlert(WebView webView, String url, String message, com.tencent.smtt.export.external.interfaces.JsResult jsResult) {
                AlertDialog.Builder b = new AlertDialog.Builder(context);
                b.setTitle("提示");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        jsResult.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
            }
        });
        setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                /**
                 * 防止加载网页时调起系统浏览器
                 */
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    webView.loadUrl(url);
                    return false;
                }
                try {  // Otherwise allow the OS to handle things like tel, mailto, etc.
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    getContext().startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView webView, String url, Bitmap favicon) {
                if (mProgressBar != null)
                    mProgressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(webView, url, favicon);
            }

            @Override
            public void onPageFinished(WebView webView, String url) {
                super.onPageFinished(webView, url);
                if (!webSettings.getLoadsImagesAutomatically())//不设置的话，部分机型不显示图片
                    webSettings.setLoadsImagesAutomatically(true);
                if (mProgressBar != null)
                    mProgressBar.setVisibility(View.GONE);
            }
        });
        addJavascriptInterface(new JSInterface(getContext()), "JSInterface");
    }

    /**
     * 自动播放视频
     */
    public void setAutoPlay() {
        WebSettings webSettings = getSettings();
        // 设置WebView是否需要用户手势才能播放媒体。默认true
        webSettings.setMediaPlaybackRequiresUserGesture(false);
    }

    public void setTextView(TextView view) {
        mTextView = view;
    }

    public void setProgressBar(ProgressBar progressBar) {
        mProgressBar = progressBar;
        mProgressBar.setMax(100);  //设置加载进度最大值
    }
}