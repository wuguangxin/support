package com.wuguangxin.simple.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

public class LineGraphicView extends View {
    private static final int CIRCLE_SIZE = 10; // 小圆点大小

    private enum LineStyle { Line, Curve}

    private long unitTime = 1000;
    private long maxValue; // Y轴最大值
    private long avgValue; // Y轴平均值

    private int width;
    private int height;
    private int startX; // X坐标的开始位置
    private int startY; // Y坐标的开始位置
    private int spaceX; // X轴的item间隔
    private int spaceY; // Y轴的item间隔
    private int marginLeft; // Y轴间距值
    private int marginTop; // Y轴间距值
    private int marginBottom;

    // 实际内容占的宽高
    private int realWidth;
    private int realHeight;


    private LineStyle mStyle = LineStyle.Curve;
    private DisplayMetrics dm;
    private Paint paintLine;
    private Paint paintText;
    private Point[] points; // 曲线上总点数
    private ArrayList<Long> values; // 值-纵坐标值
    private ArrayList<String> dates; // 日期-横坐标值
    private ArrayList<Integer> xList = new ArrayList<>();// 记录每个x的值


    public LineGraphicView(Context context) {
        this(context, null);
    }

    public LineGraphicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);

        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setTextSize(dip2px(12));
        paintText.setColor(0xff999999);
        paintText.setTextAlign(Paint.Align.LEFT);

        startX = dip2px(50);
        spaceY = dip2px(20);
        marginLeft = dip2px(50);
        marginTop = dip2px(20);
        marginBottom = dip2px(50);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private void calculateValue() {
        int size = values.size();
        maxValue = getMaxValue();
        avgValue = maxValue / size;

        startX = marginLeft + getPaddingLeft();
        startY = height - (marginBottom + getPaddingBottom());

        realWidth = width - startX - getPaddingRight();
        realHeight = height - getPaddingBottom() - -getPaddingTop() - marginBottom - marginTop;

        spaceX = (int) (realWidth / ((maxValue + unitTime) / unitTime));
        spaceY = (int) (realHeight / ((maxValue) / unitTime + 1));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (values.size() == 0) return;

        calculateValue();

        canvas.save();

        // 画网格线
        paintLine.setColor(Color.RED);
        drawLineX(canvas); // X轴
        drawLineY(canvas); // Y轴

        points = getPoints();

        paintLine.setColor(Color.BLUE);
        paintLine.setStrokeWidth(dip2px(2.5f));
        paintLine.setStyle(Style.STROKE);
        if (mStyle == LineStyle.Curve) {
            drawScrollLine(canvas);
        } else {
            drawLine(canvas);
        }

        paintLine.setStyle(Style.FILL);
        for (Point point : points) {
            canvas.drawCircle(point.x, point.y, CIRCLE_SIZE / 2F, paintLine);
        }
        canvas.restore();
    }

    /**
     * 画所有横向表格，包括X轴
     */
    private void drawLineX(Canvas canvas) {
        int size = getCount();
        int x = startX;
        int y = startY;
        for (int i = 0; i < size; i++) {
            String text = String.valueOf(avgValue * i);
            float tw = paintText.measureText(text);
            canvas.drawText(text, x - tw + 10, y+(paintText.getTextSize()/2), paintText);
            canvas.drawLine(x, y, width, y, paintLine);
            y -= spaceY;
        }
    }

    /**
     * 画所有纵向表格，包括Y轴
     */
    private void drawLineY(Canvas canvas) {
        int size = getCount();
        int x = startX;
        int y = startY;
        for (int i = 0; i < size; i++) {
            xList.add(x);

            String text = dates.get(i);
            float tw = paintText.measureText(text);

            canvas.drawLine(x, y, x, y - realWidth, paintLine);
            canvas.drawText(text, x + tw/2, y+tw, paintText);

            x += spaceX;
        }
    }

    private void drawScrollLine(Canvas canvas) {
        Point startp;
        Point endp;
        Point p3 = new Point();
        Point p4 = new Point();
        for (int i = 0; i < points.length - 1; i++) {
            startp = points[i];
            endp = points[i + 1];
            int wt = (startp.x + endp.x) / 2;
            p3.y = startp.y;
            p3.x = wt;
            p4.y = endp.y;
            p4.x = wt;

            Path path = new Path();
            path.moveTo(startp.x, startp.y);
            path.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
            canvas.drawPath(path, paintLine);
        }
    }

    private void drawLine(Canvas canvas) {
        Point startp;
        Point endp;
        for (int i = 0; i < points.length - 1; i++) {
            startp = points[i];
            endp = points[i + 1];
            canvas.drawLine(startp.x, startp.y, endp.x, endp.y, paintLine);
        }
    }

    private Point[] getPoints() {
        Point[] points = new Point[values.size()];
        for (int i = 0; i < values.size(); i++) {
            int ph = startY - (int) (startY * (values.get(i) / maxValue));

            points[i] = new Point(xList.get(i), ph + marginTop);
        }
        return points;
    }

    public void setData(ArrayList<Long> values, ArrayList<String> dates) {
        this.points = new Point[values.size()];
        this.dates = dates;
        this.values = values;
        invalidate();
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public void setAvgValue(int avgValue) {
        this.avgValue = avgValue;
    }

    public void setMarginTop(int marginTop) {
        this.marginTop = marginTop;
    }

    public void setMarginBottom(int marginBottom) {
        this.marginBottom = marginBottom;
    }

    public void setStyle(LineStyle mStyle) {
        this.mStyle = mStyle;
    }

    public void setBheight(int bheight) {
        this.startY = bheight;
    }



    private int getCount() {
        return isEmpty() ? 0 : values.size();
    }

    private boolean isEmpty() {
        return values == null || values.isEmpty();
    }

    private long getMaxValue() {
        if (isEmpty()) return 0;
        long max = 0;
        for (long item : values) {
            max = Math.max(maxValue, item);
        }
        return max;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(float dpValue) {
        return (int) (dpValue * dm.density + 0.5f);
    }

}