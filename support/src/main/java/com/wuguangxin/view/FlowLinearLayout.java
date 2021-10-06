package com.wuguangxin.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.wuguangxin.support.R;

/**
 * 流布局，自动换行显示，可设置是否单行显示，最多显示item个数.
 * 这是一个使用LinearLayout实现的流布局，和{@link FlowLayout}一样。
 *
 * <p>Created by wuguangxin on 2017/9/12.
 */
public class FlowLinearLayout extends LinearLayout {
    /**
     * 行间距
     */
    private int lineSpace;
    /**
     * 列间距
     */
    private int itemSpace;
    /**
     * 每行显示的个数（当设置了该值，会出现个数超出一行可显示数量时会折行显示，在不确定的情况下，尽量不使用该字段）
     */
    private int spanCount;
    /**
     * 最多显示个数，0默认显示全部
     */
    private int maxSize;
    /**
     * 是否单行显示
     */
    private boolean singleLine;
    /**
     * 可显示的View总行数
     */
    private int lines;

    public FlowLinearLayout(Context context) {
        this(context, null);
    }

    public FlowLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        if (array != null) {
            this.lineSpace = array.getDimensionPixelSize(R.styleable.FlowLayout_lineSpace, 0);
            this.itemSpace = array.getDimensionPixelSize(R.styleable.FlowLayout_itemSpace, 0);
            this.spanCount = array.getInteger(R.styleable.FlowLayout_spanCount, 0);
            this.singleLine = array.getBoolean(R.styleable.FlowLayout_singleLine, singleLine);
            this.maxSize = array.getInteger(R.styleable.FlowLayout_maxSize, maxSize);
            array.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int maxWidth = widthMode != MeasureSpec.AT_MOST && widthMode != MeasureSpec.EXACTLY ? Integer.MAX_VALUE : width;
        int childLeft = getPaddingLeft();
        int childTop = getPaddingTop();
        int childBottom = getPaddingBottom();
        int maxChildRight = 0;
        int maxRight = maxWidth - getPaddingRight();

        int visibleCount = 0; // 可显示的数量
        for (int i = 0; i < getChildCount(); ++i) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                visibleCount++;
                // 设置限制大小时，超出个数不再显示
                if (maxSize > 0 && visibleCount > maxSize) {
                    break;
                }

                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                int childRight = childLeft + child.getMeasuredWidth();
                if (childRight > maxRight && !singleLine) {
                    childLeft = getPaddingLeft();
                    childTop = childBottom + lineSpace;
                }

                childRight = childLeft + child.getMeasuredWidth();
                childBottom = childTop + child.getMeasuredHeight();
                if (childRight > maxChildRight) {
                    maxChildRight = childRight;
                    childBottom += getPaddingBottom();
                }

                childLeft += child.getMeasuredWidth() + itemSpace;
            }
        }

        int finalWidth = getMeasuredDimension(width, widthMode, maxChildRight);
        int finalHeight = getMeasuredDimension(height, heightMode, childBottom);
        this.setMeasuredDimension(finalWidth, finalHeight);
    }

    @Override
    protected void onLayout(boolean sizeChanged, int left, int top, int right, int bottom) {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        int l = paddingLeft;
        int t = paddingTop;
        int r = 0;
        int b;

        int visibleCount = 0; // 可显示的View的个数
        int childCount = getChildCount();
        lines = childCount > 0 ? 1 : 0;
        // 对子View重新排列
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();

            // 是否达到换行条件
            boolean newline = (visibleCount > 0 && spanCount > 0 && visibleCount % spanCount == 0);
            if (r > right || newline) {
                //println("换行了");
                lines++;
                l = paddingLeft;
                t = t + height + lineSpace;
            }
            r = l + width;
            b = t + height;

            // printInfo(l, t, r, b, right, i, line, width, height);
            child.layout(l, t, r, b);

            l = r + itemSpace;
            r = l + width;

            visibleCount++;
        }
    }

    private int getMeasuredDimension(int size, int mode, int childrenEdge) {
        switch (mode) {
        case MeasureSpec.AT_MOST:
            return Math.min(childrenEdge, size);
        case MeasureSpec.EXACTLY:
            return size;
        default:
            return childrenEdge;
        }
    }

    /**
     * 获取行数
     *
     * @return
     */
    public int getLines() {
        return lines;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        if (this.maxSize != maxSize) {
            this.maxSize = maxSize;
            requestLayout();
        }
    }

    public float getLineSpace() {
        return this.lineSpace;
    }

    /**
     * 设置行间距（垂直间距）
     *
     * @param lineSpace 行间距 dp
     */
    public void setLineSpace(int lineSpace) {
        if (this.lineSpace != lineSpace) {
            this.lineSpace = lineSpace;
            requestLayout();
        }
    }

    public float getItemSpace() {
        return this.itemSpace;
    }

    /**
     * 设置item的间距（水平间距）
     *
     * @param itemSpace Item间距 dp
     */
    protected void setItemSpace(int itemSpace) {
        if (this.itemSpace != itemSpace) {
            this.itemSpace = itemSpace;
            requestLayout();
        }
    }

    public boolean isSingleLine() {
        return this.singleLine;
    }

    /**
     * 设置是否只显示一行
     *
     * @param singleLine 单行则true，否则false
     */
    public void setSingleLine(boolean singleLine) {
        if (this.singleLine != singleLine) {
            this.singleLine = singleLine;
            requestLayout();
        }
    }

    private void printInfo(int l, int t, int r, int b, int maxR, int i, int line, int width, int height) {
        println("第" + line + "行------" + i + " " + width + "x" + height + "  maxR=" + maxR
                + " l=" + l
                + " t=" + t
                + " r=" + r
                + " b=" + b);
    }

    private void println(String s) {
        Log.e(getClass().getSimpleName(), s);
    }
}
