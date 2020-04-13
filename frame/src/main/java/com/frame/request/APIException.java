package com.frame.request;

import android.net.ParseException;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;


/**
 * description: 异常/错误信息
 */
public class APIException {
    /**
     * 接口返回码
     */
    public static final int SUCCESS = 200;

    /**
     * 网络请求直接抛出的异常信息
     */
    public static String getRequestExceptionMessage(Throwable e) {
        String message;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            int code = httpException.code();
            if (code == 500 || code == 404)
                message = "服务器出错";
            else if (code == 502)
                message = "无效网关";
            else
                message = "未知错误:" + code;
        } else if (e instanceof ConnectException)
            message = "服务器连接失败";
        else if (e instanceof SocketTimeoutException)
            message = "网络连接超时";
        else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException)
            message = "数据解析错误";
        else if (e instanceof javax.net.ssl.SSLHandshakeException)
            message = "证书验证失败";
        else if (e instanceof UnknownHostException)
            message = "网络连接断开";
        else
            message = "未知错误:" + e.getMessage();
        return message;
    }
}
