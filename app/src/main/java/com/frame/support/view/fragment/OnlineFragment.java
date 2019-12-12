package com.frame.support.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.frame.base.BaseModel;
import com.frame.base.fragment.BaseRequestFragment;
import com.frame.bean.BaseBean;
import com.frame.support.R;
import com.frame.support.presenter.OnlinePt;
import com.frame.support.util.photos.IPhotoResult;
import com.frame.support.util.photos.TakePhotoDialog;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

public class OnlineFragment extends BaseRequestFragment<OnlinePt, BaseBean> {
    @BindView(R.id.request_msg)
    TextView requestMsg;
    TakePhotoDialog photoDialog;

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_online;
    }


    @OnClick({R.id.select_photo, R.id.crop_photo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.select_photo:
                takePhoto(false);
                break;
            case R.id.crop_photo:
                takePhoto(true);
                break;
        }
    }


    public void takePhoto(boolean isCrop) {
        if (photoDialog == null)
            photoDialog = new TakePhotoDialog(this);
        photoDialog.isCrop(isCrop);
        photoDialog.takePhoto();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        photoDialog.onPhotoResult(requestCode, resultCode, data, new IPhotoResult() {
            @Override
            public void onResult(File file, String path) {
                LogUtils.e("路径", file.getPath());
                requestMsg.setText("图片路径(压缩后)："
                        + "\nPath：" + file.getPath()
                        + "\nAbsolutePath：" + file.getAbsolutePath()
                        + "\n返回的path：" + path);
            }
        });
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected OnlinePt setPresenter() {
        return new OnlinePt(this);
    }

    @Override
    protected void reRequest() {

    }


    @Override
    public void requestSuccess(BaseBean data, BaseModel.LoadMode loadMode, Object tag, int pageCount) {
    }
}
