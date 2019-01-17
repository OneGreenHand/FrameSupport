package com.frame.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.frame.R;
import com.frame.R2;
import com.frame.base.BaseDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 通用提示dialog
 */
public class CommonPromptDialog extends BaseDialog {

    @BindView(R2.id.title)
    TextView title;
    @BindView(R2.id.content)
    TextView content;
    @BindView(R2.id.cancel)
    TextView cancel;
    @BindView(R2.id.sure)
    TextView sure;
    @BindView(R2.id.dialog_layout)
    LinearLayout dialogLayout;
    @BindView(R2.id.line_view)
    View lineView;
    private SureCalk sureCalk;
    private CancelCalk cancelCalk;


    public CommonPromptDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void initView(Context context) {

    }

    /**
     * 是否可以点击取消
     */
    public void setCancel(boolean canCancel) {
        setCancelable(canCancel);
        setCanceledOnTouchOutside(canCancel);
        if (!canCancel) {
            cancel.setVisibility(View.GONE);
            lineView.setVisibility(View.GONE);
            sure.setBackgroundResource(R.drawable.selector_cmdialog_all);
        } else {
            cancel.setVisibility(View.VISIBLE);
            lineView.setVisibility(View.VISIBLE);
            sure.setBackgroundResource(R.drawable.selector_cmdialog_right);
        }
    }

    public void setContentText(String msg) {
        content.setText(msg);
    }

    public void setContfrimText(String msg) {
        sure.setText(msg);
    }

    public void setCancelText(String msg) {
        cancel.setText(msg);
    }

    public void setTitleText(String msg) {
        title.setText(msg);
    }

    @Override
    public void show() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) dialogLayout.getLayoutParams();
        params.width = (int) (ScreenUtils.getScreenWidth() * 0.7);//设置为屏幕的一半
        dialogLayout.setLayoutParams(params);
        content.getViewTreeObserver().addOnGlobalLayoutListener(listener);
        super.show();
    }

    private ViewTreeObserver.OnGlobalLayoutListener listener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            if (content.getLineCount() > 1) {
                content.setGravity(Gravity.LEFT);
            } else {
                content.setGravity(Gravity.CENTER);
            }
            content.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    };


    @Override
    protected int getLayoutID() {
        return R.layout.dialog_common_prompt;
    }


    public void setOnSureClick(SureCalk sureCalk) {
        this.sureCalk = sureCalk;
    }

    public void setOnCancelClick(CancelCalk sureCalk) {
        this.cancelCalk = sureCalk;
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

    public interface SureCalk {
        void OnClick(View view);
    }

    public interface CancelCalk {
        void OnClick(View view);
    }
}
