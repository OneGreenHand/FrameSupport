package com.frame.util.photos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.blankj.utilcode.util.UriUtils;

import java.io.File;

/**
 * date：2018/4/7 11:17
 * author: wenxy
 * description: 拍照/选取本地图片/裁剪图片工具类
 */
public class PhotoUtil {
    /**
     * 拍照
     */
    public static Intent takePhoto(File file) {
        Intent intent = null;
        if (null != file) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //将拍取的照片保存到指定URI
            intent.putExtra(MediaStore.EXTRA_OUTPUT, UriUtils.file2Uri(file));
        }
        return intent;
    }

    /**
     * 选择本地图片
     */
    public static Intent pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        return intent;
    }

    /**
     * 裁剪图片
     */
    public static Intent cropPhoto(File file, File outFile) {
        Intent intent = null;
        if (null != file) {
            intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(UriUtils.file2Uri(file), "image/*");
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //是否进行裁剪
            intent.putExtra("crop", "true");
            //裁剪比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            //裁剪后大小
            //intent.putExtra("outputX", 400);
            //intent.putExtra("outputY", 200);
            //输出内容
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));
            //是否缩放
            intent.putExtra("scale", true);
            //true的话直接返回bitmap，可能会很占内存 不建议
            intent.putExtra("return-data", false);
            //图片格式
            intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
            //面部识别 这里用不上
            intent.putExtra("noFaceDetection", true);
        }
        return intent;
    }

}
