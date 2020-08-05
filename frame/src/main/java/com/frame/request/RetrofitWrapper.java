package com.frame.request;

import com.frame.config.AppConfig;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * description: Retofit网络请求工具类
 */
public class RetrofitWrapper {
    private static final int READ_TIMEOUT = 10;//读取超时时间,单位  秒
    private static final int CONN_TIMEOUT = 10;//连接超时时间,单位  秒

    private static volatile RetrofitWrapper instance = null;
    private Retrofit retrofit;

    private RetrofitWrapper() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(CONN_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                //.addInterceptor(new HttpLoggingInterceptor())//此处设置的拦截器用来添加统一的请求头
                //.addInterceptor(new ParamInterceptor())//添加公共请求参数
                .protocols(Collections.singletonList(Protocol.HTTP_1_1));//解决协议错误问题
        if (AppConfig.DEBUG)
            builder.addInterceptor(new okhttp3.logging.HttpLoggingInterceptor().setLevel(okhttp3.logging.HttpLoggingInterceptor.Level.BODY));//此处设置的拦截器用来查看请求日志
        retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.getUrl())
                .client(builder.build())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static RetrofitWrapper getInstance() {
        if (instance == null) {
            synchronized (RetrofitWrapper.class) {
                if (instance == null)
                    instance = new RetrofitWrapper();
            }
        }
        return instance;
    }

    public <T> T createApi(Class<T> clazz) {
        return retrofit.create(clazz);
    }

}