package com.frame.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.View;

import com.frame.R;
import com.frame.R2;
import com.frame.base.BaseDialog;
import com.frame.widget.dialog.LoadingView;

import butterknife.BindView;


/**
 * 加载框
 */
public class LoadingDialog extends BaseDialog {
    @BindView(R2.id.loadView)
    LoadingView loadView;

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.DialogStyle, R.style.PopWindowAnimStyle);
    }

    @Override
    protected void initView(Context context) {
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                loadView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void show() {
    //    loadView.setVisibility(View.VISIBLE);
        super.show();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.dialog_loading;
    }

    public void setMsg(String msg) {
        loadView.setLoadingText(msg);
    }

    public void setCancle(boolean isCancle) {
        setCancelable(isCancle);
        setCanceledOnTouchOutside(isCancle);
    }
}
