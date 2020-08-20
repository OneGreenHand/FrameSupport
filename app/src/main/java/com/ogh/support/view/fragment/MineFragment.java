package com.ogh.support.view.fragment;

import android.os.Bundle;
import android.view.View;

import com.frame.base.fragment.BaseFragment;
import com.ogh.support.R;
import com.ogh.support.util.IntentUtil;
import com.ogh.support.view.activity.HeadFootExampleActivity;
import com.ogh.support.view.activity.NoDataExampleActivity;
import com.ogh.support.view.activity.RefreshRequestActivity;

import butterknife.OnClick;

public class MineFragment extends BaseFragment {

    @Override
    protected void init(Bundle savedInstanceState) {
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_mine;
    }

    @OnClick({R.id.example_one, R.id.example_two, R.id.example_three})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.example_one:
                IntentUtil.goActivity(mActivity, NoDataExampleActivity.class);
                break;
            case R.id.example_two:
                IntentUtil.goActivity(mActivity, HeadFootExampleActivity.class);
                break;
            case R.id.example_three:
                IntentUtil.goActivity(mActivity, RefreshRequestActivity.class);
                break;
        }
    }
}