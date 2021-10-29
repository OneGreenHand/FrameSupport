package com.ogh.support.view.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.frame.base.BaseDialog;
import com.ogh.support.R;
import com.ogh.support.util.CommonUtil;

/**
 * 通用提示框
 */
public class TipDialog extends BaseDialog {
    private TextView title;
    private TextView content;
    private TextView cancel;
    private TextView sure;
    private SureCalk sureCalk;
    private CancelCalk cancelCalk;

    public TipDialog(Context context) {
        super(context);
    }

    public void setOnSureClick(SureCalk sureCalk) {
        this.sureCalk = sureCalk;
    }

    public void setOnCancelClick(CancelCalk sureCalk) {
        this.cancelCalk = sureCalk;
    }

    public interface SureCalk {
        void OnClick(View view);
    }

    public interface CancelCalk {
        void OnClick(View view);
    }

    @Override
    protected void initView(Context context) {
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        cancel = findViewById(R.id.cancel);
        sure = findViewById(R.id.sure);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelCalk != null)
                    cancelCalk.OnClick(cancel);
                dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sureCalk != null)
                    sureCalk.OnClick(sure);
                dismiss();
            }
        });
    }

//    @Override
//    public void show() {
//        content.getViewTreeObserver().addOnGlobalLayoutListener(listener);
//        super.show();
//    }

    private ViewTreeObserver.OnGlobalLayoutListener listener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            if (content.getLineCount() > 1) {
                content.setGravity(Gravity.LEFT);
            } else
                content.setGravity(Gravity.CENTER);
            content.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    };

    /**
     * 是否可以点击取消
     */
    public void setCancel(boolean canCancel) {
        setCancelable(canCancel);
        setCanceledOnTouchOutside(canCancel);
        cancel.setVisibility(canCancel ? View.VISIBLE : View.GONE);
    }

    public void setTitle(String msg) {
        title.setText(msg);
    }

    public void setColorTitle(String msg) {
        title.setText(CommonUtil.setHtmlColor(msg));
    }

    public void setContent(String msg) {
        content.setText(msg);
    }

    public void setColorContent(String msg) {
        content.setText(CommonUtil.setHtmlColor(msg));
    }

    public void setSureText(String msg) {
        sure.setText(msg);
    }

    public void setSureColorText(String msg) {
        sure.setText(CommonUtil.setHtmlColor(msg));
    }

    public void setCancelText(String msg) {
        cancel.setText(msg);
    }

    public void setCancelColorText(String msg) {
        cancel.setText(CommonUtil.setHtmlColor(msg));
    }

    @Override
    protected int getLayoutID() {
        return R.layout.dialog_tips;
    }
}
