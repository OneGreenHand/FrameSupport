package com.frame.support.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.blankj.utilcode.util.ScreenUtils;
import com.frame.view.LoadingDialog;

/**
 * 基类 WebView
 * 备注：使用的时候不要设置 android:scrollbars="none"，不然部分机型会显示空白
 */
public class BaseWebView extends WebView {

    private ValueCallback<Uri[]> filePathCallback;
    public static int FILECHOOSER_RESULTCODE = 1;
    //   private LoadCompleteClick loadCompleteClick;
    //  private float screenDensity = 1f;//屏幕密度
    private boolean isShowLoading = true;//是否显示加载框
    protected LoadingDialog progressDialog;
    // private TextView titleView;

    @SuppressLint("SetJavaScriptEnabled")
    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //  getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= 19)
            getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//加载缓存否则网络
        if (Build.VERSION.SDK_INT >= 19)
            getSettings().setLoadsImagesAutomatically(true);//图片自动缩放 打开
        else
            getSettings().setLoadsImagesAutomatically(false);//图片自动缩放 关闭
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);//软件解码
        setLayerType(View.LAYER_TYPE_HARDWARE, null);//硬件解码
        //webSettings.setAllowContentAccess(true);
        //webSettings.setAllowFileAccessFromFileURLs(true);
        //webSettings.setAppCacheEnabled(true);
        getSettings().setJavaScriptEnabled(true); // 设置支持javascript脚本
        //webSettings.setPluginState(WebSettings.PluginState.ON);
        getSettings().setSupportZoom(true);// 设置可以支持缩放
        getSettings().setBuiltInZoomControls(true);// 设置出现缩放工具 是否使用WebView内置的缩放组件，由浮动在窗口上的缩放控制和手势缩放控制组成，默认false
        getSettings().setDisplayZoomControls(false);//隐藏缩放工具
        getSettings().setUseWideViewPort(true);// 扩大比例的缩放
        getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//自适应屏幕
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setDatabaseEnabled(true);//
        getSettings().setSavePassword(true);//保存密码
        getSettings().setDomStorageEnabled(true);//是否开启本地DOM存储  鉴于它的安全特性（任何人都能读取到它，尽管有相应的限制，将敏感数据存储在这里依然不是明智之举），Android 默认是关闭该功能的。
        setSaveEnabled(true);
        setKeepScreenOn(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            getSettings().setMediaPlaybackRequiresUserGesture(false);
        //其他设置
        setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                BaseWebView.this.filePathCallback = filePathCallback;
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
//        setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onReceivedTitle(WebView view, String title) {
//                if (titleView != null && !TextUtils.isEmpty(title))
//                    titleView.setText(title);
//                super.onReceivedTitle(view, title);
//            }
//        });
        setDownloadListener(new DownloadListener() {//下载事件
            @Override
            public void onDownloadStart(String paramAnonymousString1, String paramAnonymousString2, String paramAnonymousString3, String paramAnonymousString4, long paramAnonymousLong) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(paramAnonymousString1));
                getContext().startActivity(intent);
            }
        });
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
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
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (isShowLoading)
                    showProgressDialog("", true);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!getSettings().getLoadsImagesAutomatically())//不设置的话，部分机型不显示图片
                    getSettings().setLoadsImagesAutomatically(true);
                if (isShowLoading)
                    dismissProgressDialog();
                //   if (loadCompleteClick != null)
                //       loadCompleteClick.loadComplete();
            }
        });
        addJavascriptInterface(new JSInterface(getContext()), "JSInterface");
        //   screenDensity = ScreenUtils.getScreenDensity();
    }

    @Override
    public void destroy() {
        super.destroy();
    }

//    public void setTitleView(TextView title) {
//        titleView = title;
//    }

    /**
     * 是否显示加载框
     *
     * @param isShow 默认为显示
     */
    public void isShowLoading(boolean isShow) {
        isShowLoading = isShow;
    }

    /**
     * 显示加载对话框
     */
    public void showProgressDialog(Object msgType, boolean isCancel) {
        String message = "玩命加载中...";
        if (msgType == null || (msgType instanceof String && ((String) msgType).isEmpty())) {
            message = "请求中...";
        } else if (msgType instanceof String) {
            message = (String) msgType;
        } else if (msgType instanceof Integer) {
            message = getResources().getString((int) msgType);
        }
        if (progressDialog == null)
            progressDialog = new LoadingDialog(getContext());
        progressDialog.setCancle(isCancel);
        progressDialog.setMsg(message);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    /**
     * 隐藏加载对话框
     */
    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public void doFileChoose(int requestCode, int resultCode, Intent data) {
        if (filePathCallback == null)
            return;
        if (data == null || resultCode != -1 || requestCode != FILECHOOSER_RESULTCODE) {
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

    /***************************下面都是一些监听********************************/
//    private OnScrollChangeListener mOnScrollChangeListener;
//
//    @Override
//    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//        super.onScrollChanged(l, t, oldl, oldt);
//        if (mOnScrollChangeListener != null) {
//            float webcontent = getContentHeight() * getScale(); // webview的高度
//            float webnow = getHeight() / screenDensity + getScrollY() / screenDensity;  // 当前webview的高度
//            if (Math.abs(webcontent - webnow) < 1) {  //处于底端
//                mOnScrollChangeListener.onPageEnd(l, t, oldl, oldt);
//            } else if (getScrollY() == 0) {   //处于顶端
//                mOnScrollChangeListener.onPageTop(l, t, oldl, oldt);
//            } else {
//                mOnScrollChangeListener.onScrollChanged(l, t, oldl, oldt);
//            }
//        }
//    }
//
//    public void setOnScrollChangeListener(OnScrollChangeListener listener) {
//        this.mOnScrollChangeListener = listener;
//    }
//
//    public interface OnScrollChangeListener {//webview滚动监听
//
//        void onPageEnd(int l, int t, int oldl, int oldt);
//
//        void onPageTop(int l, int t, int oldl, int oldt);
//
//        void onScrollChanged(int l, int t, int oldl, int oldt);
//    }
//
//    public interface LoadCompleteClick {
//        void loadComplete();
//    }
//
//    public void getWebLoadState(LoadCompleteClick loadCompleteClick) {//是否加载结束监听
//        this.loadCompleteClick = loadCompleteClick;
//    }
}
