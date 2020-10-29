package com.frame.base;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.frame.bean.BaseBean;
import com.frame.bean.FileInfoBean;
import com.frame.config.AppConfig;
import com.frame.request.HttpRequest;
import com.frame.util.GsonUtil;
import com.frame.util.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import okhttp3.ResponseBody;

import static autodispose2.AutoDispose.autoDisposable;

/**
 * 网络层
 */
public class BaseModel {

    private final HttpRequest mHttpRequest;
    private Builder mBuilder;
    private boolean mIsEmpty;

    private BaseModel(Builder builder) {
        mBuilder = builder;
        mHttpRequest = new HttpRequest();
    }

    public void post(String api) {
        post(api, BaseBean.class);
    }

    public void get(String api) {
        get(api, BaseBean.class);
    }

    public <B extends BaseBean> void post(String api, final Class<B> clazz) {
        Observer<ResponseBody> responseBodySubscriber = getResponseBodySubscriber(api, clazz);
        if (responseBodySubscriber == null) return;
        if (mBuilder.mParam == null || mBuilder.mParam.isEmpty()) { //参数为空时，添加一个无用参数
            mBuilder.mParam = new HashMap<>();
            mBuilder.mParam.put("", "");
        }
        if (mBuilder.mBaseRequestView instanceof FragmentActivity) {//绑定生命周期
            mHttpRequest.post(api, mBuilder.mParam).to(autoDisposable(AndroidLifecycleScopeProvider.from(((FragmentActivity) mBuilder.mBaseRequestView)))).subscribe(responseBodySubscriber);
        } else if (mBuilder.mBaseRequestView instanceof Fragment) {
            mHttpRequest.post(api, mBuilder.mParam).to(autoDisposable(AndroidLifecycleScopeProvider.from(((Fragment) mBuilder.mBaseRequestView)))).subscribe(responseBodySubscriber);
        } else
            mHttpRequest.post(api, mBuilder.mParam).subscribe(responseBodySubscriber);
    }

    public <B extends BaseBean> void get(String api, final Class<B> clazz) {
        Observer<ResponseBody> responseBodySubscriber = getResponseBodySubscriber(api, clazz);
        if (responseBodySubscriber == null) return;
        if (mBuilder.mBaseRequestView instanceof FragmentActivity) {
            mHttpRequest.get(api).to(autoDisposable(AndroidLifecycleScopeProvider.from(((FragmentActivity) mBuilder.mBaseRequestView)))).subscribe(responseBodySubscriber);
        } else if (mBuilder.mBaseRequestView instanceof Fragment) {
            mHttpRequest.get(api).to(autoDisposable(AndroidLifecycleScopeProvider.from(((Fragment) mBuilder.mBaseRequestView)))).subscribe(responseBodySubscriber);
        } else
            mHttpRequest.get(api).subscribe(responseBodySubscriber);
    }

    public <B extends BaseBean> void upload(String api, final Class<B> clazz) {
        Observer<ResponseBody> responseBodySubscriber = getResponseBodySubscriber(api, clazz);
        if (responseBodySubscriber == null) return;
        if (mBuilder.mBaseRequestView instanceof FragmentActivity) {
            mHttpRequest.uploadFile(api, mBuilder.mParam, mBuilder.multiFileKey, mBuilder.mFileInfoBeans).to(autoDisposable(AndroidLifecycleScopeProvider.from(((FragmentActivity) mBuilder.mBaseRequestView)))).subscribe(responseBodySubscriber);
        } else if (mBuilder.mBaseRequestView instanceof Fragment) {
            mHttpRequest.uploadFile(api, mBuilder.mParam, mBuilder.multiFileKey, mBuilder.mFileInfoBeans).to(autoDisposable(AndroidLifecycleScopeProvider.from(((Fragment) mBuilder.mBaseRequestView)))).subscribe(responseBodySubscriber);
        } else
            mHttpRequest.uploadFile(api, mBuilder.mParam, mBuilder.multiFileKey, mBuilder.mFileInfoBeans).subscribe(responseBodySubscriber);
    }

