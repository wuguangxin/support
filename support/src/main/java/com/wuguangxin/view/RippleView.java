/*
 *  Copyright (C) 2015, gelitenight(gelitenight@gmail.com).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.wuguangxin.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

public class RippleView extends View {
    /**
     * +------------------------+
     * |<--wave length->        |______
     * |   /\          |   /\   |  |
     * |  /  \         |  /  \  | amplitude
     * | /    \        | /    \ |  |
     * |/      \       |/      \|__|____
     * |        \      /        |  |
     * |         \    /         |  |
     * |          \  /          |  |
     * |           \/           | water level
     * |                        |  |
     * |                        |  |
     * +------------------------+__|____
     */
    private static final float DEFAULT_AMPLITUDE_RATIO = 0.05f; // 幅度
    private static final float DEFAULT_WATER_LEVEL_RATIO = 0.5f;
    private static final float DEFAULT_WAVE_LENGTH_RATIO = 1.0f;
    private static final float DEFAULT_WAVE_SHIFT_RATIO = 0.0f;

    /** 背景色 */
    private static final int DEFAULT_BG_COLOR = Color.WHITE;
    /** 外圆边框线默认大小和颜色 */
    private static final int DEFAULT_WAVE_BORDER_COLOR = 0xffff0000;
    private static final int DEFAULT_WAVE_BORDER_WIDTH = 5;
    /** 默认水波1、2的颜色 */
//    private static final int DEFAULT_WAVE_COLOR_1 = 0xFF47c480;
//    private static final int DEFAULT_WAVE_COLOR_2 = 0xFF47c480;

