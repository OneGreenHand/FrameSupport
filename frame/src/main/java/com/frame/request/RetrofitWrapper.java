package com.frame.request;

import com.frame.config.AppConfig;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * date：2018/4/2 13:29
 * author: wenxy
 * description: Retofit网络请求工具类
 */
public class RetrofitWrapper {
    private static final int READ_TIMEOUT = 30;//读取超时时间,单位  秒
    private static final int CONN_TIMEOUT = 30;//连接超时时间,单位  秒

    private static volatile RetrofitWrapper instance = null;
    private Retrofit retrofit = null;

    private RetrofitWrapper() {
        // OkHttpClient okHttpClient = new OkHttpClient.Builder()
        //  构建 OkHttpClient 时,将 OkHttpClient.Builder() 传入 with() 方法,进行初始化配置( 用于Okhttp/Retofit Glide上传下载进度监听,还有动态切换baseUrl)
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                // RetrofitUrlManager.getInstance().with(new OkHttpClient.Builder())//动态切换URL
                //ProgressManager.getInstance().with(new OkHttpClient.Builder())//监听下载
                // ProgressManager.getInstance().with(RetrofitUrlManager.getInstance().with(new OkHttpClient.Builder()))(组合写法)
                .connectTimeout(CONN_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))//解决协议错误问题
                .addInterceptor(new HttpLoggingInterceptor())//此处设置的拦截器用来添加统一的请求头
                //          .addInterceptor(new ParamInterceptor())//添加公共请求参数
                .addInterceptor(new okhttp3.logging.HttpLoggingInterceptor().setLevel(okhttp3.logging.HttpLoggingInterceptor.Level.BODY))//此处设置的拦截器用来查看请求日志
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.getUrl())
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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
