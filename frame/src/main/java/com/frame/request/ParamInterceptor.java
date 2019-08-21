package com.frame.request;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * @data on 2018/8/18 10:40
 * @describe 添加公共请求参数(拦截器)
 */
public class ParamInterceptor implements Interceptor {
    Gson mGson = new Gson();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = null;
        Request original = chain.request();
        if (original.method().equals("GET")) { // GET 请求
            HttpUrl url = original.url().newBuilder() //请求尾部链接
                    .addQueryParameter("apppwd", "d28feb4ca50b2da463331a2d32")//添加参数
                    .build();
            request = original.newBuilder()
                    .method(original.method(), original.body())
                    .url(url)  //添加到请求里
                    .build();
        } else if (chain.request().method().equals("POST")) { // POST 请求
            request = chain.request();
            if (request.body() instanceof FormBody) {
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                FormBody formBody = (FormBody) request.body();
                //把原来的参数添加到新的构造器，（因为没找到直接添加，所以就new新的）
                for (int i = 0; i < formBody.size(); i++) {
                    bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
                }
                formBody = bodyBuilder
                        .addEncoded("apppwd", "d28feb4ca50b2da463331a2d32")//添加参数
                        .build();
                request = request.newBuilder().post(formBody).build();
            } else if (request.body() instanceof MultipartBody) {
                //   request = chain.request();
                MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                List<MultipartBody.Part> oldParts = ((MultipartBody) request.body()).parts();
                if (oldParts != null && oldParts.size() > 0) {
                    for (MultipartBody.Part part : oldParts) {
                        multipartBuilder.addPart(part);
                    }
                }
                multipartBuilder.addFormDataPart("apppwd", "d28feb4ca50b2da463331a2d32");
                request = request.newBuilder().post(multipartBuilder.build()).build();
            } else {
                RequestBody requestBody = request.body();
                //buffer流
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                String oldParamsJson = buffer.readUtf8();
                if (oldParamsJson.equals("\"\"")) oldParamsJson = "";
                HashMap rootMap = mGson.fromJson(oldParamsJson, HashMap.class);  //原始参数
                if (rootMap == null) rootMap = new HashMap();
                rootMap.put("apppwd", "d28feb4ca50b2da463331a2d32");  //重新组装（添加参数）
                String newJsonParams = mGson.toJson(rootMap);  //装换成json字符串
                request = request.newBuilder().post(RequestBody.create(MediaType.parse("application/json"), newJsonParams)).build();
            }
        }
        return chain.proceed(request);
    }
}