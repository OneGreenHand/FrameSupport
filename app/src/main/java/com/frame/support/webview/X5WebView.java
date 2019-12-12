package com.frame.support.webview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.frame.view.LoadingDialog;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * 基类 WebView(X5)
 * 备注：使用的时候不要设置 android:scrollbars="none"，不然部分机型会显示空白
 */
public class X5WebView extends com.tencent.smtt.sdk.WebView {

    private boolean isShowLoading = true;//是否显示加载框
    protected LoadingDialog progressDialog;

    //private LoadCompleteClick loadCompleteClick;
    public X5WebView(Context context) {
        super(context);
        init(context);
    }

    public X5WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public X5WebView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        getSettings().setCacheMode(com.tencent.smtt.sdk.WebSettings.LOAD_NO_CACHE);//不缓存
        getSettings().setLoadsImagesAutomatically(Build.VERSION.SDK_INT >= 19);//图片自动缩放 打开
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
        setDownloadListener(new com.tencent.smtt.sdk.DownloadListener() {//下载事件
            @Override
            public void onDownloadStart(String s, String s1, String s2, String s3, long l) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(s));
                getContext().startActivity(intent);
            }
        });
        setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(context);
                b.setTitle("提示");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
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
                    showProgressDialog();
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
    public void showProgressDialog() {
        if (progressDialog == null)
            progressDialog = new LoadingDialog(getContext());
        progressDialog.setMsg("玩命加载中...");
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
