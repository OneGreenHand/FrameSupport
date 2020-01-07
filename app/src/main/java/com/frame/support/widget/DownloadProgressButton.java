package com.frame.support.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.frame.support.R;


public class DownloadProgressButton extends TextView {

    //背景画笔
    private Paint mBackgroundPaint;
    //按钮文字画笔
    private volatile Paint mTextPaint;
    //背景颜色
    private int mBackgroundColor;
    //下载中后半部分后面背景颜色
    private int mBackgroundSecondColor;
    //文字颜色
    private int mTextColor;
    //覆盖后颜色
    private int mTextCoverColor;
    //文字大小
    private float mAboveTextSize = 40;
    private int mProgress = -1;
    private int mToProgress;
    private int mMaxProgress;
    private int mMinProgress;
    private float mProgressPercent;
    private float mButtonRadius;
    private RectF mBackgroundBounds;
    private LinearGradient mProgressBgGradient;
    private LinearGradient mProgressTextGradient;
    //下载平滑动画
    private ValueAnimator mProgressAnimation;
    //显示的文字
    private String mCurrentText;
    private String mNormalText = "下载";//用于重置
    private int mState;//下载状态
    //初始状态
    public static final int NORMAL = 0;
    //下载中
    public static final int DOWNLOADING = 1;
    //暂停
    public static final int PAUSE = 2;
    //完成
    public static final int FINISH = 3;

    public DownloadProgressButton(Context context) {
        this(context, null);
        initAttrs(context, null);
    }

