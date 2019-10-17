package com.frame.support.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.frame.view.LoadingDialog;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * 基类 WebView(X5)
 * 备注：使用的时候不要设置 android:scrollbars="none"，不然部分机型会显示空白
 */
public class X5WebView extends com.tencent.smtt.sdk.WebView {

    private ValueCallback<Uri[]> filePathCallback;
    public static int FILECHOOSER_RESULTCODE = 1;
    private boolean isShowLoading = true;//是否显示加载框
    protected LoadingDialog progressDialog;
    //private LoadCompleteClick loadCompleteClick;

    public X5WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getSettings().setCacheMode(com.tencent.smtt.sdk.WebSettings.LOAD_NO_CACHE);//不缓存
        if (Build.VERSION.SDK_INT >= 19)
            getSettings().setLoadsImagesAutomatically(true);//图片自动缩放 打开
        else
            getSettings().setLoadsImagesAutomatically(false);//图片自动缩放 关闭
        //   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        //       getSettings().setMixedContentMode(0);
        getSettings().setJavaScriptEnabled(true); // 设置支持javascript脚本
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置允许js弹出alert对话框
        getSettings().setAllowFileAccess(true);  // 设置是否允许 WebView 使用 File 协议
        getSettings().setSupportZoom(true);// 设置可以支持缩放
        getSettings().setBuiltInZoomControls(true);// 设置出现缩放工具 是否使用WebView内置的缩放组件，由浮动在窗口上的缩放控制和手势缩放控制组成，默认false
        getSettings().setDisplayZoomControls(false);//隐藏缩放工具
        getSettings().setUseWideViewPort(true);// 扩大比例的缩放
        getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//自适应屏幕
        getSettings().setLoadWithOverviewMode(true);//缩放至屏幕的大小
        getSettings().setDatabaseEnabled(true);//数据库存储API是否可用，默认值false
        getSettings().setSavePassword(true);//保存密码
        getSettings().setDomStorageEnabled(true);//是否开启本地DOM存储  鉴于它的安全特性（任何人都能读取到它，尽管有相应的限制，将敏感数据存储在这里依然不是明智之举），Android 默认是关闭该功能的。
        //其他设置
        setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(com.tencent.smtt.sdk.WebView webView, com.tencent.smtt.sdk.ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
                X5WebView.this.filePathCallback = filePathCallback;
                if (getContext() instanceof Activity) {
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("image/*");
                    ((Activity) getContext()).startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
                    return true;
                }
                return false;
            }
        });
        setDownloadListener(new com.tencent.smtt.sdk.DownloadListener() {//下载事件
            @Override
            public void onDownloadStart(String s, String s1, String s2, String s3, long l) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(s));
                getContext().startActivity(intent);
            }
        });
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView webView, String url) {
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
            public void onPageStarted(com.tencent.smtt.sdk.WebView webView, String url, Bitmap favicon) {
                if (isShowLoading)
                    showProgressDialog("", true);
                super.onPageStarted(webView, url, favicon);
            }

            @Override
            public void onPageFinished(com.tencent.smtt.sdk.WebView webView, String url) {
                super.onPageFinished(webView, url);
                if (!getSettings().getLoadsImagesAutomatically())//不设置的话，部分机型不显示图片
                    getSettings().setLoadsImagesAutomatically(true);
                if (isShowLoading)
                    dismissProgressDialog();
                //   if (loadCompleteClick != null)
                //       loadCompleteClick.loadComplete();
            }
        });
        addJavascriptInterface(new JSInterface(getContext()), "JSInterface");
    }

    /**
     * 是否显示加载框(默认为显示)
     */
    public void isShowLoading(boolean isShow) {
        isShowLoading = isShow;
    }

    /**
     * 显示加载对话框
     */
    public void showProgressDialog(String msg, boolean isCancel) {
        String message;
        if (TextUtils.isEmpty(msg))
            message = "玩命加载中...";
        else
            message = msg;
        if (progressDialog == null)
            progressDialog = new LoadingDialog(getContext());
        progressDialog.setCancle(isCancel);
        progressDialog.setMsg(message);
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    /**
     * 隐藏加载对话框
     */
    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

/***************************下面都是一些监听********************************/
//    public interface LoadCompleteClick {
//        void loadComplete();
//    }
//
//    public void getWebLoadState(LoadCompleteClick loadCompleteClick) {//是否加载结束监听
//        this.loadCompleteClick = loadCompleteClick;
//    }
}
