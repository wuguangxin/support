package com.wuguangxin.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * 圆角 ImageView
 */
public class RoundedImageView extends AppCompatImageView {
    private int cornerSize = 30; // 圆角大小
    private Paint paint;
    private RectF rectF;

    public RoundedImageView(Context context) {
        this(context, null);
    }

    public RoundedImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        rectF = new RectF();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true); // 消除锯齿
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawLeftTop(canvas);
        drawRightTop(canvas);
        drawLeftBottom(canvas);
        drawRightBottom(canvas);
    }

    // 画左上
    private void drawLeftTop(Canvas canvas) {
        rectF.set(0, 0, cornerSize * 2, cornerSize * 2);
        Path path = new Path();
        path.moveTo(0, cornerSize);
        path.lineTo(0, 0);
        path.lineTo(cornerSize, 0);
        path.arcTo(rectF, -90, -90);
        path.close();
        canvas.drawPath(path, paint);
    }

    // 画左下
    private void drawLeftBottom(Canvas canvas) {
        int h = getHeight();
        rectF.set(0, h - cornerSize * 2, cornerSize * 2, h);
        Path path = new Path();
        path.moveTo(0, h - cornerSize);
        path.lineTo(0, h);
        path.lineTo(cornerSize, h);
        path.arcTo(rectF, 90, 90);
        path.close();
        canvas.drawPath(path, paint);
    }

    // 画右下
    private void drawRightBottom(Canvas canvas) {
        int w = getWidth();
        int h = getHeight();
        rectF.set(w - cornerSize * 2, h - cornerSize * 2, w, h);
        Path path = new Path();
        path.moveTo(w - cornerSize, h);
        path.lineTo(w, h);
        path.lineTo(w, h - cornerSize);
        path.arcTo(rectF, 0, 90);
        path.close();
        canvas.drawPath(path, paint);
    }

    // 画右上
    private void drawRightTop(Canvas canvas) {
        int w = getWidth();
        rectF.set(w - cornerSize * 2, 0, w, cornerSize * 2);
        Path path = new Path();
        path.moveTo(w, cornerSize);
        path.lineTo(w, 0);
        path.lineTo(w - cornerSize, 0);
        path.arcTo(rectF, -90, 90);
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     * 获取圆角大小px
     *
     * @return
     */
    public int getCornerSize() {
        return cornerSize;
    }

    /**
     * 设置圆角大小
     *
     * @param cornerSize 圆角大小(px)
     */
    public void setCornerSize(int cornerSize) {
        this.cornerSize = cornerSize;
        postInvalidate();
    }

}