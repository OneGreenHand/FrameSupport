package com.frame.loadingView;


import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.frame.R;

public class VaryViewHelperController {

    private IVaryViewHelper helper;
    private int emptyView;

    public VaryViewHelperController(View view, int emptyView) {
        this(new VaryViewHelper(view));
        this.emptyView = emptyView;
    }

    private VaryViewHelperController(IVaryViewHelper helper) {
        super();
        this.helper = helper;
    }

    public void showNetworkError(View.OnClickListener onClickListener, String tips) {
        View layout = helper.inflate(R.layout.frame_view_pager_error);
        if (TextUtils.isEmpty(tips))
            ((TextView) layout.findViewById(R.id.v_tips)).setText(R.string.frame_view_net_error);
        else
            ((TextView) layout.findViewById(R.id.v_tips)).setText(tips);
        TextView againBtn = layout.findViewById(R.id.tv_view_pager_error_load);
        if (null != onClickListener)
            againBtn.setOnClickListener(onClickListener);
        helper.showLayout(layout);
    }

    public void showEmpty(String emptyMsg) {
        View layout = helper.inflate(emptyView);
        TextView textView = layout.findViewById(R.id.tv_view_pager_no_data_content);
        if (!TextUtils.isEmpty(emptyMsg))
            textView.setText(emptyMsg);
        helper.showLayout(layout);
    }

    public void showLoading() {
        View layout = helper.inflate(R.layout.frame_view_pager_loading);
        helper.showLayout(layout);
    }

    public void restore() {
        helper.restoreView();
    }
}
