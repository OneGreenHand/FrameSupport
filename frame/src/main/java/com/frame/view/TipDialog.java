package com.frame.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.frame.R;
import com.frame.R2;
import com.frame.base.BaseDialog;
import com.frame.util.CommonUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 通用提示框
 */
public class TipDialog extends BaseDialog {
    @BindView(R2.id.title)
    TextView title;
    @BindView(R2.id.content)
    TextView content;
    @BindView(R2.id.cancel)
    TextView cancel;
    @BindView(R2.id.sure)
    TextView sure;
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

    @OnClick({R2.id.cancel, R2.id.sure})
    public void onViewClicked(View view) {
        int v = view.getId();
        if (v == R.id.cancel) {
            if (cancelCalk != null)
                cancelCalk.OnClick(view);
            dismiss();
        } else if (v == R.id.sure) {
            if (sureCalk != null)
                sureCalk.OnClick(view);
            dismiss();
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.dialog_tips;
    }
}
