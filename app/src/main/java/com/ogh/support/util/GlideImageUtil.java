package com.ogh.support.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Looper;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.frame.R;

import java.io.File;
import java.math.BigDecimal;

/**
 * 图片处理类
 */
public class GlideImageUtil {
    /**
     * 判断宿主是否存活
     */
    private static boolean isDestroy(Context context) {
        if (context == null)
            return true;
        if ((context instanceof Activity))
            return ((Activity) context).isFinishing() || ((Activity) context).isDestroyed();
        else
            return false;
    }

    /**
     * 加载图片
     */
    public static void showImage(Context context, String url, ImageView view) {
        if (isDestroy(context))
            return;
        Glide.with(context)
                .load(url)
                .apply(getRequestOptions())
                .into(view);
    }

    /**
     * 加载图片(跳过缓存,一般是频繁更换的图片)
     */
    public static void showImageNoCache(Context context, String url, ImageView view) {
        if (isDestroy(context))
            return;
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .apply(getRequestOptions()).into(view);
    }

    /**
     * 等比例缩放图片
     */
    public static void showWrapImage(Context context, String url, ImageView view) {
        if (isDestroy(context))
            return;
        Glide.with(context)
                .asBitmap()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        view.setImageBitmap(resource);
                        //获取原图的宽高
                        int width = resource.getWidth();
                        int height = resource.getHeight();
                        //获取imageView的宽
                        int imageViewWidth = view.getWidth();
                        //计算缩放比例
                        float sy = (float) (imageViewWidth * 0.1) / (float) (width * 0.1);
                        //计算图片等比例放大后的高
                        int imageViewHeight = (int) (height * sy);
                        ViewGroup.LayoutParams params = view.getLayoutParams();
                        params.height = imageViewHeight;
                        view.setLayoutParams(params);
                    }
                });
    }

    private static RequestOptions getRequestOptions() {
        return new RequestOptions()
                .placeholder(R.mipmap.img_show_ing)
                .error(R.mipmap.img_show_error);
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
        if (isDestroy(context))
            return;
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