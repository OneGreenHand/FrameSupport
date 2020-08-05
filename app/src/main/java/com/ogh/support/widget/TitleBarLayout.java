package com.ogh.support.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.frame.util.CustomClickListener;
import com.ogh.support.R;

/**
 * @describe 通用标题栏
 */
public class TitleBarLayout extends LinearLayout {
    private int backgroundColor = getResources().getColor(R.color.colorPrimary);//背景颜色
    private int backImage = R.mipmap.back_white;//左侧返回图标
    private boolean backShow = true;//返回图标是否显示
    private String tText = getResources().getString(R.string.app_name);//标题文字
    private int tTextColor = Color.WHITE;//标题文字颜色
    private boolean rTextIsShow;//右侧文字是否显示
    private String rText;//右侧文字
    private int rTextColor = Color.WHITE;//右侧文字颜色
    //控件
    private TextView title;
    private TextView other;
    //点击事件
    private CustomClickListener clickListener;//返回按钮

    public TitleBarLayout(Context context) {
        super(context);
        initView(context, null);
    }

    public TitleBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public TitleBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TitleBarLayout);
            backgroundColor = array.getColor(R.styleable.TitleBarLayout_TblBackgroundColor, getResources().getColor(R.color.colorPrimary));
            backShow = array.getBoolean(R.styleable.TitleBarLayout_TblBackShow, true);
            backImage = array.getResourceId(R.styleable.TitleBarLayout_TblBackImage, R.mipmap.back_white);
            tText = array.getString(R.styleable.TitleBarLayout_TblText);
            tTextColor = array.getColor(R.styleable.TitleBarLayout_TblTextColor, Color.WHITE);
            rTextIsShow = array.getBoolean(R.styleable.TitleBarLayout_TblRTextShow, false);
            rText = array.getString(R.styleable.TitleBarLayout_TblRText);
            rTextColor = array.getColor(R.styleable.TitleBarLayout_TblRTextColor, Color.WHITE);
            array.recycle();
        }
        findId();
    }

    private void findId() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.titlebar, this);
        RelativeLayout layoutTitle = inflate.findViewById(R.id.layout_title);
        ImageView imgFinish = inflate.findViewById(R.id.img_finish);
        title = inflate.findViewById(R.id.app_title);
        other = inflate.findViewById(R.id.other);
        layoutTitle.setBackgroundColor(backgroundColor);
        if (backShow) {
            imgFinish.setVisibility(View.VISIBLE);
            imgFinish.setImageResource(backImage);
        } else
            imgFinish.setVisibility(View.GONE);
        title.setText(TextUtils.isEmpty(tText) ? getResources().getString(R.string.app_name) : tText);
        title.setTextColor(tTextColor);
        if (rTextIsShow) {
            other.setVisibility(View.VISIBLE);
            other.setText(TextUtils.isEmpty(rText) ? "确定" : rText);
            other.setTextColor(rTextColor);
        } else
            other.setVisibility(View.GONE);
        imgFinish.setOnClickListener(new CustomClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (clickListener != null) {
                    clickListener.onClick(v);
                    return;
                }
                if (getContext() instanceof Activity)
                    ((Activity) getContext()).finish();
            }
        });
    }

    public interface BackClickListener {
        void onClick(View view);
    }

    public void setOnBackListener(CustomClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setOnRightListener(CustomClickListener clickListener) {
        other.setOnClickListener(clickListener);
    }

    public void setTitle(String str) {
        title.setText(str);
    }

    public void setRightTitle(String str) {
        other.setVisibility(View.VISIBLE);
        other.setText(str);
    }

    public void setRightIsShow(boolean isShow) {
        other.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public TextView getTitleText() {
        return title;
    }
}