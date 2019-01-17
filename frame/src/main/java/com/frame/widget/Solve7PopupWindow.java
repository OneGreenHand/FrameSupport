package com.frame.widget;

import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.widget.PopupWindow;

/**
 * @author OneGreenHand
 * @package com.goudiw.www.goudiwapp.widget
 * @fileName Solve7PopupWindow
 * @data on 2018/9/25 17:00
 * @describe 解决部分手机showAsDropDown位置不对问题
 */
public class Solve7PopupWindow extends PopupWindow {


    public Solve7PopupWindow(View mMenuView, int matchParent, int matchParent1) {
        super(mMenuView, matchParent, matchParent1);
    }

    public Solve7PopupWindow() {
        setClippingEnabled(false);//PopupWindow的阴影没有覆盖状态栏的问题
    }

    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor);
    }

    @Override
    public void showAsDropDown(View anchor, final int xoff, final int yoff) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom + Math.abs(yoff);
            setHeight(h);
        }
        super.showAsDropDown(anchor, xoff, yoff);
    }

    @Override
    public void showAsDropDown(View anchor, final int xoff, final int yoff, final int gravity) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom + Math.abs(yoff);
            setHeight(h);
        }
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }
}
