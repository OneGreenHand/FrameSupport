package com.frame.request;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * date：2018/4/2 13:29
 * description: API接口
 */
public interface APIService {

    //下载文件
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);

    @GET
    Observable<ResponseBody> get(@Url() String url);

    @POST
    Observable<ResponseBody> post(@Url() String url, @Body Object object);

    @POST
    Observable<ResponseBody> upload(@Url() String url, @Body RequestBody Body);

    @Streaming//防止写入内存
    @GET
    Observable<ResponseBody> download(@Header("Range") String range, @Url() String url);

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @FormUrlEncoded
    @PATCH
    Observable<ResponseBody> patch(@Url() String url, @FieldMap Map<String, Object> fieldMap);

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @FormUrlEncoded
    @HTTP(method = "DELETE", hasBody = true)
    Observable<ResponseBody> delete(@Url() String url, @FieldMap Map<String, Object> fieldMap);

}
