package com.frame.support.view.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import com.frame.support.R;
import com.frame.support.util.AlertDialog;
import com.frame.base.fragment.BaseFragment;
import com.frame.support.view.activity.HeadFootExampleActivity;
import com.frame.support.view.activity.NoDataExampleActivity;
import com.frame.support.view.activity.RefreshRequestActivity;
import com.frame.util.IntentUtil;
import com.frame.widget.dialog.LoadingView;

import butterknife.OnClick;

public class MineFragment extends BaseFragment {

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_mine;
    }

    @OnClick({R.id.loading_view, R.id.example_one, R.id.example_two, R.id.example_three})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.loading_view:
                new AlertDialog(mActivity).setLayoutId(R.layout.dialog_loading).getContentView(new AlertDialog.ContentView() {
                    @Override
                    public void initDialog(Dialog dialog) {
                        LoadingView loadingView = dialog.findViewById(R.id.loadView);
                        loadingView.setLoadingText("自定义文字");
                    }
                }).show();
                break;
            case R.id.example_one:
                IntentUtil.goActivity(mActivity, NoDataExampleActivity.class, null, false, true);
                break;
            case R.id.example_two:
                IntentUtil.goActivity(mActivity, HeadFootExampleActivity.class, null, false, true);
                break;
            case R.id.example_three:
                IntentUtil.goActivity(mActivity, RefreshRequestActivity.class, null, false, true);
                break;
        }
    }

    @Override
    public void initImmersionBar() {

    }
}