//    private static final int DEFAULT_WAVE_COLOR_1 = 0xFF00ff00;
//    private static final int DEFAULT_WAVE_COLOR_2 = 0xFFff0000;

    private static final int DEFAULT_WAVE_COLOR_1 = 0xFFf7a03d;
    private static final int DEFAULT_WAVE_COLOR_2 = 0xFFf7a03d;
    private static final int DEFAULT_TEXT_COLOR = 0xFFffffff;     // 文字颜色
    private static final int DEFAULT_TEXT_SIZE = 36;              // 文字大小

    // if true, the shader will display the wave
    private boolean mShowWave = true;
    // shader containing repeated waves
    private BitmapShader mWaveShader;
    // shader matrix
    private Matrix mShaderMatrix = new Matrix();
    // paint to draw wave
    private Paint mViewPaint = new Paint();
    // paint to draw border
    private Paint mBorderPaint = new Paint();
    // 背景
    private Paint mBgPaint = new Paint();
    // 水波色
    private Paint wavePaint1 = new Paint();
    private Paint wavePaint2 = new Paint();
    // 文字
    private Paint mTextPaint = new Paint();

    private float mDefaultAmplitude;
    private float mDefaultWaterLevel;
    private double mDefaultAngularFrequency;
    /** 进度百分比*/
    private float percent;

    /** 水波纹起伏交错幅度的变化值 */
    private float mAmplitudeRatio = DEFAULT_AMPLITUDE_RATIO; //
    private float mWaveLengthRatio = DEFAULT_WAVE_LENGTH_RATIO;
    /** 水位高度 */
    private float mWaterLevelRatio = DEFAULT_WATER_LEVEL_RATIO; // 水位高度
    /** 水波浪 */
    private float mWaveShiftRatio = DEFAULT_WAVE_SHIFT_RATIO; // 水波浪

    // 圆大小颜色
    private int borderColor = DEFAULT_WAVE_BORDER_COLOR;
    private int borderWidth = DEFAULT_WAVE_BORDER_WIDTH;
    // 水波颜色
    private int waveColor1 = DEFAULT_WAVE_COLOR_1;
    private int waveColor2 = DEFAULT_WAVE_COLOR_2;
    private int bgColor = DEFAULT_BG_COLOR;
    private int textColor = DEFAULT_TEXT_COLOR;
    private int textSize = DEFAULT_TEXT_SIZE;

    public RippleView(Context context) {
        super(context);
    }

    public RippleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RippleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RippleView build() {
        mViewPaint.setAntiAlias(true);
        mViewPaint.setFilterBitmap(true);

        // 背景
        mBgPaint.setFilterBitmap(true);
        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(bgColor);

        // 外圆
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setFilterBitmap(true);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(borderColor);
        mBorderPaint.setStrokeWidth(borderWidth);

        // 波纹1
        wavePaint1.setAntiAlias(true);
        wavePaint1.setColor(waveColor1);
//      wavePaint1.setAlpha(60);

        // 波纹2
        wavePaint2.setAntiAlias(true);
        wavePaint2.setColor(waveColor2);
        wavePaint2.setAlpha(150);

        // 文字
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextScaleX(1f);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(textColor);

        initAnimation(percent / 100);
        return this;
    }

    public boolean isShowWave() {
        return mShowWave;
    }

    /**
     * 设置进度
     */
    public void setProgress(float progress) {
        this.percent = progress;
    }

    /**
     * 开始水波纹 API小于11的将无动画效果
     */
    public void start() {
        mShowWave = true;
        if (mAnimatorSet != null && Build.VERSION.SDK_INT >= 11) {
            mAnimatorSet.start();
        }
    }

    /**
     * 暂停水波纹
     */
    public void stop() {
        mShowWave = false;
        if (mAnimatorSet != null && Build.VERSION.SDK_INT >= 11) {
            mAnimatorSet.cancel();
        }
    }

    public void setBorder(int width, int color) {
        borderColor = color;
        borderWidth = width;
    }

    /**
     * 设置背景色
     * @param color
     */
    public void setBgColor(int color) {
        bgColor = color;
    }

    /**
     * 设置水波颜色
     * @param color
     */
    public void setWaveColor(int color) {
        waveColor1 = waveColor2 = color;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = Color.parseColor(textColor);
    }

    public void setTextSize(int textSine) {
        this.textSize = textSine;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        createShader();
    }

    /**
     * Create the shader with default waves which repeat horizontally, and clamp vertically
     */
    private void createShader() {
        mDefaultAngularFrequency = 2.0f * Math.PI / DEFAULT_WAVE_LENGTH_RATIO / getWidth();
        mDefaultAmplitude = getHeight() * DEFAULT_AMPLITUDE_RATIO;
        mDefaultWaterLevel = getHeight() * DEFAULT_WATER_LEVEL_RATIO;

        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Draw default waves into the bitmap
        // y=Asin(ωx+φ)+h
        int startX1 = 0;
        int endX = getWidth();
        int endY = getHeight();
        final int wave2Shift = getWidth() / 4;

//        while (startX1 < endX+1) {
//            int startX2 = (startX1 + wave2Shift) % endX;
//            int startY = (int) (mDefaultWaterLevel + mDefaultAmplitude * Math.sin(startX1 * mDefaultAngularFrequency));
//            canvas.drawLine(startX1, startY, startX1, endY, wavePaint1); // draw bottom wave with the alpha 40
//            canvas.drawLine(startX2, startY, startX1, endY, wavePaint2); // draw top wave with the alpha 60
//            startX1++;
//        }

        // 第一层
        endX+=1;
        while (startX1 < endX) {
            int startY = (int) (mDefaultWaterLevel + mDefaultAmplitude * Math.sin(startX1 * mDefaultAngularFrequency));
            canvas.drawLine(startX1, startY, startX1, endY, wavePaint1); // draw bottom wave with the alpha 40
            startX1++;
        }
        // 第二层
        startX1 = 0;
        while (startX1 < endX) {
            startX1++;
            int len = startX1 + wave2Shift;
            int startX2 = len % (endX);
            float startY =  (float) (mDefaultWaterLevel + mDefaultAmplitude * Math.sin(startX1 * mDefaultAngularFrequency));
            canvas.drawLine(startX2, startY, startX2, endY, wavePaint2); // draw top wave with the alpha 60
        }

        // use the bitamp to create the shader
        mWaveShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        mViewPaint.setShader(mWaveShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 画背景色
        canvas.drawCircle(getWidth()  >> 1, getHeight() >> 1, (getWidth() - mBorderPaint.getStrokeWidth()) / 2f, mBgPaint);

        // modify paint shader according to mShowWave state
        if (mShowWave && mWaveShader != null) {

            // first call after mShowWave, assign it to our paint
            if (mViewPaint.getShader() == null) {
                mViewPaint.setShader(mWaveShader);
            }

            // sacle shader according to mWaveLengthRatio and mAmplitudeRatio
            // this decides the size(mWaveLengthRatio for width, mAmplitudeRatio for height) of waves
            mShaderMatrix.setScale(mWaveLengthRatio / DEFAULT_WAVE_LENGTH_RATIO, mAmplitudeRatio / DEFAULT_AMPLITUDE_RATIO, 0, mDefaultWaterLevel);

            // translate shader according to mWaveShiftRatio and mWaterLevelRatio
            // this decides the register position(mWaveShiftRatio for x, mWaterLevelRatio for y) of waves
            mShaderMatrix.postTranslate(mWaveShiftRatio * getWidth(), (DEFAULT_WATER_LEVEL_RATIO - mWaterLevelRatio) * getHeight());

            // assign matrix to invalidate the shader
            mWaveShader.setLocalMatrix(mShaderMatrix);

            float radius = (getWidth() >> 1) - (mBorderPaint == null ? 0f : mBorderPaint.getStrokeWidth());
            canvas.drawCircle(getWidth() >> 1, getHeight() >> 1, radius, mViewPaint);
        } else {
            mViewPaint.setShader(null);
        }

        // 画百分比
        String str = (int)percent + "%";
        canvas.drawText(str, getWidth() / 2 - mTextPaint.measureText(str) / 2, getHeight() / 2 + 15, mTextPaint);

        // 画圆
        if (mBorderPaint != null) {
            canvas.drawCircle(getWidth() >> 1, getHeight() >> 1, (getWidth() - mBorderPaint.getStrokeWidth()) / 2, mBorderPaint);
        }

    }

    private AnimatorSet mAnimatorSet;
    private List<Animator> animators;

    /**
     *
     * @param waterLevelRatio 比例 0-1
     */
    private void initAnimation(float waterLevelRatio) {
        if(Build.VERSION.SDK_INT < 11){
            return;
        }
        mAnimatorSet = new AnimatorSet();
        animators = new ArrayList<>();

        // horizontal animation.
        // wave waves infinitely.
        // 水平波浪动画
        ObjectAnimator waveShiftAnim = ObjectAnimator.ofFloat(this, "waveShiftRatio", 0f, 1f); // 0f, 1f
        waveShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
        waveShiftAnim.setInterpolator(new LinearInterpolator());
        waveShiftAnim.setDuration(1000);
        animators.add(waveShiftAnim);

        // vertical animation.
        // water level increases from 0 to center of WaveView
        // 垂直动画
        // 水位高度（上涨）

        ObjectAnimator waterLevelAnim = ObjectAnimator.ofFloat(this, "waterLevelRatio", 0f, waterLevelRatio); // 0f, 0.5f
        waterLevelAnim.setInterpolator(new DecelerateInterpolator());
        waterLevelAnim.setDuration(5000);
        animators.add(waterLevelAnim);

        // amplitude animation.
        // wave grows big then grows small, repeatedly
        // 水波上下起伏的幅度，反复
        ObjectAnimator amplitudeAnim = ObjectAnimator.ofFloat(this, "amplitudeRatio", 0.02f, 0.05f); // 0f, 0.05f
        amplitudeAnim.setInterpolator(new LinearInterpolator());
        amplitudeAnim.setRepeatCount(ValueAnimator.INFINITE);
        amplitudeAnim.setRepeatMode(ValueAnimator.REVERSE);
        amplitudeAnim.setDuration(5000);
        animators.add(amplitudeAnim);

        mAnimatorSet.playTogether(animators);
    }

    public float getWaveShiftRatio() {
        return mWaveShiftRatio;
    }

    /**
     * Shift the wave horizontally according to <code>waveShiftRatio</code>.
     *
     * @param waveShiftRatio Should be 0 ~ 1. Default to be 0.
     *                       Result of waveShiftRatio multiples width of WaveView is the length to shift.
     */
    public void setWaveShiftRatio(float waveShiftRatio) {
        if (mWaveShiftRatio != waveShiftRatio) {
            mWaveShiftRatio = waveShiftRatio;
            invalidate();
        }
    }

    public float getWaterLevelRatio() {
        return mWaterLevelRatio;
    }

    /**
     * Set water level according to <code>waterLevelRatio</code>.
     *
     * @param waterLevelRatio Should be 0 ~ 1. Default to be 0.5.
     *                        Ratio of water level to WaveView height.
     */
    public void setWaterLevelRatio(float waterLevelRatio) {
        if (mWaterLevelRatio != waterLevelRatio) {
            mWaterLevelRatio = waterLevelRatio;
            invalidate();
        }
    }

    public float getAmplitudeRatio() {
        return mAmplitudeRatio;
    }

    /**
     * Set vertical size of wave according to <code>amplitudeRatio</code>
     *
     * @param amplitudeRatio Default to be 0.05. Result of amplitudeRatio + waterLevelRatio should be less than 1.
     *                       Ratio of amplitude to height of WaveView.
     */
    public void setAmplitudeRatio(float amplitudeRatio) {
        if (mAmplitudeRatio != amplitudeRatio) {
            mAmplitudeRatio = amplitudeRatio;
            invalidate();
        }
    }

    public float getWaveLengthRatio() {
        return mWaveLengthRatio;
    }

    /**
     * Set horizontal size of wave according to <code>waveLengthRatio</code>
     *
     * @param waveLengthRatio Default to be 1.
     *                        Ratio of wave length to width of WaveView.
     */
    public void setWaveLengthRatio(float waveLengthRatio) {
        mWaveLengthRatio = waveLengthRatio;
    }

}
