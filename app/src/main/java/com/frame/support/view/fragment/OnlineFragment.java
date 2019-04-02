package com.frame.support.view.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.frame.support.R;
import com.frame.support.bean.CityWeatherBean;
import com.frame.support.presenter.OnlinePt;
import com.frame.base.BaseModel;
import com.frame.base.fragment.BaseRequestFragment;
import com.frame.bean.BaseBean;

import butterknife.BindView;
import butterknife.OnClick;

public class OnlineFragment extends BaseRequestFragment<OnlinePt, BaseBean> {
    @BindView(R.id.request_msg)
    TextView requestMsg;

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

    @OnClick({R.id.post_request})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.post_request:
                mPresenter.getCityWeather();
                break;
        }
    }

    @Override
    protected OnlinePt setPresenter() {
        return new OnlinePt(this);
    }

    @Override
    protected void reRequest() {

    }


    @Override
    public void requestSuccess(BaseBean data, BaseModel.LoadMode loadMode, Object tag,int pageCount) {
        CityWeatherBean cityWeatherBean = (CityWeatherBean) data;
        requestMsg.setText("城市：" + cityWeatherBean.data.city + "\n日期：" + cityWeatherBean.data.yesterday.date + "\n最低温度：" + cityWeatherBean.data.yesterday.low + "\n最高温度：" + cityWeatherBean.data.yesterday.high);
    }
}
