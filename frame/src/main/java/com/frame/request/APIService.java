package com.frame.request;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * description: API接口
 */
public interface APIService {
    //下载文件
    @Streaming//防止写入内存
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);

    @GET
    Observable<ResponseBody> get(@Url() String url);

    @POST
    Observable<ResponseBody> post(@Url() String url, @Body Object object);

    @POST
    Observable<ResponseBody> upload(@Url() String url, @Body RequestBody Body);
}
