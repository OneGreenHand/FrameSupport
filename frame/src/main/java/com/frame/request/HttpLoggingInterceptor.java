package com.frame.request;


import java.io.IOException;
import java.net.URLDecoder;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * description: 网络请求日志拦截
 */
public class HttpLoggingInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        String url = URLDecoder.decode(original.url().toString(), "UTF-8");
        //添加统一通用header，不会覆盖前面的header
        Request.Builder requestBuilder = original.newBuilder()
                .addHeader("Accept", "application/json")
                .url(url);
        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}