package com.frame.util;

import android.content.Context;
import android.os.Environment;

import com.blankj.utilcode.util.CleanUtils;
import com.blankj.utilcode.util.FileUtils;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;

import java.io.File;
import java.math.BigDecimal;

public class CleanDataUtils {
    /**
     * 获取缓存大小
     */
    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = FileUtils.getDirLength(context.getCacheDir());//获取内部缓存大小
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//sd卡正常挂载
            cacheSize += FileUtils.getDirLength(context.getExternalCacheDir());//获取外部缓存大小
        }
        cacheSize += FileUtils.getDirLength(new File(context.getCacheDir() + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR));//获取glide的图片缓存大小
        return getFormatSize(cacheSize);
    }

    /**
     * 清空缓存
     */
    public static void clearAllCache() {
        CleanUtils.cleanInternalCache();//清除内部缓存
        CleanUtils.cleanExternalCache();//清除外部缓存
    }


    /**
     * 格式化单位
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
//        if (kiloByte < 1) {
//            return size + "Byte";
//        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "K";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "M";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "G";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "T";
    }

}