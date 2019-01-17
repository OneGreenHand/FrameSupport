package com.frame.request;


import android.text.TextUtils;

import com.frame.bean.FileInfoBean;

import java.io.File;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * date：2018/4/21 16:09
 * author: wenxy
 * description:
 */

public class HttpRequest {

    private APIService apiService = null;

    public HttpRequest() {
        apiService = RetrofitWrapper.getInstance().createApi(APIService.class);
    }

    public Observable<ResponseBody> get(String url, ObservableTransformer observer) {
        Observable<ResponseBody> observable = apiService
                .get(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observer == null ? observable : observable.compose(observer);
    }

    public Observable<ResponseBody> post(String url, Object object, ObservableTransformer observer) {
        Observable<ResponseBody> observable = apiService
                .post(url, object == null ? "" : object)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observer == null ? observable : observable.compose(observer);
    }

    public Observable<ResponseBody> delete(String url, Map<String, Object> fieldMap, ObservableTransformer observer) {
        Observable<ResponseBody> observable = apiService
                .delete(url, fieldMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observer == null ? observable : observable.compose(observer);
    }

    public Observable<ResponseBody> patch(String url, Map<String, Object> fieldMap, ObservableTransformer observer) {
        Observable<ResponseBody> observable = apiService
                .patch(url, fieldMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observer == null ? observable : observable.compose(observer);
    }

    /**
     * @param mutilFileKey 多图的key
     */
    public Observable<ResponseBody> uploadFile(String url, Map<String, Object> params, String mutilFileKey, List<FileInfoBean> fileList, ObservableTransformer observer) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        if (params != null) {
            for (String key : params.keySet()) { //添加请求参数
                builder.addFormDataPart(key, params.get(key).toString());
            }
        }
        if (!fileList.isEmpty())
            if (!TextUtils.isEmpty(mutilFileKey)) {//类型：photos[0]、photos[1]、photos[2]
                int i = 0;
                for (FileInfoBean fileInfoBean : fileList) {
                    builder.addFormDataPart(mutilFileKey + "[" + i + "]", fileInfoBean.getFile().getName(), RequestBody.create(MediaType.parse("multipart/form-data"), fileInfoBean.getFile()));
                    i++;
                }
            } else {//类型：pic1、pic2、pic3
                for (FileInfoBean fileInfoBean : fileList) {
                    builder.addFormDataPart(fileInfoBean.getParamName(), fileInfoBean.getFile().getName(), RequestBody.create(MediaType.parse("multipart/form-data"), fileInfoBean.getFile()));
                }
            }
        RequestBody requestBody = builder.build();
        Observable<ResponseBody> observable = apiService.upload(url, requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observer == null ? observable : observable.compose(observer);
    }

    public void download(long range, String url, String fileFolder, String fileName, Observer<ResponseBody> observer) {
        //断点续传时请求的总长度
        File file = new File(fileFolder, fileName);
        String totalLength = "-";
        if (file.exists()) {
            totalLength += file.length();
        }
        apiService.download("bytes=" + Long.toString(range) + totalLength, url)
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    /**
     * 下载文件
     */
    public void downloadFile(String url, Observer<ResponseBody> observer) {
        apiService.downloadFile(url)
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }

}
