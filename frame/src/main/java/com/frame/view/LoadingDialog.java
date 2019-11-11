package com.frame.view;

import android.content.Context;
import androidx.annotation.NonNull;
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

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.DialogStyle, R.style.PopWindowAnimStyle);
    }

    @Override
    protected void initView(Context context) {
//        setOnDismissListener(new OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//            }
//        });
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.dialog_loading;
    }

    public void setMsg(String msg) {
        loadingText.setText(msg);
    }

    public void setCancle(boolean isCancle) {
        setCancelable(isCancle);
        setCanceledOnTouchOutside(isCancle);
    }
}
