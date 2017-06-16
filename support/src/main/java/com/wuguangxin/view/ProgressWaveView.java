package com.wuguangxin.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.wuguangxin.R;

/**
 * 水波纹进度组件
 */
public class ProgressWaveView extends View {
    private final int DEF_ABOVE_COLOR = 0x99ff0000;
    private final int DEF_BEHIND_COLOR = 0x33ff0000;

    // 水波颜色默认颜色
    private int mAboveColor = DEF_ABOVE_COLOR;
    private int mBehindColor = DEF_BEHIND_COLOR;
    // 水波进度
    private final float DEFAULT_LEVEL_RATIO = 0.5f;
    private float mWaveProgress = DEFAULT_LEVEL_RATIO;
    // 水波高度
    private final float DEFAULT_AMPLITUDE_RATIO = 0.05f;
    private float mWaveAmplitudeRatio = DEFAULT_AMPLITUDE_RATIO;
    // 水波周期
    private float mWaveCycleRatio = 1.0f;
    // 水波位移
    private float mWaveTranslateRatio = 1.0f;
    // 水波进度形状（方形）
    private ShapeType mShapeType = ShapeType.Square;

    public enum ShapeType {
        Circle, Square
    }

    private Paint mPaint;
    private Matrix mShaderMatrix;
    private Shader mShader;
    private Paint mBorderPaint;

    private AnimatorSet animatorSet;
    private ObjectAnimator mAnimAmplitude;
    private ObjectAnimator mAnimTranslate;
    private ObjectAnimator mAnimProgress;

    public ProgressWaveView(Context context) {
        this(context, null);
    }

