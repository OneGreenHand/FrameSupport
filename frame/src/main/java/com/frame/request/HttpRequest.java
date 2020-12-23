package com.frame.request;


import android.text.TextUtils;

import com.frame.bean.FileInfoBean;

import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class HttpRequest {
    private APIService apiService;

    public HttpRequest() {
        apiService = RetrofitWrapper.getInstance().createApi(APIService.class);
    }

    public Observable<ResponseBody> get(String url) {
        return apiService
                .get(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResponseBody> post(String url, Object object) {
        return apiService
                .post(url, object == null ? "" : object)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @param mutilFileKey 多图的key
     */
    public Observable<ResponseBody> uploadFile(String url, Map<String, Object> params, String mutilFileKey, List<FileInfoBean> fileList) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        if (params != null) {
            for (String key : params.keySet())  //添加请求参数
                builder.addFormDataPart(key, params.get(key).toString());
        }
        if (fileList != null && !fileList.isEmpty())
            if (!TextUtils.isEmpty(mutilFileKey)) {//类型: photos[0]、photos[1]、photos[2]
                int i = 0;
                for (FileInfoBean fileInfoBean : fileList) {
                    builder.addFormDataPart(mutilFileKey + "[" + i + "]", fileInfoBean.file.getName(), RequestBody.create(fileInfoBean.file, MediaType.parse("multipart/form-data")));
                    i++;
                }
            } else {//类型: pic1、pic2、pic3
                for (FileInfoBean fileInfoBean : fileList)
                    builder.addFormDataPart(fileInfoBean.paramName, fileInfoBean.file.getName(), RequestBody.create(fileInfoBean.file, MediaType.parse("multipart/form-data")));
            }
        RequestBody requestBody = builder.build();
        return apiService.upload(url, requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
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