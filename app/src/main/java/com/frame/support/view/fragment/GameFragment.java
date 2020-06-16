package com.frame.support.view.fragment;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.frame.base.fragment.BaseFragment;
import com.frame.config.BaseConfig;
import com.frame.support.R;
import com.frame.support.util.InstructionsUtils;
import com.frame.support.view.activity.WebActivity;
import com.frame.util.ToastUtil;

import butterknife.OnClick;

public class GameFragment extends BaseFragment {
    private String downloadUrl = "http://sqdd.myapp.com/myapp/qqteam/tim/down/tim.apk";//Tim下载地址,大概51.9Mb

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_game;
    }

    @OnClick({R.id.video_web, R.id.download_apk, R.id.clear_download_apk})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.video_web:
                WebActivity.openActivity(mActivity);
                break;
            case R.id.download_apk:
                if (!NetworkUtils.isWifiConnected()) {
                    ToastUtil.showShortToast("请在wifi状态下下载");
                } else {
                    //   if (CommonUtil.notificationAuthority(mActivity)) {//检测通知栏是否打开,未打开类似于静默下载
                    InstructionsUtils.checkInstallOrDown(mActivity, downloadUrl);
                    //   }
                }
                break;
            case R.id.clear_download_apk:
                if (FileUtils.isFileExists(BaseConfig.FILE_FOLDER + "tim.apk")) {
                    if (FileUtils.delete(BaseConfig.FILE_FOLDER + "tim.apk")) {
                        ToastUtil.showShortToast("文件已删除,可以重新下载了");
                    } else {
                        ToastUtil.showShortToast("文件删除失败");
                    }
                } else {
                    ToastUtil.showShortToast("无需清除");
                }
                break;
        }
    }
}