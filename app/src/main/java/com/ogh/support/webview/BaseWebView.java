package com.ogh.support.webview;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.frame.util.ToastUtil;
import com.ogh.support.R;
import com.ogh.support.util.ImageSaveUtil;


/**
 * 基类 WebView
 * 备注: 使用的时候不要设置 android:scrollbars="none"，不然部分机型会显示空白
 */
public class BaseWebView extends WebView {
    private ProgressBar mProgressBar;
    private ValueCallback<Uri[]> filePathCallback;

    public BaseWebView(Context context) {
        super(context);
        init(context);
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void init(Context context) {
        WebSettings webSettings = getSettings();
        webSettings.setMediaPlaybackRequiresUserGesture(false);// 允许自动播放多媒体
        webSettings.setLoadsImagesAutomatically(true);//设置自动加载图片
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//设置允许js弹出alert对话框
        webSettings.setUseWideViewPort(true);  //设置webview推荐使用的窗口，使html界面自适应屏幕
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setDomStorageEnabled(true);//DOM存储API是否可用
        webSettings.setDatabaseEnabled(true);//数据库存储API是否可用
        webSettings.setAllowFileAccess(true);//使用 File 协议
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)//支持同时加载Https和Http混合模式  
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不缓存
        setDownloadListener(new DownloadListener() {//下载事件
            @Override
            public void onDownloadStart(String s, String s1, String s2, String s3, long l) {
                try {
                    if (s.startsWith("data:image")) {//下载图片
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {//android 10以下
                            PermissionUtils.permission(PermissionConstants.STORAGE)
                                    .callback(new PermissionUtils.SimpleCallback() {
                                        @Override
                                        public void onGranted() {
                                            SaveImage(s);
                                        }

                                        @Override
                                        public void onDenied() {
                                            ToastUtil.showShortToast("没有权限");
                                        }
                                    })
                                    .request();
                        } else
                            SaveImage(s);
                    } else {//调用系统下载器
                        Intent intent = new Intent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction("android.intent.action.VIEW");
                        intent.setData(Uri.parse(s));
                        getContext().startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                BaseWebView.this.filePathCallback = filePathCallback;
                if (getContext() instanceof Activity) {
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("image/*");
                    ((Activity) getContext()).startActivityForResult(Intent.createChooser(i, "File Browser"), 1);
                    return true;
                }
                return false;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (mProgressBar != null)
                    mProgressBar.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public boolean onJsAlert(WebView webView, String url, String message, JsResult jsResult) {
                AlertDialog.Builder b = new AlertDialog.Builder(context);
                b.setTitle("提示");
                b.setMessage(message);
                b.setPositiveButton(getResources().getString(R.string.sure), new DialogInterface.OnClickListener() {
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
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
     * 保存图片到本地
     *
     * @param url 图片地址 data:image/png;base64格式
     */
    public void SaveImage(String url) {
        try {
            byte[] bytes = Base64.decode(url.split(",")[1], Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            if (bitmap == null) {
                ToastUtil.showShortToast("下载失败,请稍后重试!");
                return;
            }
            Uri uri = ImageSaveUtil.saveAlbum(getContext(), bitmap, Bitmap.CompressFormat.JPEG, 90, true);
            ToastUtil.showShortToast(uri == null ? "下载失败,请稍后重试!" : "图片下载完成");
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showShortToast("下载失败,请稍后重试!");
        }
    }

    public void doFileChoose(Intent data) {
        if (filePathCallback == null)
            return;
        if (data == null) {
            filePathCallback.onReceiveValue(null);
            return;
        }
        Uri[] results = null;
        String dataString = data.getDataString();
        ClipData clipData = data.getClipData();
        if (clipData != null) {
            results = new Uri[clipData.getItemCount()];
            for (int i = 0; i < clipData.getItemCount(); i++) {
                ClipData.Item item = clipData.getItemAt(i);
                results[i] = item.getUri();
            }
        }
        if (dataString != null)
            results = new Uri[]{Uri.parse(dataString)};
        filePathCallback.onReceiveValue(results);
        filePathCallback = null;
    }

    public void setProgressBar(ProgressBar progressBar) {
        mProgressBar = progressBar;
        mProgressBar.setMax(100);  //设置加载进度最大值
    }
}