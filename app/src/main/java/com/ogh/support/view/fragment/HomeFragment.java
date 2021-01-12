package com.ogh.support.view.fragment;


import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.frame.base.fragment.BaseFragment;
import com.frame.util.ToastUtil;
import com.ogh.support.config.AppConfig;
import com.ogh.support.databinding.FragmentHomeBinding;
import com.ogh.support.util.InstructionsUtils;
import com.ogh.support.view.activity.WebActivity;

public class HomeFragment extends BaseFragment<FragmentHomeBinding> {

    @Override
    protected void init(Bundle savedInstanceState) {
        setViewClicked();
    }

    private void setViewClicked() {
        viewBinding.videoWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivity.openActivity(mActivity);
            }
        });
        viewBinding.downloadApk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtils.isWifiConnected()) {
                    ToastUtil.showShortToast("请在wifi状态下下载");
                } else {
                    //   if (CommonUtil.notificationAuthority(mActivity)) {//检测通知栏是否打开,未打开类似于静默下载
                    InstructionsUtils.checkInstallOrDown(mActivity, "https://qd.myapp.com/myapp/qqteam/tim/down/tim.apk");//Tim下载地址,大概51.9Mb
                    //   }
                }
            }
        });
        viewBinding.clearDownloadApk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FileUtils.isFileExists(AppConfig.FilePath.FILE_FOLDER + "tim.apk")) {
                    if (FileUtils.delete(AppConfig.FilePath.FILE_FOLDER + "tim.apk")) {
                        ToastUtil.showShortToast("文件已删除,可以重新下载了");
                    } else
                        ToastUtil.showShortToast("文件删除失败");
                } else
                    ToastUtil.showShortToast("无需清除");
            }
        });
    }

}