    public ProgressWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ProgressWaveView);
        if (ta != null) {
            mAboveColor = ta.getColor(R.styleable.ProgressWaveView_behindColor, DEF_ABOVE_COLOR);
            mBehindColor = ta.getColor(R.styleable.ProgressWaveView_behindColor, DEF_BEHIND_COLOR);
            mWaveProgress = ta.getFloat(R.styleable.ProgressWaveView_waveProgress, 0.0F);

            if(mWaveProgress > 1) mWaveProgress = 1;
            int mShapeTypeCode = ta.getInt(R.styleable.ProgressWaveView_shapeType, 0);
            if(mShapeTypeCode == 0){
                mShapeType = ShapeType.Square;
            } else if(mShapeTypeCode == 1){
                mShapeType = ShapeType.Circle;
            } else {
                mShapeType = ShapeType.Square;
            }
            ta.recycle();
        }

        mShaderMatrix = new Matrix();

        initAnim();
    }

    private void initAnim() {
        if(Build.VERSION.SDK_INT < 11){
            return;
        }
        // 水波移动
        mAnimTranslate = ObjectAnimator.ofFloat(this, "waveTranslateRatio", 0f, 1f);
        mAnimTranslate.setRepeatCount(ValueAnimator.INFINITE);
        mAnimTranslate.setInterpolator(new LinearInterpolator());
        mAnimTranslate.setDuration(1000);

        // 水波振幅
        mAnimAmplitude = ObjectAnimator.ofFloat(this, "waveAmplitudeRatio", 0.02f, 0.05f);
        mAnimAmplitude.setRepeatCount(ValueAnimator.INFINITE);
        mAnimAmplitude.setRepeatMode(ValueAnimator.REVERSE);
        mAnimAmplitude.setInterpolator(new LinearInterpolator());
        mAnimAmplitude.setDuration(5000);

        // 水波进度
//        mAnimProgress = ObjectAnimator.ofFloat(this, "progress", 0f, mWaveProgress);
//        mAnimProgress.setInterpolator(new DecelerateInterpolator());
//        mAnimProgress.setDuration(10000);

        animatorSet = new AnimatorSet();
        animatorSet.playTogether(mAnimTranslate, mAnimAmplitude);
    }


    /**
     * 开始动画
     */
    public void startAnim() {
        if(animatorSet != null){
            animatorSet.start();
        }
    }

    /**
     * 暂停动画
     */
    public void pauseAnim() {
        if(animatorSet != null){
            animatorSet.cancel();
        }
    }

    /**
     * 设置水波的颜色
     * @param behindColor 后面水波颜色
     * @param aboveColor 前面水波颜色
     */
    public void setWaveColor(int behindColor, int aboveColor) {
        mBehindColor = behindColor;
        mAboveColor = aboveColor;
        // 如果shader是null，代表onSizeChange还未执行。当前不需要创建shader
        if (mShader != null) {
            createShader();
            invalidate();
        }
    }

    /**
     * 设置边框大小和颜色
     * @param width 边框宽度
     * @param color 边框颜色
     */
    public void setBorder(int width, int color) {
        if (mBorderPaint == null) {
            mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mBorderPaint.setStyle(Paint.Style.STROKE);
        }
        mBorderPaint.setStrokeWidth(width);
        mBorderPaint.setColor(color);
        invalidate();
    }

    /**
     * 设置水波进度条形状
     * @param shapeType
     */
    public void setShapeType(ShapeType shapeType) {
        mShapeType = shapeType;
        invalidate();
    }

    /**
     * 设置水波进度
     * @param progress 区间 0.0f~1.0f
     */
    public void setProgress(float progress) {
        if (mWaveProgress != progress) {
            mWaveProgress = progress;
            invalidate();
        }
    }

    /**
     * 设置水波振幅，默认是控件高度的0.05f
     * @param waveAmplitudeRatio 建议区间 0.0001f ~ 0.05f
     */
    public void setWaveAmplitudeRatio(float waveAmplitudeRatio) {
        if (mWaveAmplitudeRatio != waveAmplitudeRatio) {
            this.mWaveAmplitudeRatio = waveAmplitudeRatio;
            invalidate();
        }
    }

    /**
     * 设置水波X轴的位移
     * @param waveTranslateRatio
     */
    public void setWaveTranslateRatio(float waveTranslateRatio) {
        if (mWaveAmplitudeRatio != waveTranslateRatio && waveTranslateRatio > 0) {
            this.mWaveTranslateRatio = waveTranslateRatio;
            invalidate();
        }
    }

    /**
     * 设置水波的周期（宽度）0.0f - 1.0f
     * @param waveCycleRatio
     */
    public void setWaveCycleRatio(float waveCycleRatio) {
        if (mWaveCycleRatio != waveCycleRatio) {
            this.mWaveCycleRatio = waveCycleRatio;
            invalidate();
        }
    }

    public float getWaveLevelRatio() {
        return mWaveProgress;
    }

    public float getWaveAmplitudeRatio() {
        return mWaveAmplitudeRatio;
    }

    public float getWaveTranslateRatio() {
        return mWaveTranslateRatio;
    }


    public float getWaveCycleRatio() {
        return mWaveCycleRatio;
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        createShader();
    }

    // y=Asin(ωx+φ)+k
    private void createShader() {
        int height = getHeight();
        int width = getWidth();
        // ω周期  让一个周期的宽度正好是width
        double frequency = 2 * Math.PI / width;
        // A振幅  默认的振幅是高度的0.05f
        float amplitude = height * DEFAULT_AMPLITUDE_RATIO;
        // k（y轴偏移量，进度） 默认的进度是50%
        float level = height * DEFAULT_LEVEL_RATIO;

        Bitmap waveBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(waveBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);

        Path abovePath = new Path();
        Path behindPath = new Path();
        abovePath.moveTo(0, height);
        behindPath.moveTo(0, height);
        for (int x = 0; x <= width; x++) {
            // y=Asin(ωx+φ)+k
            float aboveY = (float) (amplitude * Math.sin(frequency * x)) + level;
            // 背面的水波偏移一些，和前面的错开。
            float behindY = (float) (amplitude * Math.sin(frequency * x + width / 4 * frequency)) + level;
            abovePath.lineTo(x, aboveY);
            behindPath.lineTo(x, behindY);
        }
        abovePath.lineTo(width + 1, height);
        behindPath.lineTo(width + 1, height);
        paint.setColor(mBehindColor);
        canvas.drawPath(behindPath, paint);
        paint.setColor(mAboveColor);
        canvas.drawPath(abovePath, paint);

        mShader = new BitmapShader(waveBitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setShader(mShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // dy  ±0.5f
        mShaderMatrix.setTranslate(mWaveTranslateRatio * getWidth(), (DEFAULT_LEVEL_RATIO - mWaveProgress) * getHeight());
        mShaderMatrix.postScale(mWaveCycleRatio, mWaveAmplitudeRatio / DEFAULT_AMPLITUDE_RATIO, 0, getHeight() * (1 - mWaveProgress));
        mShader.setLocalMatrix(mShaderMatrix);

        int borderWidth = mBorderPaint == null ? 0 : (int) mBorderPaint.getStrokeWidth();
        switch (mShapeType) {
        case Circle:
            if (borderWidth > 0) {
                canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, getWidth() / 2f - borderWidth / 2f, mBorderPaint);
            }
            canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, getWidth() / 2f - borderWidth, mPaint);
            break;
        case Square:
            if (borderWidth > 0) {
                canvas.drawRect(borderWidth / 2f, borderWidth / 2f, getWidth() - borderWidth / 2f, getHeight() - borderWidth / 2f, mBorderPaint);
            }
            canvas.drawRect(borderWidth, borderWidth, getWidth() - borderWidth, getHeight() - borderWidth, mPaint);
            break;
        }

    }
}
