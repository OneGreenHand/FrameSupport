package com.frame.request;

/**
 * Created by ${luo} on 2018/4/9.
 */

public interface DownloadCallBack<T> {
    void onStart();//请求开始

    void onProgress(int progress);

    void onSuccess(T t);//成功了就回调这个方法,可以传递任何形式的数据给调用者

    void end();

    void onError(String msg);
}