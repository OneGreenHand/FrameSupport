package com.ogh.support.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.frame.base.BaseQuickHolder;
import com.ogh.support.R;
import com.ogh.support.bean.DuanZiBean;

public class ExampleAdapter extends BaseQuickAdapter<DuanZiBean.ResultBean, BaseQuickHolder> implements LoadMoreModule {

    public ExampleAdapter() {
        super(R.layout.item_common);
    }

    @Override
    protected void convert(BaseQuickHolder helper, DuanZiBean.ResultBean item) {
        helper.setText(R.id.item_content, item.text);
    }
}
