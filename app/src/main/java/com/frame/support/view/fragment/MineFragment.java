package com.frame.support.view.fragment;

import android.os.Bundle;
import android.view.View;

import com.frame.base.fragment.BaseFragment;
import com.frame.support.R;
import com.frame.support.view.activity.HeadFootExampleActivity;
import com.frame.support.view.activity.NoDataExampleActivity;
import com.frame.support.view.activity.RefreshRequestActivity;
import com.frame.util.IntentUtil;

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

    @OnClick({R.id.example_one, R.id.example_two, R.id.example_three})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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
}
