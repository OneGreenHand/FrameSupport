package com.frame.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.frame.R;

import java.io.File;
import java.math.BigDecimal;

import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.MaskTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * 图片处理类
 */
public class GlideImageUtil {

    /**
     * 显示图片
     */
    public static void showImage(Context context, String url, ImageView view) {
        Glide.with(context).load(url)//图片地址
                .apply(new RequestOptions().placeholder(R.drawable.img_showing)
                        .error(R.drawable.img_show_error))
                //  .override(600,600)//指定尺寸(图片大小在xml中不能写死,是wrap_content才可以指定尺寸)
                .into(view);
    }


    /**
     * 将图片显示在指定的图片形状中
     *
     * @param drawable 要显示的图片形状
     */
    public static void showMaskImage(final Context context, String url, int drawable, ImageView view) {
        Glide.with(context)
                .load(url)
                .apply(RequestOptions.bitmapTransform(new MultiTransformation<Bitmap>(new CenterCrop(), new MaskTransformation(drawable)))
                        .placeholder(R.drawable.img_showing)
                        .error(R.drawable.img_show_error))
                .into(view);
    }

    /**
     * 圆形、圆圈
     */
    public static void showCircleImage(final Context context, String url, ImageView view) {
        Glide.with(context)
                .load(url)
                .apply(RequestOptions.bitmapTransform(new CropCircleTransformation())
                        .placeholder(R.drawable.img_showing)
                        .error(R.drawable.img_show_error))
                .into(view);
    }

    /**
     * 圆角
     *
     * @param radius     圆角大小
     * @param cornerType 显示的方式，例如 RoundedCornersTransformation.CornerType.ALL
     */
    public static void showRoundedCornersImage(final Context context, String url, ImageView view, int radius, RoundedCornersTransformation.CornerType cornerType) {
        Glide.with(context)
                .load(url)
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(radius, 0,
                        cornerType))
                        .placeholder(R.drawable.img_showing)
                        .error(R.drawable.img_show_error))
                .into(view);
    }

    /**
     * 方形
     */
    public static void showSquareImage(final Context context, String url, ImageView view) {
        Glide.with(context)
                .load(url)
                .apply(RequestOptions.bitmapTransform(new CropSquareTransformation())
                        .placeholder(R.drawable.img_showing)
                        .error(R.drawable.img_show_error))
                .into(view);
    }

    /**
     * 加遮罩颜色
     */
    public static void showGrayImage(final Context context, String url, int color, ImageView view) {
        Glide.with(context)
                .load(url)
                .apply(RequestOptions.bitmapTransform(new ColorFilterTransformation(color))
                        .placeholder(R.drawable.img_showing)
                        .error(R.drawable.img_show_error))
                .into(view);
    }

    /**
     * 加载GIF图片
     */
    public static void showGisImage(Context context, String url, ImageView view) {
        Glide.with(context)
                .asGif()
                .load(url)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.img_showing)
                        .error(R.drawable.img_show_error))
                .into(view);
    }

    /**
     * 加载本地资源
     */
    public static void showLocalImage(Context context, boolean isGif, int url, ImageView view) {
        if (isGif) {
            Glide.with(context)
                    .asGif()
                    .load(url)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.img_showing)
                            .error(R.drawable.img_show_error))
                    .into(view);
        } else {
            Glide.with(context)
                    .load(url)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.img_showing)
                            .error(R.drawable.img_show_error))
                    .into(view);
        }
    }

    /**
     * 取消和恢复请求(一般用于listview高速滑动中)
     * 0:滑动中1:停止滑动
     * 当列表在滑动的时候，调用pauseRequests()取消请求，滑动停止时，调用resumeRequests()恢复请求。
     */
    public static void suspendImage(Context context, int type) {
        if (type == 0)
            Glide.with(context).pauseRequests();
        else
            Glide.with(context).resumeRequests();
    }

    /**
     * 清除图片磁盘缓存
     */
    public static void clearImageDiskCache(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片内存缓存
     */
    public static void clearImageMemoryCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) //只能在主线程执行
                Glide.get(context).clearMemory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片所有缓存
     */
    public static void clearImageAllCache(Context context) {
        clearImageDiskCache(context);
        clearImageMemoryCache(context);
        String ImageExternalCatchDir = context.getExternalCacheDir() + ExternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR;
        deleteFolderFile(ImageExternalCatchDir, true);
    }

    /**
     * 获取Glide造成的缓存大小
     */
    public static String getCacheSize(Context context) {
        try {
            return getFormatSize(getFolderSize(new File(context.getCacheDir() + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory())
                    size = size + getFolderSize(aFileList);
                else
                    size = size + aFileList.length();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 删除指定目录下的文件，这里用于缓存的删除
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (File file1 : files) {
                        deleteFolderFile(file1.getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {
                        file.delete();
                    } else {
                        if (file.listFiles().length == 0) {
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 格式化单位
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

}
