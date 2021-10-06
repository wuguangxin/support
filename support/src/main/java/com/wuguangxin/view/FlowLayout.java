package com.wuguangxin.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.wuguangxin.support.R;

import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.ViewCompat;

/**
 * 流布局。
 * 该布局基于{@link com.google.android.material.internal.FlowLayout} 扩展发流布局。
 * 如可设置单行显示、限制可见View个数和每列显示个数。
 * Created by wuguangxin on 2017/9/12.
 */
public class FlowLayout extends ViewGroup {
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

    public FlowLayout(Context context) {
        this(context, (AttributeSet)null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs);
    }

    @TargetApi(21)
    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        if (array != null) {
            this.lineSpace = array.getDimensionPixelSize(R.styleable.FlowLayout_lineSpace, lineSpace);
            this.itemSpace = array.getDimensionPixelSize(R.styleable.FlowLayout_itemSpace, itemSpace);
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
        int childLeft = this.getPaddingLeft();
        int childTop = this.getPaddingTop();
        int childBottom = this.getPaddingBottom();
        int maxChildRight = 0;
        int maxRight = maxWidth - this.getPaddingRight();

        for (int i = 0; i < this.getChildCount(); ++i) {
            View child = this.getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                // 设置限制大小时，超出个数不再显示
                if (maxSize > 0 && maxSize < i + 1) {
                    break;
                }

                this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
                int childRight = childLeft + child.getMeasuredWidth();

                boolean newline = (i > 0 && spanCount > 0 && i % spanCount == 0);
                if (childRight > maxRight && !singleLine || newline) {
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
        if (this.getChildCount() != 0) {
            boolean isRtl = ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL;
            int paddingStart = isRtl ? this.getPaddingRight() : this.getPaddingLeft();
            int paddingEnd = isRtl ? this.getPaddingLeft() : this.getPaddingRight();
            int childStart = paddingStart;
            int childTop = this.getPaddingTop();
            int childBottom = childTop;
            int maxChildEnd = right - left - paddingEnd;
            int childCount = this.getChildCount();
            lines = childCount > 0 ? 1 : 0;
            for(int i = 0; i < childCount; ++i) {
                View child = this.getChildAt(i);
                if (child.getVisibility() != View.GONE) {
                    LayoutParams lp = child.getLayoutParams();
                    int startMargin = 0;
                    int endMargin = 0;
                    if (lp instanceof MarginLayoutParams) {
                        MarginLayoutParams marginLp = (MarginLayoutParams)lp;
                        startMargin = MarginLayoutParamsCompat.getMarginStart(marginLp);
                        endMargin = MarginLayoutParamsCompat.getMarginEnd(marginLp);
                    }

                    int childEnd = childStart + startMargin + child.getMeasuredWidth();
                    if (childEnd > maxChildEnd) {
                        lines++;
                        childStart = paddingStart;
                        childTop = childBottom + this.lineSpace;
                    }

                    childEnd = childStart + startMargin + child.getMeasuredWidth();
                    childBottom = childTop + child.getMeasuredHeight();
                    if (isRtl) {
                        child.layout(maxChildEnd - childEnd, childTop, maxChildEnd - childStart - startMargin, childBottom);
                    } else {
                        child.layout(childStart + startMargin, childTop, childEnd, childBottom);
                    }

                    childStart += startMargin + endMargin + child.getMeasuredWidth() + this.itemSpace;
                }
            }

        }
    }

    private static int getMeasuredDimension(int size, int mode, int childrenEdge) {
        switch(mode) {
        case MeasureSpec.AT_MOST:
            return Math.min(childrenEdge, size);
        case MeasureSpec.EXACTLY:
            return size;
        default:
            return childrenEdge;
        }
    }

    public int getLines() {
        return lines;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
        requestLayout();
    }

    public int getLineSpace() {
        return this.lineSpace;
    }

    public void setLineSpace(int lineSpace) {
        this.lineSpace = lineSpace;
        requestLayout();
    }

    public int getItemSpace() {
        return this.itemSpace;
    }

    protected void setItemSpace(int itemSpace) {
        this.itemSpace = itemSpace;
        requestLayout();
    }

    public boolean isSingleLine() {
        return this.singleLine;
    }

    public void setSingleLine(boolean singleLine) {
        this.singleLine = singleLine;
        requestLayout();
    }
}
