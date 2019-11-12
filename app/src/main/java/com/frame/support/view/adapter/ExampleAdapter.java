package com.frame.support.view.adapter;

import com.frame.adapter.BaseQuickAdapter;
import com.frame.base.BaseQuickHolder;
import com.frame.support.R;
import com.frame.support.bean.DuanZiBean;

public class ExampleAdapter extends BaseQuickAdapter<DuanZiBean.ResultBean, BaseQuickHolder> {

    public ExampleAdapter() {
        super(R.layout.item_common);
    }

    @Override
    protected void convert(BaseQuickHolder helper, DuanZiBean.ResultBean item) {
        helper.setText(R.id.item_content, item.text);
    }
}
