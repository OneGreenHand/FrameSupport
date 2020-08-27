package com.ogh.support.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.frame.base.activity.BaseActivity;
import com.frame.util.CommonUtil;
import com.frame.util.ToastUtil;
import com.ogh.support.R;
import com.ogh.support.widget.zhuanpan.LuckPanLayout;
import com.ogh.support.widget.zhuanpan.RotatePan;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ZhuanPanActivity extends BaseActivity {
    @BindView(R.id.luckpan_layout)
    LuckPanLayout luckPanLayout;
    @BindView(R.id.rotatePan)
    RotatePan rotatePan;

    public static void openActivity(Context context) {
        context.startActivity(new Intent(context, ZhuanPanActivity.class));
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        //    rotatePan.setDatas();//设置转盘数量,默认是8个
        luckPanLayout = (LuckPanLayout) findViewById(R.id.luckpan_layout);
        luckPanLayout.setAnimationEndListener(new LuckPanLayout.AnimationEndListener() {
            @Override
            public void endAnimation(int position) {
                ToastUtil.showShortToast("Position = " + position + "," + rotatePan.getDatas().get(position));
            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_zhuan_pan;
    }


    @OnClick({R.id.go, R.id.github})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.go:
                luckPanLayout.rotate(-1, 100);
                break;
            case R.id.github:
                CommonUtil.intentToBrowsable(mContext, "https://github.com/Nipuream/LuckPan");
                break;
        }
    }
}