    private <B extends BaseBean> Observer<ResponseBody> getResponseBodySubscriber(final String tag, final Class<B> clazz) {
        if (!NetworkUtils.isWifiConnected() && !NetworkUtils.isConnected()) { //检查网络是否连接
            if (mBuilder.mBaseRequestView instanceof BaseSwipeView) {
                ((BaseSwipeView) mBuilder.mBaseRequestView).resetRefreshView();
                if (mBuilder.pageIndex != AppConfig.ViewPage.START_INDEX)
                    ((BaseSwipeListView) mBuilder.mBaseRequestView).loadMoreFailView();
            }
            if (mBuilder.mLoadStyle == LoadStyle.DIALOG_VIEW || mBuilder.mLoadStyle == LoadStyle.VIEW)
                mBuilder.mBaseRequestView.showNetErrorView("");
            ToastUtil.showShortToast("请检查网络");
            return null;
        }
        Observer<ResponseBody> subscriber = new Observer<ResponseBody>() {

            @Override
            public void onError(Throwable e) {
                LogUtils.e("okhttp", "请求错误");
                refreshStatusView(e, mBuilder.requestTag == null ? tag : mBuilder.requestTag);
            }

            @Override
            public void onComplete() {
                switch (mBuilder.mLoadStyle) {
                    case NONE:
                        break;
                    case VIEW:
                        if (mIsEmpty)
                            mBuilder.mBaseRequestView.showEmptyView();
                        else
                            mBuilder.mBaseRequestView.refreshView();
                        break;
                    case DIALOG:
                        mBuilder.mBaseRequestView.dismissLoadingDialog();
                        break;
                    case DIALOG_VIEW:
                        if (mIsEmpty)
                            mBuilder.mBaseRequestView.showEmptyView();
                        else
                            mBuilder.mBaseRequestView.refreshView();
                        mBuilder.mBaseRequestView.dismissLoadingDialog();
                        break;
                }
                if (mBuilder.mBaseRequestView instanceof BaseSwipeView)
                    ((BaseSwipeView) mBuilder.mBaseRequestView).resetRefreshView();
            }

            @Override
            public void onSubscribe(Disposable d) {
                if (mBuilder.mLoadStyle == LoadStyle.NONE) {
                    return;
                } else if (mBuilder.mLoadStyle == LoadStyle.VIEW) {
                    mBuilder.mBaseRequestView.showLoadingView();
                } else
                    mBuilder.mBaseRequestView.showLoadingDialog(mBuilder.mMsg, mBuilder.isDialogCancel);
            }

            @Override
            public void onNext(ResponseBody requestBody) {
                try { //解析json
                    B bean = GsonUtil.getBean(requestBody.string(), clazz);
                    mIsEmpty = bean.isEmpty();
                    if (bean.code == 200) {
                        mBuilder.mBaseRequestView.requestSuccess(bean, mBuilder.requestTag == null ? tag : mBuilder.requestTag, mBuilder.pageIndex, mBuilder.pageCount);
                    } else {
                        mBuilder.mBaseRequestView.requestFail(bean, mBuilder.requestTag == null ? tag : mBuilder.requestTag);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (AppConfig.DEBUG) {
                        LogUtils.e("数据解析异常", e.getMessage() + "");
                        throw new RuntimeException("数据解析异常");
                    } else
                        mBuilder.mBaseRequestView.requestError(e, mBuilder.requestTag == null ? tag : mBuilder.requestTag);
                }
            }
        };
        return subscriber;
    }

    private void refreshStatusView(Throwable e, Object tag) {
        switch (mBuilder.mLoadStyle) {
            case NONE:
                break;
            case VIEW:
                mBuilder.mBaseRequestView.showNetErrorView("服务器错误: " + e.getMessage());
                break;
            case DIALOG:
                mBuilder.mBaseRequestView.dismissLoadingDialog();
                break;
            case DIALOG_VIEW:
                mBuilder.mBaseRequestView.dismissLoadingDialog();
                mBuilder.mBaseRequestView.showNetErrorView("服务器错误: " + e.getMessage());
                break;
        }
        mBuilder.mBaseRequestView.requestError(e, tag);
        if (mBuilder.mBaseRequestView instanceof BaseSwipeView) {
            ((BaseSwipeView) mBuilder.mBaseRequestView).resetRefreshView();
            if (mBuilder.pageIndex != AppConfig.ViewPage.START_INDEX)
                ((BaseSwipeListView) mBuilder.mBaseRequestView).loadMoreFailView();
        }
    }

    //构建模式--用于添加配置
    public static class Builder {
        //默认参数
        private LoadStyle mLoadStyle = LoadStyle.NONE;
        private String mMsg = null;
        private Object requestTag;
        private boolean isDialogCancel = true;//请求时dialog是否可以手动取消
        //动态参数
        private Map<String, Object> mParam;
        private BaseRequestView mBaseRequestView;
        //上传文件
        private List<FileInfoBean> mFileInfoBeans;
        private String multiFileKey;
        private int pageCount = AppConfig.ViewPage.PAGE_COUNT;//每页请求的数据量
        private int pageIndex = AppConfig.ViewPage.START_INDEX;//当前页码数

        //绑定界面就用这构造
        public Builder(BaseRequestView baseRequestView) {
            mBaseRequestView = baseRequestView;
        }

        public BaseModel create() {
            return new BaseModel(this);
        }

        //设置加载风格
        public Builder setLoadStyle(LoadStyle loadStyle) {
            mLoadStyle = loadStyle;
            return this;
        }

        //添加参数
        public Builder putParam(String key, Object value) {
            if (mParam == null)
                mParam = new HashMap<>();
            mParam.put(key, value);
            return this;
        }

        public Builder putAllParam(Map<String, Object> map) {
            if (mParam == null)
                mParam = new HashMap<>();
            mParam.putAll(map);
            return this;
        }

        //添加文件
        public Builder setFileInfoBeans(List<FileInfoBean> fileInfoBeans) {
            mFileInfoBeans = fileInfoBeans;
            return this;
        }

        //多文件上传key（与后台约定）
        public Builder setMultiFileKey(String multiFileKey) {
            this.multiFileKey = multiFileKey;
            return this;
        }

        //设置加载框文字
        public Builder setLoadMsg(String msg) {
            mMsg = msg;
            return this;
        }

        //设置请求标题(用于区分请求)
        public Builder setRequestTag(Object requestTag) {
            this.requestTag = requestTag;
            return this;
        }

        //加载弹框是否可以手动取消
        public Builder setDialogCancel(boolean dialogCancel) {
            isDialogCancel = dialogCancel;
            return this;
        }

        //设置一页请求请求数
        public Builder setPageCount(int count) {
            pageCount = count;
            return this;
        }

        //设置当前请求页数
        public Builder setPageIndex(int page) {
            pageIndex = page;
            return this;
        }

    }

    //加载样式
    public enum LoadStyle {
        NONE(1),//静默加载
        DIALOG(2),//有加载框
        VIEW(3),//有错误、网络异常等布局
        DIALOG_VIEW(4);//综上两点

        private int mValue;

        LoadStyle(int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }
    }

}