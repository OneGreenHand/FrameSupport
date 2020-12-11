package com.ogh.support.util;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileSaveUtil {
    /**
     * 保存文本到公共目录(txt文本,其他文件同理)
     * 29 以下，需要提前申请文件读写权限
     * 29及29以上的，不需要权限
     * 保存的文件在 Download 目录下
     *
     * @param mContext 上下文
     * @param content  文本内容
     * @return 文件的 uri
     */
    public static Uri saveTextFile(Context mContext, String content) {
        if (TextUtils.isEmpty(content))
            return null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (!isGranted(mContext)) {
                LogUtils.e("FileSaveUtil", "save to file need storage permission");
                return null;
            }
            File destFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), System.currentTimeMillis() + ".txt");
            if (!save(destFile, content))
                return null;
            Uri uri = null;
            if (destFile.exists())
                uri = Uri.parse("file://" + destFile.getAbsolutePath());
            return uri;
        } else {//android Q
            Uri contentUri;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                contentUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
            } else
                contentUri = MediaStore.Downloads.INTERNAL_CONTENT_URI;
            //创建ContentValues对象，准备插入数据
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Downloads.MIME_TYPE, "text/plain");//文件格式
            contentValues.put(MediaStore.Downloads.DATE_TAKEN, System.currentTimeMillis());
            contentValues.put(MediaStore.Downloads.DISPLAY_NAME, System.currentTimeMillis());//文件名字
            Uri fileUri = mContext.getContentResolver().insert(contentUri, contentValues);
            if (fileUri == null)
                return null;
            OutputStream outputStream = null;
            try {
                outputStream = mContext.getContentResolver().openOutputStream(fileUri);
                if (outputStream != null) {
                    outputStream.write(content.getBytes());
                    outputStream.flush();
                }
                return fileUri;
            } catch (Exception e) {
                e.printStackTrace();
                mContext.getContentResolver().delete(fileUri, null, null);  // 失败的时候，删除此 uri 记录
                return null;
            } finally {
                try {
                    if (outputStream != null)
                        outputStream.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    private static boolean save(File file, String content) {
        if (!createFile(file, true)) {
            LogUtils.e("FileSaveUtil", "create or delete file <$file> failed.");
            return false;
        }
        FileOutputStream outStream = null;
        boolean ret;
        try {
            outStream = new FileOutputStream(file);
            outStream.write(content.getBytes());
            outStream.flush();
            ret = true;
        } catch (Exception e) {
            e.printStackTrace();
            ret = false;
        } finally {
            try {
                if (outStream != null)
                    outStream.close();
            } catch (IOException e) {
                // ignore
            }
        }
        return ret;
    }

    private static boolean createFile(File file, boolean isDeleteOldFile) {
        if (file == null) return false;
        if (file.exists()) {
            if (isDeleteOldFile) {
                if (!file.delete()) return false;
            } else
                return file.isFile();
        }
        if (!createDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            return false;
        }
    }

    private static boolean createDir(File file) {
        if (file == null) return false;
        if (file.exists())
            return file.isDirectory();
        else
            return file.mkdirs();
    }

    private static boolean isGranted(Context context) {
        return (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }
}