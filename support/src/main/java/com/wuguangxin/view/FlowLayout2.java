package com.wuguangxin.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.wuguangxin.support.R;

import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.ViewCompat;

/**
 * 流布局，自动换行显示，可设置是否单行显示,最多显示item个数
 * 
 * Created by wuguangxin on 2017/9/12.
 */
public class FlowLayout2 extends LinearLayout {
    private int lineSpace; // 行间距
    private int itemSpace; // 列间距
    private int maxSize; // 最多显示标签个数，0默认显示全部
    private boolean singleLine; // 是否单行显示

    private int spanCount = 3;

    public FlowLayout2(Context context) {
        this(context, null);
    }

    public FlowLayout2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initAttr(context, attrs);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout, 0, 0);
        this.lineSpace = array.getDimensionPixelSize(R.styleable.FlowLayout_lineSpace, 0);
        this.itemSpace = array.getDimensionPixelSize(R.styleable.FlowLayout_itemSpace, 0);
        this.singleLine = array.getBoolean(R.styleable.FlowLayout_singleLine, singleLine);
        this.maxSize = array.getInteger(R.styleable.FlowLayout_maxSize, maxSize);
        array.recycle();

        String spanCountValue = attrs.getAttributeValue("http://schemas.android.com/app", "spanCount");
        if (spanCountValue != null) {
            spanCount = Integer.parseInt(spanCountValue);
        }
        int listitemRes = attrs.getAttributeResourceValue("http://schemas.android.com/tools", "listitem", 0);
        if (listitemRes != 0) {
            for (int i = 0; i < listitemRes; i++) {
                View.inflate(getContext(), listitemRes, this);
            }
        }
