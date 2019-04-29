package com.frame.support.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.frame.support.R;
import com.frame.support.bean.CityWeatherBean;
import com.frame.support.presenter.OnlinePt;
import com.frame.base.BaseModel;
import com.frame.base.fragment.BaseRequestFragment;
import com.frame.bean.BaseBean;
import com.frame.util.photos.IPhotoResult;
import com.frame.util.photos.TakePhotoDialog;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

public class OnlineFragment extends BaseRequestFragment<OnlinePt, BaseBean> {
    @BindView(R.id.request_msg)
    TextView requestMsg;
    TakePhotoDialog dialog;

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

    private boolean isCrio;

    @OnClick({R.id.post_request})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.post_request:
                dialog = new TakePhotoDialog(this, isCrio);
                dialog.start();
                //   mPresenter.getCityWeather();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        dialog.onResult(requestCode, resultCode, data, new IPhotoResult() {
            @Override
            public void onResult(File file, String path) {
                isCrio = true;
                Log.e("paht1", file.getPath());
                Log.e("paht2", file.getAbsolutePath());
                Log.e("paht3", path);
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
        CityWeatherBean cityWeatherBean = (CityWeatherBean) data;
        requestMsg.setText("城市：" + cityWeatherBean.data.city + "\n日期：" + cityWeatherBean.data.yesterday.date + "\n最低温度：" + cityWeatherBean.data.yesterday.low + "\n最高温度：" + cityWeatherBean.data.yesterday.high);
    }
}
