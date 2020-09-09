package com.frame.view;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frame.R;
import com.frame.R2;
import com.frame.base.BaseDialog;

import butterknife.BindView;


/**
 * 加载框
 */
public class LoadingDialog extends BaseDialog {
    @BindView(R2.id.loading_text)
    TextView loadingText;

    public LoadingDialog(Context context) {
        super(context, R.style.DialogStyle);
    }

    @Override
    protected void initView(Context context) {
    }

    @Override
    protected int getLayoutID() {
        return R.layout.dialog_loading;
    }

    public void setMsg(String msg) {
        loadingText.setText(msg);
    }

    @Override
    protected int getWidth() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    public void setCancel(boolean isCancel) {
        setCancelable(isCancel);
        setCanceledOnTouchOutside(isCancel);
    }
}
