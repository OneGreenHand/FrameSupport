package com.frame.request;


import java.io.IOException;
import java.net.URLDecoder;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * date：2018/4/2 13:29
 * description: 网络请求日志拦截
 */

public class HttpLoggingInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        String url = URLDecoder.decode(original.url().toString());
        //添加统一通用header，不会覆盖前面的header
        Request.Builder requestBuilder = original.newBuilder()
                .addHeader("Accept", "application/json")
                //.addHeader("token", InfoUtil.getTOKEN() == null ? "" : InfoUtil.getTOKEN())
              //  .addHeader("token", "5be19f81f24f7480e263ba7917d4abb1")
             //   .addHeader("Role", "Android")
                .url(url);
        Request request = requestBuilder.build();
        Response response = chain.proceed(request);
        return response;
    }


}
