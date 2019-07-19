package com.frame.request;

import android.support.v4.util.ArrayMap;

import java.util.Set;

import io.reactivex.disposables.Disposable;

/**
 * date：2018/7/5 18:14
 * description: 用于取消网络请求(rxlifecycle已替代)
 */
public class RxAPIManager {

    private static RxAPIManager sInstance = null;
    private ArrayMap<Object, Disposable> maps;

    public static RxAPIManager get() {
        if (sInstance == null) {
            synchronized (RxAPIManager.class) {
                if (sInstance == null)
                    sInstance = new RxAPIManager();
            }
        }
        return sInstance;
    }

    private RxAPIManager() {
        maps = new ArrayMap<>();
    }

    public void add(Object tag, Disposable d) {
        maps.put(tag, d);
    }

    public void cancel(Object tag) {
        if (maps.isEmpty())
            return;
        if (maps.get(tag) == null)
            return;
        if (!maps.get(tag).isDisposed()) {
            maps.get(tag).dispose();
            maps.remove(tag);
        }
    }

    public void cancelAll() {
        if (maps.isEmpty())
            return;
        Set<Object> keys = maps.keySet();
        for (Object apiKey : keys) {
            cancel(apiKey);
        }
    }
}