//        String text = attrs.getAttributeValue("http://schemas.android.com/tools", "text");
//        String targetApi = attrs.getAttributeValue("http://schemas.android.com/tools", "targetApi");
//        String listitem = attrs.getAttributeValue("http://schemas.android.com/tools", "listitem");
//        for (int i = 0; i < attrs.getAttributeCount(); i++) {
//            String name = attrs.getAttributeName(i);
//            String value = attrs.getAttributeValue(i);
//            println(name + "=" + value);
//        }


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
     * @param singleLine 单行则true，否则false
     */
    public void setSingleLine(boolean singleLine) {
        if (this.singleLine != singleLine) {
            this.singleLine = singleLine;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        if (true) return;

//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int width = MeasureSpec.getSize(widthMeasureSpec);
//        int height = MeasureSpec.getSize(heightMeasureSpec);
//        int maxWidth = widthMode != MeasureSpec.AT_MOST && widthMode != MeasureSpec.EXACTLY ? Integer.MAX_VALUE : width;
//        int childLeft = this.getPaddingLeft();
//        int childTop = this.getPaddingTop();
//        int childBottom = this.getPaddingBottom();
//        int maxChildRight = 0;
//        int maxRight = maxWidth - this.getPaddingRight();
//
//        for (int i = 0; i < this.getChildCount(); ++i) {
//            View child = this.getChildAt(i);
//            if (child.getVisibility() != View.GONE) {
//                this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
//                String text = "";
//                if (child instanceof TextView) {
//                    text = ((TextView) child).getText().toString();
//                }
//                Log.e("WGXIN", text + " -----parent width="+getMeasuredWidth() + "----------");
//                Log.e("WGXIN", text + " width="+child.getMeasuredWidth());
//                Log.e("WGXIN", text + " height="+child.getMeasuredHeight());
//                LayoutParams lp = (LayoutParams) child.getLayoutParams();
//                int leftMargin = 0;
//                int rightMargin = 0;
//                if (lp != null) {
//                    leftMargin = lp.leftMargin;
//                    rightMargin = lp.rightMargin;
//                }
//
//                int childRight = childLeft + leftMargin + child.getMeasuredWidth();
//                // 设置限制大小时，超出个数不再显示
//                if (maxSize > 0 && maxSize < i + 1) {
//                    break;
//                }
//                if (childRight > maxRight && !this.singleLine) {
//                    childLeft = this.getPaddingLeft();
//                    childTop = childBottom + this.lineSpace;
//                }
//
//                childRight = childLeft + leftMargin + child.getMeasuredWidth();
//                childBottom = childTop + child.getMeasuredHeight();
//                if (childRight > maxChildRight) {
//                    maxChildRight = childRight;
//                }
//
//                childLeft += leftMargin + rightMargin + child.getMeasuredWidth() + this.itemSpace;
//            }
//        }
//
//        int finalWidth = getMeasuredDimension(width, widthMode, maxChildRight);
//        int finalHeight = getMeasuredDimension(height, heightMode, childBottom);
//        this.setMeasuredDimension(finalWidth, finalHeight);
    }

    private static int getMeasuredDimension(int size, int mode, int childrenEdge) {
        switch (mode) {
        case MeasureSpec.AT_MOST:
            return Math.min(childrenEdge, size);
        case MeasureSpec.EXACTLY:
            return size;
        default:
            return childrenEdge;
        }
    }

    @Override
    protected void onLayout(boolean sizeChanged, int left, int top, int right, int bottom) {
        int l = getPaddingLeft();
        int t = getPaddingTop();
        int r = 0;
        int b = 0;

        int maxR = right;

        int childCount = getChildCount();
        int line = 1;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            r += width;
            b = height*line;
            if (r > maxR || i%spanCount == 0) {
                line++;
                println("换行了");
                l = getPaddingLeft();
                r = getPaddingLeft() + width;
                t += height;
                b += height;
            }

            printInfo(l, t, r, b, maxR, i, width, height);

            child.layout(l, t, r, b);

            l += width;

        }
    }

    private void printInfo(int l, int t, int r, int b, int maxR, int i, int width, int height) {
        println("---------------"+ i +" " + width +"x"+ height + "  maxR="+ maxR
                + "\nl="+ l
                + "\nt="+ t
                + "\nr="+ r
                + "\nb="+ b);
    }


    //    @Override
    protected void onLayout2(boolean sizeChanged, int left, int top, int right, int bottom) {
        if (this.getChildCount() != 0) {
            boolean isRtl = ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL;
            int paddingStart = isRtl ? this.getPaddingRight() : this.getPaddingLeft();
            int paddingEnd = isRtl ? this.getPaddingLeft() : this.getPaddingRight();
            int childStart = paddingStart;
            int childTop = this.getPaddingTop();
            int childBottom = childTop;
            int maxChildEnd = right - left - paddingEnd;

            int childCount = this.getChildCount();
            for (int i = 0; i < childCount; ++i) {
                View child = this.getChildAt(i);
                if (child.getVisibility() != View.GONE) {
                    LayoutParams lp = (LayoutParams) child.getLayoutParams();
                    int startMargin = 0;
                    int endMargin = 0;
                    if (lp instanceof MarginLayoutParams) {
                        MarginLayoutParams marginLp = (MarginLayoutParams) lp;
                        startMargin = MarginLayoutParamsCompat.getMarginStart(marginLp);
                        endMargin = MarginLayoutParamsCompat.getMarginEnd(marginLp);
                    }

                    int childEnd = childStart + startMargin + child.getMeasuredWidth();
                    if (childEnd > maxChildEnd) {
                        childStart = paddingStart;
                        childTop = childBottom + this.lineSpace;
                    }

                    childEnd = childStart + startMargin + child.getMeasuredWidth();
                    childBottom = childTop + child.getMeasuredHeight();

                    int l, r;
                    if (isRtl) {
                        l = maxChildEnd - childEnd;
                        r = maxChildEnd - childStart - startMargin;
                    } else {
                        l = childStart + startMargin;
                        r = childEnd;
                    }
                    child.layout(l, childTop, r, childBottom);

                    childStart += startMargin + endMargin + child.getMeasuredWidth() + this.itemSpace;
                }
            }

//            if (child instanceof TextView) {
//                TextView textView = (TextView) child;
//                textView.setTextSize(6);
//
//                DisplayMetrics dm = getResources().getDisplayMetrics();
//
////                    String wh = child.getMeasuredWidth() + "x" + child.getMeasuredHeight();
//                String wh = child.getWidth() + "x" + child.getHeight();
//                textView.setText(String.format("%s\n%s\n%s\n%s\n%s  %s",
//                        wh, dm.density, dm.densityDpi, dm.scaledDensity, dm.xdpi, dm.ydpi));
//            }
        }
    }


    private void println(String s) {
        Log.e(getClass().getSimpleName(), s);
    }
}