    public DownloadProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public DownloadProgressButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AnimDownloadProgressButton);
            mBackgroundColor = a.getColor(R.styleable.AnimDownloadProgressButton_progressbtn_background_color, Color.parseColor("#6699ff"));
            mBackgroundSecondColor = a.getColor(R.styleable.AnimDownloadProgressButton_progressbtn_background_second_color, Color.LTGRAY);
            mButtonRadius = a.getFloat(R.styleable.AnimDownloadProgressButton_progressbtn_radius, 60);
            mAboveTextSize = a.getFloat(R.styleable.AnimDownloadProgressButton_progressbtn_text_size, 40);
            mTextColor = a.getColor(R.styleable.AnimDownloadProgressButton_progressbtn_text_color, mBackgroundSecondColor);
            mTextCoverColor = a.getColor(R.styleable.AnimDownloadProgressButton_progressbtn_text_covercolor, Color.WHITE);
            mCurrentText = a.getString(R.styleable.AnimDownloadProgressButton_progressbtn_text);
            a.recycle();
        }
        if (TextUtils.isEmpty(mCurrentText))
            mCurrentText = mNormalText;
        else
            mNormalText = mCurrentText;
        init();
        setProgressAnimations();
    }

    private void init() {
        normalState();
        //设置背景画笔
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        //设置文字画笔
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mAboveTextSize);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            setLayerType(LAYER_TYPE_SOFTWARE, mTextPaint);  //解决文字有时候画不出问题
    }

    /**
     * ProgressBar的动画
     */
    private void setProgressAnimations() {
        mProgressAnimation = ValueAnimator.ofInt(0, 1).setDuration(500);
        mProgressAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mProgress = ((mToProgress - mProgress) * (int) animation.getAnimatedValue() + mProgress);
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isInEditMode())
            drawing(canvas);
    }

    private void drawing(Canvas canvas) {
        drawBackground(canvas);
        drawTextAbove(canvas);
    }

    private void drawBackground(Canvas canvas) {
        mBackgroundBounds = new RectF();
        if (mButtonRadius != 0) {//有圆角有设置一点距离,防止显示异常
            mBackgroundBounds.left = 2;
            mBackgroundBounds.top = 2;
            mBackgroundBounds.right = getMeasuredWidth() - 2;
            mBackgroundBounds.bottom = getMeasuredHeight() - 2;
        } else {
            mBackgroundBounds.right = getMeasuredWidth();
            mBackgroundBounds.bottom = getMeasuredHeight();
        }
        switch (mState) {  //color
            case NORMAL:
            case FINISH:
                if (mBackgroundPaint.getShader() != null)
                    mBackgroundPaint.setShader(null);
                mBackgroundPaint.setColor(mBackgroundColor);
                canvas.drawRoundRect(mBackgroundBounds, mButtonRadius, mButtonRadius, mBackgroundPaint);
                break;
            case PAUSE:
            case DOWNLOADING:
                mProgressPercent = mProgress / (mMaxProgress + 0f);
                mProgressBgGradient = new LinearGradient(0, 0, getMeasuredWidth(), 0,
                        new int[]{mBackgroundColor, mBackgroundSecondColor},
                        new float[]{mProgressPercent, mProgressPercent + 0.001f},
                        Shader.TileMode.CLAMP
                );
                mBackgroundPaint.setColor(mBackgroundColor);
                mBackgroundPaint.setShader(mProgressBgGradient);
                canvas.drawRoundRect(mBackgroundBounds, mButtonRadius, mButtonRadius, mBackgroundPaint);
                break;
        }
    }

    private void drawTextAbove(Canvas canvas) {
        final float y = canvas.getHeight() / 2 - (mTextPaint.descent() / 2 + mTextPaint.ascent() / 2);
        if (TextUtils.isEmpty(mCurrentText))
            mCurrentText = mNormalText;
        final float textWidth = mTextPaint.measureText(mCurrentText);
        switch (mState) {  //color
            case NORMAL:
            case FINISH:
                mTextPaint.setShader(null);
                mTextPaint.setColor(mTextCoverColor);
                canvas.drawText(mCurrentText, (getMeasuredWidth() - textWidth) / 2, y, mTextPaint);
                break;
            case PAUSE:
            case DOWNLOADING:
                //进度条压过距离
                float coverlength = getMeasuredWidth() * mProgressPercent;
                //开始渐变指示器
                float indicator1 = getMeasuredWidth() / 2 - textWidth / 2;
                //结束渐变指示器
                float indicator2 = getMeasuredWidth() / 2 + textWidth / 2;
                //文字变色部分的距离
                float coverTextLength = textWidth / 2 - getMeasuredWidth() / 2 + coverlength;
                float textProgress = coverTextLength / textWidth;
                if (coverlength <= indicator1) {
                    mTextPaint.setShader(null);
                    mTextPaint.setColor(mTextColor);
                } else if (indicator1 < coverlength && coverlength <= indicator2) {
                    mProgressTextGradient = new LinearGradient((getMeasuredWidth() - textWidth) / 2, 0, (getMeasuredWidth() + textWidth) / 2, 0,
                            new int[]{mTextCoverColor, mTextColor},
                            new float[]{textProgress, textProgress + 0.001f},
                            Shader.TileMode.CLAMP);
                    mTextPaint.setColor(mTextColor);
                    mTextPaint.setShader(mProgressTextGradient);
                } else {
                    mTextPaint.setShader(null);
                    mTextPaint.setColor(mTextCoverColor);
                }
                canvas.drawText(mCurrentText, (getMeasuredWidth() - textWidth) / 2, y, mTextPaint);
                break;
        }

    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mState = ss.state;
        mProgress = ss.progress;
        mCurrentText = ss.currentText;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState(superState, mProgress, mState, mCurrentText);
    }

    public static class SavedState extends BaseSavedState {
        private int progress;
        private int state;
        private String currentText;

        public SavedState(Parcelable parcel, int progress, int state, String currentText) {
            super(parcel);
            this.progress = progress;
            this.state = state;
            this.currentText = currentText;
        }

        private SavedState(Parcel in) {
            super(in);
            progress = in.readInt();
            state = in.readInt();
            currentText = in.readString();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(progress);
            out.writeInt(state);
            out.writeString(currentText);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    public int getState() {
        return mState;
    }

    /**
     * 设置按钮文字
     */
    public void setCurrentText(String str) {
        mCurrentText = str;
        invalidate();
    }

    /**
     * 获取当前文字
     */
    public String getCurrentText() {
        return mCurrentText;
    }

    /**
     * 重置为默认状态
     */
    public void reset() {
        mCurrentText = mNormalText;
        normalState();
        invalidate();
    }

    /**
     * 下载完成
     */
    public void DownloadFinish() {
        mCurrentText = "打开";
        mState = FINISH;
        if (mProgressAnimation != null && mProgressAnimation.isRunning())
            mProgressAnimation.end();
        invalidate();
    }

    /**
     * 暂停
     */
    public void DownloadStop() {
        mCurrentText = "继续";
        mState = PAUSE;
        invalidate();
    }

    /**
     * 下载错误
     */
    public void DownloadError() {
        mCurrentText = "重新下载";
        normalState();
        invalidate();
    }

    /**
     * 开始或恢复下载
     */
    public void startDownload() {
        mCurrentText = "下载中";
        mState = DOWNLOADING;
        invalidate();
    }

    /**
     * 设置带下载进度的文字
     */
    public void setProgress(String text, int progress) {
        if (progress >= mMinProgress && progress < mMaxProgress) {
            if (TextUtils.isEmpty(text))
                mCurrentText = getContext().getResources().getString(R.string.downloaded, progress);
            else
                mCurrentText = text + getContext().getResources().getString(R.string.downloaded, progress);
            mToProgress = progress;
            if (mProgressAnimation != null && !mProgressAnimation.isRunning())
                mProgressAnimation.start();
        } else if (progress < mMinProgress) {
            mProgress = 0;
        } else if (progress >= mMaxProgress) {
            mProgress = 100;
            if (TextUtils.isEmpty(text))
                mCurrentText = getContext().getResources().getString(R.string.downloaded, mProgress);
            else
                mCurrentText = text + getContext().getResources().getString(R.string.downloaded, mProgress);
            invalidate();
        }
    }

    public void setProgress(int progress) {
        setProgress("", progress);
    }

    /**
     * 释放内存,防止内存泄漏(请在结束后调用)
     */
    public void clear() {
        if (mProgressAnimation != null) {
            mProgressAnimation.cancel();
            //   mProgressAnimation.removeAllListeners();
        }
    }

    @Override
    public void setTextSize(float size) {
        mAboveTextSize = size;
        mTextPaint.setTextSize(size);
    }

    @Override
    public float getTextSize() {
        return mAboveTextSize;
    }

    private void normalState() {
        mState = NORMAL;
        mMaxProgress = 100;
        mMinProgress = 0;
        mProgress = 0;
        if (mProgressAnimation != null && mProgressAnimation.isRunning())
            mProgressAnimation.end();
    }
}