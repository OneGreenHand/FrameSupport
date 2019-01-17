package com.frame.support.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;

import com.frame.support.R;
import com.frame.util.ToastUtil;

/**
 * @describe 通用dialog
 */
public class AlertDialog {
    private Context context;
    private int layoutId = 0;//布局内容
    private int style = R.style.ActionSheetDialogStyle;//dialog风格
    private boolean isCancel = true;//是否可以点击返回键取消
    private boolean isFocus = true;//点击外部是否可以取消
    private int gravity = Gravity.CENTER;//垂直方式
    private ContentView contentView;//得到dialog对象(操作布局资源)

    public AlertDialog(Context context) {
        this.context = context;
    }

    public AlertDialog setLayoutId(int layoutId) {
        this.layoutId = layoutId;
        return this;
    }

    public AlertDialog setStyle(int style) {
        this.style = style;
        return this;
    }

    public AlertDialog getContentView(ContentView contentView) {
        this.contentView = contentView;
        return this;
    }

    public AlertDialog isCancel(boolean isCancel) {
        this.isCancel = isCancel;
        return this;
    }

    public AlertDialog isFocus(boolean isFocus) {
        this.isFocus = isFocus;
        return this;
    }

    public AlertDialog setGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    private Dialog create() {
        Dialog dialog = new Dialog(context, style);//设置style
        dialog.setCancelable(isCancel);//弹出后会点击屏幕或物理返回键，dialog不消失
        dialog.setCanceledOnTouchOutside(isFocus);// 弹出后会点击屏幕，dialog不消失；点击物理返回键dialog消失
        dialog.getWindow().setContentView(layoutId);//设置布局
        dialog.getWindow().setGravity(gravity);//设置位置
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
        p.width = WindowManager.LayoutParams.MATCH_PARENT;
        p.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(p);
        if (contentView != null) {
            contentView.initDialog(dialog);
        }
        return dialog;
    }

    public void show() {
        if (layoutId == 0) {
            ToastUtil.showCenterToast(context, "请选设置内容布局");
            return;
        }
        create().show();
    }

    public void dimiss() {
        if (create() != null && create().isShowing()) {
            create().dismiss();
        }
    }

    public interface ContentView {
        void initDialog(Dialog dialog);
    }

}
