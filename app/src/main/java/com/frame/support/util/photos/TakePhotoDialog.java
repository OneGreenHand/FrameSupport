package com.frame.support.util.photos;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.frame.base.BaseDialog;
import com.frame.config.BaseConfig;
import com.frame.support.R;
import com.frame.support.util.compress.Compressor;
import com.frame.util.ToastUtil;

import java.io.File;
import java.io.IOException;

import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;
import butterknife.OnClick;

/**
 * 拍照弹框
 */
public class TakePhotoDialog extends BaseDialog {

    private Activity mActivity;
    private Fragment mFragment;
    private String imagePath = BaseConfig.PHOTO_FOLDER;//图片地址
    private String imageName = "";//文件名
    private String cropImageName = "";//裁剪后的文件名字
    private boolean isCrop;//是否裁剪
    //删除文件相关
    private String deleteImagePath = "";//需要删除的图片路径，主要是图库选择的图片

    public TakePhotoDialog(Activity activity) {
        super(activity, Gravity.BOTTOM, true);
        mActivity = activity;
    }

    public TakePhotoDialog(Activity activity, boolean isCrop) {
        super(activity, Gravity.BOTTOM, true);
        mActivity = activity;
        this.isCrop = isCrop;
    }

    /**
     * 使用fragment方式的话必须要在父Activity中onActivityResult方法回调
     */
    public TakePhotoDialog(Fragment fragment) {
        super(fragment.getActivity(), Gravity.BOTTOM, true);
        mFragment = fragment;
    }

    public TakePhotoDialog(Fragment fragment, boolean isCrop) {
        super(fragment.getActivity(), Gravity.BOTTOM, true);
        mFragment = fragment;
        this.isCrop = isCrop;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.dialog_take_photo;
    }

    @OnClick({R.id.tv_take_photo, R.id.tv_pick_photo, R.id.tv_cancle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_take_photo://拍照
                dismiss();
                if (mActivity != null)
                    mActivity.startActivityForResult(PhotoUtil.takePhoto(FileUtils.getFileByPath(imagePath + imageName)), 0x001);
                else
                    mFragment.startActivityForResult(PhotoUtil.takePhoto(FileUtils.getFileByPath(imagePath + imageName)), 0x001);
                break;
            case R.id.tv_pick_photo://图库
                dismiss();
                if (mActivity != null)
                    mActivity.startActivityForResult(PhotoUtil.pickPhoto(), 0x002);
                else
                    mFragment.startActivityForResult(PhotoUtil.pickPhoto(), 0x002);
                break;
            case R.id.tv_cancle:
                dismiss();
                break;
        }
    }

    /**
     * 申请权限并进行拍照
     */
    public void takePhoto() {
        PermissionUtils.permission(PermissionConstants.STORAGE, PermissionConstants.CAMERA)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        imageName = System.currentTimeMillis() + ".png";//设置图片名字
                        cropImageName = "crop_" + imageName;//设置裁剪后的图片名字
                        if (FileUtils.createOrExistsDir(imagePath))  //创建photo目录
                            show();
                        else
                            ToastUtil.showShortToast("出现错误,请稍候重试");
                    }

                    @Override
                    public void onDenied() {
                        ToastUtil.showShortToast("未获取权限,该功能暂时无法使用");
                    }
                })
                .request();
    }

    /**
     * 拍照回调
     */
    public void onPhotoResult(int requestCode, int resultCode, Intent data, IPhotoResult
            iPhotoResult) {
        //拍照和选择图片结果回调
        if (resultCode != 0) {
            if (requestCode == 0x001) // 拍照返回
                takePhotoResult(iPhotoResult);
            else if (requestCode == 0x002) // 选择本地图片返回
                pickPhotoResult(data, iPhotoResult);
            else if (requestCode == 0x003) //裁剪图片后
                cropPhotoResult(iPhotoResult);
        }
    }

    /**
     * 拍照返回后的逻辑操作
     */
    private void takePhotoResult(IPhotoResult iPhotoResult) {
        if (isCrop) {
            deleteImagePath = "";
            File file = FileUtils.getFileByPath(imagePath + imageName);//源文件
            File outFile = FileUtils.getFileByPath(imagePath + cropImageName);//裁剪后的文件
            if (mActivity != null)
                mActivity.startActivityForResult(PhotoUtil.cropPhoto(file, outFile), 0x003);
            else
                mFragment.startActivityForResult(PhotoUtil.cropPhoto(file, outFile), 0x003);
        } else {
            compressTheImg(imagePath + imageName, iPhotoResult);
        }
    }

    /**
     * 选择本地图片返回后的逻辑操作
     */
    private void pickPhotoResult(Intent data, IPhotoResult iPhotoResult) {
        if (null != data) {//为了取消选取不报空指针用的
            Uri imageUri = data.getData();
            if (isCrop) {
                deleteImagePath = getPathByUri(imageUri);
                File outFile = FileUtils.getFileByPath(imagePath + cropImageName);//裁剪后的文件
                if (mActivity != null)
                    mActivity.startActivityForResult(PhotoUtil.cropPhoto(new File(getPathByUri(imageUri)), outFile), 0x003);
                else
                    mFragment.startActivityForResult(PhotoUtil.cropPhoto(new File(getPathByUri(imageUri)), outFile), 0x003);
            } else {
                compressTheImg(getPathByUri(imageUri), iPhotoResult);
            }
        }
    }

    public boolean isCrop() {
        return isCrop;
    }

    public void isCrop(boolean isCrop) {
        this.isCrop = isCrop;
    }

    /**
     * 裁剪图片返回后的逻辑操作
     */
    private void cropPhotoResult(IPhotoResult iPhotoResult) {
        if (TextUtils.isEmpty(deleteImagePath)) {//删除拍照相关创建的文件
            FileUtils.delete(imagePath + imageName);//删除拍照后的源文件
        } else {//删除选择的图片相关创建的文件
            FileUtils.delete(deleteImagePath);//删除图库选择的源文件
        }
        deleteImagePath = "";
        compressTheImg(imagePath + cropImageName, iPhotoResult);
    }

    /**
     * 压缩图片
     */
    @SuppressLint("CheckResult")
    private void compressTheImg(String path, IPhotoResult iPhotoResult) {
        try {
            File file2 = new Compressor(mActivity == null ? mFragment.getActivity() : mActivity).compressToFile(new File(path), "cp" + System.currentTimeMillis() + ".png");
            if (file2.exists()) {//如果压缩成功
                if (iPhotoResult != null)
                    iPhotoResult.onResult(file2, path);
            } else {
                if (iPhotoResult != null)
                    iPhotoResult.onResult(file2, path);
            }
        } catch (IOException e) {
            if (iPhotoResult != null)
                iPhotoResult.onResult(new File(path), path);
        }
    }

    /**
     * 获取文件路径
     */
    private String getPathByUri(Uri uri) {
        if (uri == null)
            return null;
        CursorLoader cl = new CursorLoader(mActivity == null ? mFragment.getActivity() : mActivity);
        cl.setUri(uri);
        cl.setProjection(new String[]{MediaStore.Audio.Media.DATA});
        Cursor cursor = null;
        try {
            cursor = cl.loadInBackground();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(columnIndex);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

}