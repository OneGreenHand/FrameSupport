package com.frame.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * description: 解析工具类
 */
public class GsonUtil {

    private static Gson buildGson() {
        GsonBuilder gsonBulder = new GsonBuilder();
        gsonBulder.registerTypeAdapter(String.class, STRING);
        gsonBulder.serializeNulls();
        return gsonBulder.create();
    }

    /**
     * Object转成String
     */
    public static String getString(Object object) {
        Gson gson = buildGson();
        String gsonString = gson.toJson(object);
        return gsonString;
    }

    /**
     * Object转成Bean
     */
    public static <T> T getBean(Object object, Class<T> clz) {
        Gson gson = buildGson();
        T t = gson.fromJson(object.toString(), clz);
        return t;
    }

    /**
     * 指定JSONObject转成Bean
     */
    public static <T> T getBean(String key, JSONObject json, Class<T> clz) throws JSONException {
        Gson gson = buildGson();
        T t = gson.fromJson(json.getString(key), clz);
        return t;
    }

    /**
     * Object转成JSONObject
     */
    public static JSONObject getJSONObject(Object object) throws JSONException {
        Gson gson = buildGson();
        String jsonStr = gson.toJson(object);
        JSONObject jsonObject = new JSONObject(jsonStr);
        return jsonObject;
    }

    /**
     * Object转成List<T>
     */
    public static <T> List<T> getBeanList(Object object, TypeToken<List<T>> typeToken) {
        Gson gson = buildGson();
        List<T> t = gson.fromJson(object.toString(), typeToken.getType());
        return t;
    }

    /**
     * Object转成list中有map的
     */
    public static <T> List<Map<String, T>> getListMap(Object object) {
        Gson gson = buildGson();
        List<Map<String, T>> list = gson.fromJson(object.toString(), new TypeToken<List<Map<String, T>>>() {
        }.getType());
        return list;
    }

    /**
     * Object转成Map
     */
    public static <T> Map<String, T> getMap(Object object) {
        Gson gson = buildGson();
        Map<String, T> map = gson.fromJson(object.toString(), new TypeToken<Map<String, T>>() {
        }.getType());
        return map;
    }


    /***
     *
     * 获取JSON类型
     * 判断规则:判断第一个字母是否为{或[ 如果都不是则不是一个JSON格式的文本
     * @return 0不是JSON格式的字符串 2JSONObject 1JSONArray
     */
    public static int getJsonType(String str) {
        if (TextUtils.isEmpty(str))
            return 0;
        final char[] strChar = str.substring(0, 1).toCharArray();
        final char firstChar = strChar[0];
        if (firstChar == '{')
            return 2;
        else if (firstChar == '[')
            return 1;
        else
            return 0;
    }

    /**
     * 自定义TypeAdapter ,null对象将被解析成空字符串
     */
    private static final TypeAdapter<String> STRING = new TypeAdapter<String>() {
        public String read(JsonReader reader) {
            try {
                if (reader.peek() == JsonToken.NULL) {
                    reader.nextNull();
                    return ""; // 原先是返回null，这里改为返回空字符串
                }
                return reader.nextString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        public void write(JsonWriter writer, String value) {
            try {
                if (value == null) {
                    writer.value("");
                    return;
                }
                writer.value(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

}