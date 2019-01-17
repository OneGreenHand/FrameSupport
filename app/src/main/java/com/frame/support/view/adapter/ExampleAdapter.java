package com.frame.support.view.adapter;

import com.frame.adapter.BaseQuickAdapter;
import com.frame.support.R;
import com.frame.support.bean.PersonalizedSignatureBean;
import com.frame.base.BaseQuickHolder;

public class ExampleAdapter extends BaseQuickAdapter<PersonalizedSignatureBean.DataBean, BaseQuickHolder> {

    public ExampleAdapter() {
        super(R.layout.item_common);
    }

    @Override
    protected void convert(BaseQuickHolder helper, PersonalizedSignatureBean.DataBean item) {
        helper.setText(R.id.item_title, item.femalename);
    }
}
