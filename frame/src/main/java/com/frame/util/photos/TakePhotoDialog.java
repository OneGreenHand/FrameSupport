package com.frame.util.photos;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.FileUtils;
import com.frame.FrameApplication;
import com.frame.R;
import com.frame.R2;
import com.frame.base.BaseDialog;
import com.frame.config.BaseConfig;
import com.frame.util.CommonUtil;
import com.frame.util.ToastUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;

/**
 * 拍照弹框
 */
public class TakePhotoDialog extends BaseDialog {

    @BindView(R2.id.tv_take_photo)
    TextView tvTakePhoto;
    @BindView(R2.id.tv_pick_photo)
    TextView tvPickPhoto;
    @BindView(R2.id.tv_cancle)
    TextView tvCancle;

    private FragmentActivity mActivity;
    private Fragment mFragment;
    private String imagePath = BaseConfig.PHOTO_FOLDER;//图片地址
    private String imageName = "";//文件名
    private String cropImageName = "";//裁剪后的文件名字
    private boolean isCrop;//是否裁剪
    //删除文件相关
    private String deleteImagePath = "";//需要删除的图片路径，主要是图库选择的图片
    private RxPermissions rxPermissions = null;//权限请求

    public TakePhotoDialog(@NonNull FragmentActivity activity) {
        super(activity, R.style.ActionSheetDialogStyle, Gravity.BOTTOM, true, true);
        mActivity = activity;
    }

    public TakePhotoDialog(@NonNull FragmentActivity activity, boolean isCrop) {
        super(activity, R.style.ActionSheetDialogStyle, Gravity.BOTTOM, true, true);
        mActivity = activity;
        this.isCrop = isCrop;
    }

    /**
     * 使用fragment方式的话必须要在父Activity中onActivityResult方法回调
     */
    public TakePhotoDialog(@NonNull Fragment fragment) {
        super(fragment.getActivity(), R.style.ActionSheetDialogStyle, Gravity.BOTTOM, true, true);
        mFragment = fragment;
    }

    public TakePhotoDialog(@NonNull Fragment fragment, boolean isCrop) {
        super(fragment.getActivity(), R.style.ActionSheetDialogStyle, Gravity.BOTTOM, true, true);
        mFragment = fragment;
        this.isCrop = isCrop;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.dialog_take_photo;
    }

    @OnClick({R2.id.tv_take_photo, R2.id.tv_pick_photo, R2.id.tv_cancle})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.tv_take_photo) {//拍照
            dismiss();
            if (mActivity != null)
                mActivity.startActivityForResult(PhotoUtil.takePhoto(FileUtils.getFileByPath(imagePath + imageName)), 0x001);
            else
                mFragment.startActivityForResult(PhotoUtil.takePhoto(FileUtils.getFileByPath(imagePath + imageName)), 0x001);
        } else if (view.getId() == R.id.tv_pick_photo) {//图库
            dismiss();
            if (mActivity != null)
                mActivity.startActivityForResult(PhotoUtil.pickPhoto(), 0x002);
            else
                mFragment.startActivityForResult(PhotoUtil.pickPhoto(), 0x002);
        } else if (view.getId() == R.id.tv_cancle) {
            dismiss();
        }
    }

    /**
     * 申请权限并进行拍照
     */
    @SuppressLint("CheckResult")
    public void takePhoto() {
        if (mActivity != null) {
            if (rxPermissions == null)
                rxPermissions = new RxPermissions(mActivity);
        } else {
            if (rxPermissions == null)
                rxPermissions = new RxPermissions(mFragment);
        }
        rxPermissions.requestEachCombined(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .subscribe(permission -> {
                    if (permission.granted) {//权限申请成功
                        imageName = System.currentTimeMillis() + ".png";//设置图片名字
                        cropImageName = "crop_" + imageName;//设置裁剪后的图片名字
                        if (FileUtils.createOrExistsDir(imagePath))  //创建photo目录
                            show();
                        else
                            ToastUtil.showShortToast("出现未知错误，请稍候重试~");
                    } else if (permission.shouldShowRequestPermissionRationale) {//拒绝申请权限
                        ToastUtil.showShortToast("您拒绝了应用权限，该功能暂时无法使用~");
                    } else {//不在提醒申请权限
                        if (mActivity != null)
                            CommonUtil.getPermissions(mActivity, null);
                        else
                            CommonUtil.getPermissions(mFragment.getActivity(), null);
                    }
                });
    }

    /**
     * 拍照回调
     */
    public void onPhotoResult(int requestCode, int resultCode, Intent data, IPhotoResult iPhotoResult) {
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
            FileUtils.deleteFile(imagePath + imageName);//删除拍照后的源文件
        } else {//删除选择的图片相关创建的文件
            FileUtils.deleteFile(deleteImagePath);//删除图库选择的源文件
        }
        deleteImagePath = "";
        compressTheImg(imagePath + cropImageName, iPhotoResult);
    }

    /**
     * 压缩图片(luban)
     */
    @SuppressLint("CheckResult")
    private void compressTheImg(String path, IPhotoResult iPhotoResult) {
        Flowable.just(path)
                .subscribeOn(Schedulers.io())
                .map(new Function<String, File>() {
                    @Override
                    public File apply(@NonNull String str) throws Exception {
                        return Luban.with(mActivity == null ? mFragment.getActivity() : mActivity)
                                .load(str)
                                .ignoreBy(100)//设置忽略的图片大小，这里为100KB时不压缩
                                .setTargetDir(imagePath)//设置压缩后存放的地址
                                .filter(new CompressionPredicate() {
                                    @Override
                                    public boolean apply(String path) {
                                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                                    }
                                }).get(str);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File files) throws Exception {
                        if (files.exists()) {//压缩成功返回压缩后的文件和文件路径
                            if (iPhotoResult != null) {
                                iPhotoResult.onResult(files, files.getPath());
                                FileUtils.deleteFile(path);//删除压缩前的文件
                            }
                        } else {//压缩失败直接返回源文件和路径
                            if (iPhotoResult != null)
                                iPhotoResult.onResult(new File(path), path);
                        }
                    }
                });
//        try {
//            File file2 = new Compressor(mContext).compressToFile(file, "cp" + System.currentTimeMillis() + ".png");
//            if (file2.exists()) {//如果压缩成功
//                if (iPhotoResult != null)
//                    iPhotoResult.onResult(file2, path);
//            } else {
//                if (iPhotoResult != null)
//                    iPhotoResult.onResult(file2, path);
//            }
//        } catch (IOException e) {
//            if (iPhotoResult != null)
//                iPhotoResult.onResult(file, path);
//        }
    }

    /**
     * 获取文件路径
     */
    private String getPathByUri(Uri uri) {
        if (uri == null)
            return null;
        CursorLoader cl = new CursorLoader(FrameApplication.mContext == null ? mActivity == null ? mFragment.getActivity() : mActivity : FrameApplication.mContext);
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
            CrashReport.postCatchedException(e);//手动上报异常到bugly
            return "";
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

}
