package com.ogh.support.view.fragment;

import android.os.Bundle;
import android.view.View;

import com.frame.base.fragment.BaseFragment;
import com.ogh.support.databinding.FragmentMineBinding;
import com.ogh.support.util.IntentUtil;
import com.ogh.support.view.activity.HeadFootExampleActivity;
import com.ogh.support.view.activity.NoDataExampleActivity;
import com.ogh.support.view.activity.RefreshRequestActivity;

public class MineFragment extends BaseFragment<FragmentMineBinding> {

    @Override
    protected void init(Bundle savedInstanceState) {
        setViewClicked();
    }

    private void setViewClicked() {
        viewBinding.exampleOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.goActivity(mActivity, NoDataExampleActivity.class);
            }
        });
        viewBinding.exampleTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.goActivity(mActivity, HeadFootExampleActivity.class);
        }
        });
        viewBinding.exampleThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.goActivity(mActivity, RefreshRequestActivity.class);
            }
        });
    }

}