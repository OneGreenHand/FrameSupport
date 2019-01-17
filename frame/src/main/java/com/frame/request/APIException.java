package com.frame.request;

import android.net.ParseException;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;


/**
 * date：2018/4/2 13:29
 * author: wenxy
 * description: 异常/错误信息
 */
public class APIException {
    /**
     * 接口返回码
     */
    public static final int SUCCESS = 200;
    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据接口返回的错误码对错误信息进行一个转换，再显示给用户
     *
     * @param code
     * @return
     */
    public static String getApiExceptionMessage(int code) {
        String message = "";
        switch (code) {
            case SUCCESS:
                message = "请求成功";
                break;
        }
        return message;
    }

    /**
     * 网络请求直接抛出的异常信息
     *
     * @param e
     * @return
     */
    public static String getRequestExceptionMessage(Throwable e){
        String message = "";
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            //httpException.response().errorBody().string()
            int code = httpException.code();
            if (code == 500 || code == 404) {
                message = "服务器出错";
            }else if (code == 502){
                message = "无效网关";
            }else{
                message = "未知错误:"+code;
            }
        } else if (e instanceof ConnectException) {
            message = "服务器连接失败";
        } else if (e instanceof SocketTimeoutException) {
            message = "网络连接超时";
        } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException){
            message = "数据解析错误";
        }else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            message = "证书验证失败";
        }else if (e instanceof UnknownHostException){
            message = "网络连接断开";
        }else {
            message = "未知错误:"+e.getMessage();
        }
        return message;
    }
}
