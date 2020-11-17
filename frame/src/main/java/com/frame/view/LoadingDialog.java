package com.frame.view;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frame.R;
import com.frame.base.BaseDialog;


/**
 * 加载框
 */
public class LoadingDialog extends BaseDialog {
    private TextView loadingText;

    public LoadingDialog(Context context) {
        super(context, R.style.DialogStyle);
    }

    @Override
    protected void initView(Context context) {
        loadingText = findViewById(R.id.loading_text);
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
