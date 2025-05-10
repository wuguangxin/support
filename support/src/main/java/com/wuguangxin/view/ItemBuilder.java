package com.wuguangxin.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.wuguangxin.support.R;
import com.wuguangxin.utils.Logger;
import com.wuguangxin.utils.ObjectUtils;
import com.wuguangxin.utils.Utils;

import androidx.annotation.NonNull;

/**
 * 带有上分割线、下分割线的TextView的绘辅助类
 */
public class ItemBuilder<T extends TextView> {

    public static final String TAG = ItemBuilder.class.getSimpleName();

    public static final int DIVIDER_NONE = 0;   // 没有指示器
    public static final int DIVIDER_BOTH = 1;   // 上下都有指示器
    public static final int DIVIDER_TOP = 2;    // 上有指示器
    public static final int DIVIDER_BOTTOM = 3; // 下有指示器

    // key
    private String key;             // key文字
    private int keySize;            // key字体大小
    private int keyColor;           // key字体颜色
    private int keyWidth;           // key占位宽度
    private int keyMarginLeft;      // key左内边距
    // divider
    private int dividerMode;
    private int dividerHeight;
    private int dividerNormalColor;
    private int dividerFocusedColor;
    private int dividerTop_marginTop;
    private int dividerTop_marginLeft;
    private int dividerTop_marginRight;
    private int dividerBottom_marginLeft;
    private int dividerBottom_marginRight;
    private int dividerBottom_marginBottom;
    // leftIcon
    private Drawable leftIcon;
    private int leftIconSize;
    private int leftIconMarginLeft;
    // rightIcon
    private Drawable rightIcon;
    private int rightIconSize;
    private int rightIconMarginRight;

    //////////////////////////////////////////////////////////////////////////////////////////

    private Paint dividerPaint;
    private Paint keyPaint;

    private int leftIconWidth;
    private int leftIconHeight;
    private int rightIconWidth;
    private int rightIconHeight;

    private int itemViewWidth;
    private int itemViewHeight;
    private int textPaddingLeft;
    private int textPaddingRight;

    private T mTextView;
    private Context mContext;

    public ItemBuilder(@NonNull T textView, @NonNull AttributeSet attrs, int defStyleAttr) {
        this.mTextView = ObjectUtils.requireNonNull(textView, "textView cannot be empty");
        this.mContext = textView.getContext();
        if (attrs != null) {
            // 只有styleable的名称和View的名称对应上，才能在View中有属性提示，不过没有提示也没关系，只要写对了就可以
            // 所以下面每个if里面的属性名称都是一样的，只是为了区分不同View的styleable，默认用R.styleable.ItemBuilder
            if (textView instanceof ItemTextView) {
                TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.ItemTextView);
                // key
                key = a.getString(R.styleable.ItemTextView_key);
                keySize = a.getDimensionPixelSize(R.styleable.ItemTextView_keySize, (int) textView.getTextSize());
                keyColor = a.getColor(R.styleable.ItemTextView_keyColor, textView.getCurrentTextColor());
                keyWidth = a.getDimensionPixelSize(R.styleable.ItemTextView_keyWidth, -1);
                keyMarginLeft = a.getDimensionPixelSize(R.styleable.ItemTextView_keyMarginLeft, 0);
                // divider
                dividerMode = a.getInteger(R.styleable.ItemTextView_dividerMode, DIVIDER_NONE);
                dividerHeight = a.getDimensionPixelSize(R.styleable.ItemTextView_dividerHeight, 1);
                dividerNormalColor = a.getColor(R.styleable.ItemTextView_dividerNormalColor, Color.parseColor("#dddddd"));
                dividerFocusedColor = a.getColor(R.styleable.ItemTextView_dividerFocusedColor, Color.parseColor("#dddddd"));
                dividerTop_marginTop = a.getDimensionPixelSize(R.styleable.ItemTextView_dividerTop_marginTop, 0);
                dividerTop_marginLeft = a.getDimensionPixelSize(R.styleable.ItemTextView_dividerTop_marginLeft, 0);
                dividerTop_marginRight = a.getDimensionPixelSize(R.styleable.ItemTextView_dividerTop_marginRight, 0);
                dividerBottom_marginLeft = a.getDimensionPixelSize(R.styleable.ItemTextView_dividerBottom_marginLeft, 0);
                dividerBottom_marginRight = a.getDimensionPixelSize(R.styleable.ItemTextView_dividerBottom_marginRight, 0);
                dividerBottom_marginBottom = a.getDimensionPixelSize(R.styleable.ItemTextView_dividerBottom_marginBottom, 0);
                // icon
                leftIcon = a.getDrawable(R.styleable.ItemTextView_leftIcon);
                leftIconSize = a.getDimensionPixelSize(R.styleable.ItemTextView_leftIconSize, 0);
                leftIconMarginLeft = a.getDimensionPixelSize(R.styleable.ItemTextView_leftIconMarginLeft, 0);
                rightIcon = a.getDrawable(R.styleable.ItemTextView_rightIcon);
                rightIconSize = a.getDimensionPixelSize(R.styleable.ItemTextView_rightIconSize, 0);
                rightIconMarginRight = a.getDimensionPixelSize(R.styleable.ItemTextView_rightIconMarginRight, 0);
                a.recycle();
            } else if (textView instanceof ItemEditText) {
                TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.ItemEditText);
                // key
                key = a.getString(R.styleable.ItemEditText_key);
                keySize = a.getDimensionPixelSize(R.styleable.ItemEditText_keySize, (int) textView.getTextSize());
                keyColor = a.getColor(R.styleable.ItemEditText_keyColor, textView.getCurrentTextColor());
                keyWidth = a.getDimensionPixelSize(R.styleable.ItemEditText_keyWidth, -1);
                keyMarginLeft = a.getDimensionPixelSize(R.styleable.ItemEditText_keyMarginLeft, 0);
                // divider
                dividerMode = a.getInteger(R.styleable.ItemEditText_dividerMode, DIVIDER_NONE);
                dividerHeight = a.getDimensionPixelSize(R.styleable.ItemEditText_dividerHeight, 1);
                dividerNormalColor = a.getColor(R.styleable.ItemEditText_dividerNormalColor, Color.parseColor("#dddddd"));
                dividerFocusedColor = a.getColor(R.styleable.ItemEditText_dividerFocusedColor, Color.parseColor("#dddddd"));
                dividerTop_marginTop = a.getDimensionPixelSize(R.styleable.ItemEditText_dividerTop_marginTop, 0);
                dividerTop_marginLeft = a.getDimensionPixelSize(R.styleable.ItemEditText_dividerTop_marginLeft, 0);
                dividerTop_marginRight = a.getDimensionPixelSize(R.styleable.ItemEditText_dividerTop_marginRight, 0);
                dividerBottom_marginLeft = a.getDimensionPixelSize(R.styleable.ItemEditText_dividerBottom_marginLeft, 0);
                dividerBottom_marginRight = a.getDimensionPixelSize(R.styleable.ItemEditText_dividerBottom_marginRight, 0);
                dividerBottom_marginBottom = a.getDimensionPixelSize(R.styleable.ItemEditText_dividerBottom_marginBottom, 0);
                // icon
                leftIcon = a.getDrawable(R.styleable.ItemEditText_leftIcon);
                leftIconSize = a.getDimensionPixelSize(R.styleable.ItemEditText_leftIconSize, 0);
                leftIconMarginLeft = a.getDimensionPixelSize(R.styleable.ItemEditText_leftIconMarginLeft, 0);
                rightIcon = a.getDrawable(R.styleable.ItemEditText_rightIcon);
                rightIconSize = a.getDimensionPixelSize(R.styleable.ItemEditText_rightIconSize, 0);
                rightIconMarginRight = a.getDimensionPixelSize(R.styleable.ItemEditText_rightIconMarginRight, 0);
                a.recycle();
            } else {
                TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.ItemBuilder);
                // key
                key = a.getString(R.styleable.ItemBuilder_key);
                keySize = a.getDimensionPixelSize(R.styleable.ItemBuilder_keySize, (int) textView.getTextSize());
                keyColor = a.getColor(R.styleable.ItemBuilder_keyColor, textView.getCurrentTextColor());
                keyWidth = a.getDimensionPixelSize(R.styleable.ItemBuilder_keyWidth, -1);
                keyMarginLeft = a.getDimensionPixelSize(R.styleable.ItemBuilder_keyMarginLeft, 0);
                // divider
                dividerMode = a.getInteger(R.styleable.ItemBuilder_dividerMode, DIVIDER_NONE);
                dividerHeight = a.getDimensionPixelSize(R.styleable.ItemBuilder_dividerHeight, 1);
                dividerNormalColor = a.getColor(R.styleable.ItemBuilder_dividerNormalColor, Color.parseColor("#dddddd"));
                dividerFocusedColor = a.getColor(R.styleable.ItemBuilder_dividerFocusedColor, Color.parseColor("#dddddd"));
                dividerTop_marginTop = a.getDimensionPixelSize(R.styleable.ItemBuilder_dividerTop_marginTop, 0);
                dividerTop_marginLeft = a.getDimensionPixelSize(R.styleable.ItemBuilder_dividerTop_marginLeft, 0);
                dividerTop_marginRight = a.getDimensionPixelSize(R.styleable.ItemBuilder_dividerTop_marginRight, 0);
                dividerBottom_marginLeft = a.getDimensionPixelSize(R.styleable.ItemBuilder_dividerBottom_marginLeft, 0);
                dividerBottom_marginRight = a.getDimensionPixelSize(R.styleable.ItemBuilder_dividerBottom_marginRight, 0);
                dividerBottom_marginBottom = a.getDimensionPixelSize(R.styleable.ItemBuilder_dividerBottom_marginBottom, 0);
                // icon
                leftIcon = a.getDrawable(R.styleable.ItemBuilder_leftIcon);
                leftIconSize = a.getDimensionPixelSize(R.styleable.ItemBuilder_leftIconSize, 0);
                leftIconMarginLeft = a.getDimensionPixelSize(R.styleable.ItemBuilder_leftIconMarginLeft, 0);
                rightIcon = a.getDrawable(R.styleable.ItemBuilder_rightIcon);
                rightIconSize = a.getDimensionPixelSize(R.styleable.ItemBuilder_rightIconSize, 0);
                rightIconMarginRight = a.getDimensionPixelSize(R.styleable.ItemBuilder_rightIconMarginRight, 0);
                a.recycle();
            }
        }

        // dividerPaint
        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        // keyPaint
        keyPaint = new Paint();
        keyPaint.setAntiAlias(true);
        keyPaint.setColor(keyColor);
        keyPaint.setTextSize(keySize);

        // 计算 leftIcon 的宽高
        if (leftIcon != null) {
            leftIconWidth = leftIcon.getMinimumWidth();
            leftIconHeight = leftIcon.getMinimumHeight();
            if (leftIconSize > 0) {
                leftIconWidth = leftIconHeight = leftIconSize;
            }
        }

        // 计算 rightIcon 的宽高
        if (rightIcon != null) {
            rightIconWidth = rightIcon.getMinimumWidth();
            rightIconHeight = rightIcon.getMinimumHeight();
            if (rightIconSize > 0) {
                rightIconWidth = rightIconHeight = rightIconSize;
            }
        }

        // 计算 text padding左和右
        textPaddingLeft = getTextPaddingStart();
        textPaddingRight = getTextPaddingEnd();

        // 重新设置Text文字 paddingLeft 的值
        if (!isGravityCenter()) {
            textView.setPadding(textPaddingLeft, textView.getPaddingTop(), textView.getPaddingEnd(), textView.getPaddingBottom());
        }

        // 重新设置 drawablePadding
        int rightDrawablePadding = getRightDrawablePadding();
        textView.setCompoundDrawablePadding(rightDrawablePadding);

        // 当未设置背景时，EditText回出现一条默认的下划线，设置背景让它消失
        // InsetDrawable xml中未配置background属性时，默认是这个类型
        if (textView.getBackground() instanceof InsetDrawable) {
            textView.setBackgroundColor(Color.TRANSPARENT);
        }

//        textView.setInputType();
//        if (textView instanceof AppCompatEditText && isPasswordInputType(textView.getInputType())) {
//            textView.setTypeface(Typeface.DEFAULT);
//            textView.setTransformationMethod(new PasswordTransformationMethod());


        // Call the above class using this:
//            textView.setTypeface(Typeface.DEFAULT);
//            textView.setInputType(InputType.TYPE_CLASS_TEXT);
//            textView.setTransformationMethod(new MyPasswordTransformationMethod());
//        }
    }

    /**
     * 判断是否是密码输入类型
     * @param inputType
     * @return
     */
    static boolean isPasswordInputType(int inputType) {
        final int variation = inputType & (EditorInfo.TYPE_MASK_CLASS | EditorInfo.TYPE_MASK_VARIATION);
        return variation == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD)
                || variation == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD)
                || variation == (EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD);
    }

    private static boolean isVisiblePasswordInputType(int inputType) {
        int variation = inputType & (EditorInfo.TYPE_MASK_CLASS | EditorInfo.TYPE_MASK_VARIATION);
        return variation == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
    }

    /**
     * 密码字符替换
     */
    static class MyPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }
    }

    /**
     * 自定义密码字符
     */
    static class PasswordCharSequence implements CharSequence {
        private CharSequence mSource;

        public PasswordCharSequence(CharSequence source) {
            mSource = source; // Store char sequence
        }

        // 自定义密码显示的符号 * ★ ♥ ☺ ☻
        public char charAt(int index) {
            Logger.d("index = " + index);
            return '☻';
        }

        public int length() {
            return mSource.length(); // Return default
        }

        public CharSequence subSequence(int start, int end) {
            return mSource.subSequence(start, end); // Return default
        }
    }


    public void draw(Canvas canvas) {
        if (canvas == null) {
            Logger.d(TAG, "canvas cannot be empty");
            return;
        }
        canvas.save();


        // 获得控件宽高
        itemViewWidth = mTextView.getMeasuredWidth();
        itemViewHeight = mTextView.getMeasuredHeight();

        drawKey(canvas);        // 画 key 文字
        drawLeftIcon(canvas);   // 画 左边icon
        drawRightIcon(canvas);  // 画 右边icon

        // 画 指示器（放在最后画，覆盖在内容上面）
        if (dividerMode == DIVIDER_BOTH) {
            drawDividerTop(canvas);
            drawDividerBottom(canvas);
        } else if (dividerMode == DIVIDER_TOP) {
            drawDividerTop(canvas);
        } else if (dividerMode == DIVIDER_BOTTOM) {
            drawDividerBottom(canvas);
        }

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(20);
//        canvas.drawText("测试", 0, itemViewHeight/2, paint);
        canvas.restore();
    }

    // 判断是否设置了居中，包含水平居中（Gravity.CENTER、Gravity.CENTER_HORIZONTAL）
    private boolean isGravityCenter() {
        return mTextView.getGravity() == Gravity.CENTER || mTextView.getGravity() == Gravity.CENTER_HORIZONTAL;
    }

    private int getRightDrawablePadding() {
        int rightDrawablePadding = 0;
        // 加上 paddingEnd
        rightDrawablePadding += mTextView.getPaddingEnd();
        // 加上 rightIcon 图片的宽度
        if (rightIcon != null) {
            rightDrawablePadding += rightIcon.getMinimumWidth() + rightIconMarginRight;
        }
        return Math.max(mTextView.getCompoundDrawablePadding(), rightDrawablePadding); // 谁大用谁
    }

    // 计算Text文字距离左边的间距
    private int getTextPaddingStart() {
        // 计算 key 字体宽度
        float keyTextWidth = 0;
        if (!TextUtils.isEmpty(key) && keyPaint != null) {
            keyTextWidth = keyPaint.measureText(key);
        }
        // float textHeight = -keyPaint.ascent() + keyPaint.descent();
        int totalWidth = 0;
        if (keyTextWidth > 0) {
            totalWidth = (int) (keyTextWidth + Utils.dip2px(mContext, 10));
        }
        if (keyWidth + keyMarginLeft > keyTextWidth + keyMarginLeft) {
            totalWidth = keyWidth + keyMarginLeft;
        }
        totalWidth += getLeftIcon_rightMarginLeft();
        return Math.max(mTextView.getPaddingStart(), totalWidth); // 谁大用谁
    }

    // 计算Text文字距离左边的间距
    private int getTextPaddingEnd() {
        int paddingEnd = getRightIcon_leftMarginRight();
        // 加上右边图标的宽
        if (rightIcon != null) {
            paddingEnd += rightIcon.getMinimumWidth();
        }
        return Math.max(mTextView.getPaddingEnd(), paddingEnd); // 谁大用谁
    }

    // 画 左边icon
    private void drawLeftIcon(Canvas canvas) {
        if (leftIcon != null) {
            int left = leftIconMarginLeft;
            int top = (itemViewHeight - dividerHeight) / 2 - leftIconHeight / 2;
            int right = left + leftIconWidth;
            int bottom = (itemViewHeight - dividerHeight) / 2 + leftIconHeight / 2;
            leftIcon.setBounds(left, top, right, bottom);
            leftIcon.draw(canvas);
        }
    }

    // 画 右边icon
    private void drawRightIcon(Canvas canvas) {
        if (rightIcon != null) {
            // 获取android:drawableRight的宽度
            int drawableRightWidth = mTextView.getCompoundDrawables()[2].getMinimumWidth();
            // 控件的宽 - rightIcon宽 - rightIcon右边距 - android:drawableRight图标宽 - android:paddingEnd的宽；
            int left = itemViewWidth - rightIconWidth - rightIconMarginRight - drawableRightWidth - mTextView.getPaddingEnd();
            int top = (itemViewHeight - dividerHeight) / 2 - rightIconHeight / 2;
            int right = left + rightIconWidth;
            int bottom = (itemViewHeight - dividerHeight) / 2 + rightIconHeight / 2;
            rightIcon.setBounds(left, top, right, bottom);
            rightIcon.draw(canvas);
        }
    }

    // 画 key 文字
    private void drawKey(Canvas canvas) {
        if (!TextUtils.isEmpty(key)) {
            float x = keyMarginLeft + getLeftIcon_rightMarginLeft();
            float y = itemViewHeight >> 1;
            float offset = Math.abs(keyPaint.descent() + keyPaint.ascent()) / 2;
            canvas.drawText(key, x, y + offset, keyPaint);
        }
    }

    // 画 上指示器
    private void drawDividerTop(Canvas canvas) {
        dividerPaint.setColor(getDividerCurrentColor());
        dividerPaint.setStrokeWidth(dividerHeight);
        float startX = dividerTop_marginLeft;
        float startY = (dividerHeight >> 1) + dividerTop_marginTop;
        float stopX = itemViewWidth - dividerTop_marginRight;
        float stopY = (dividerHeight >> 1) + dividerTop_marginTop;
        canvas.drawLine(startX, startY, stopX, stopY, dividerPaint);
    }

    // 画 下指示器
    private void drawDividerBottom(Canvas canvas) {
        dividerPaint.setColor(getDividerCurrentColor());
        dividerPaint.setStrokeWidth(dividerHeight);
        float startX = dividerBottom_marginLeft;
        float startY = itemViewHeight - (dividerHeight >> 1) - dividerBottom_marginBottom;
        float stopX = itemViewWidth - dividerBottom_marginRight;
        float stopY = itemViewHeight - (dividerHeight >> 1) - dividerBottom_marginBottom;
        canvas.drawLine(startX, startY, stopX, stopY, dividerPaint);
    }

//    // 画四边
//    private void drawDividerBoth(Canvas canvas) {
//        if (dividerMode == DIVIDER_BOTH) {
//            dividerPaint.setColor(getDividerCurrentColor());
//            dividerPaint.setStrokeWidth(dividerHeight);
//            dividerPaint.setStyle(Paint.Style.STROKE);//设置填满
//            float startX = 50;
//            float startY = 70;
//            float stopX = 100;
//            float stopY = 200;
//            canvas.drawRect(startX, startY, stopX, stopY, dividerPaint);
//        }
//    }

    // 计算 leftIcon 的右边到 parent 的左边的大小
    private int getLeftIcon_rightMarginLeft() {
        return leftIcon != null ? leftIconMarginLeft + leftIconWidth : 0;
    }

    // 计算 rightIcon 的左边 到 parent 的右边的大小
    private int getRightIcon_leftMarginRight() {
        return rightIcon != null ? rightIconMarginRight + rightIconWidth : 0;
    }

    // 获取指示器的颜色
    private int getDividerCurrentColor() {
        return dividerFocusedColor != 0 && mTextView.isFocused() ? dividerFocusedColor : dividerNormalColor;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////  以下是xml配置的 ////////////////////////////////////////////
    /////////////////////////////////// getter/setter ////////////////////////////////////////////

    public void setKey(String key) {
        this.key = key;
        mTextView.invalidate();
    }

    public void setKeySize(int keySize) {
        this.keySize = keySize;
        mTextView.invalidate();
    }

    public void setKeyColor(int keyColor) {
        this.keyColor = keyColor;
        mTextView.invalidate();
    }

    public void setKeyWidth(int keyWidth) {
        this.keyWidth = keyWidth;
        mTextView.invalidate();
    }

    public void setKeyMarginLeft(int keyMarginLeft) {
        this.keyMarginLeft = keyMarginLeft;
        mTextView.invalidate();
    }

    public void setDividerMode(int dividerMode) {
        this.dividerMode = dividerMode;
        mTextView.invalidate();
    }

    public void setDividerHeight(int dividerHeight) {
        this.dividerHeight = dividerHeight;
        mTextView.invalidate();
    }

    public void setDividerNormalColor(int dividerNormalColor) {
        this.dividerNormalColor = dividerNormalColor;
        mTextView.invalidate();
    }

    public void setDividerFocusedColor(int dividerFocusedColor) {
        this.dividerFocusedColor = dividerFocusedColor;
        mTextView.invalidate();
    }

    public void setDividerTop_marginTop(int dividerTop_marginTop) {
        this.dividerTop_marginTop = dividerTop_marginTop;
        mTextView.invalidate();
    }

    public void setDividerTop_marginLeft(int dividerTop_marginLeft) {
        this.dividerTop_marginLeft = dividerTop_marginLeft;
        mTextView.invalidate();
    }

    public void setDividerTop_marginRight(int dividerTop_marginRight) {
        this.dividerTop_marginRight = dividerTop_marginRight;
        mTextView.invalidate();
    }

    public void setDividerBottom_marginLeft(int dividerBottom_marginLeft) {
        this.dividerBottom_marginLeft = dividerBottom_marginLeft;
        mTextView.invalidate();
    }

    public void setDividerBottom_marginRight(int dividerBottom_marginRight) {
        this.dividerBottom_marginRight = dividerBottom_marginRight;
        mTextView.invalidate();
    }

    public void setDividerBottom_marginBottom(int dividerBottom_marginBottom) {
        this.dividerBottom_marginBottom = dividerBottom_marginBottom;
        mTextView.invalidate();
    }

    public void setLeftIcon(Drawable leftIcon) {
        this.leftIcon = leftIcon;
        mTextView.invalidate();
    }

    public void setLeftIconSize(int leftIconSize) {
        this.leftIconSize = leftIconSize;
        mTextView.invalidate();
    }

    public void setLeftIconMarginLeft(int leftIconMarginLeft) {
        this.leftIconMarginLeft = leftIconMarginLeft;
        mTextView.invalidate();
    }

    public void setRightIcon(Drawable rightIcon) {
        this.rightIcon = rightIcon;
        mTextView.invalidate();
    }

    public void setRightIconSize(int rightIconSize) {
        this.rightIconSize = rightIconSize;
        mTextView.invalidate();
    }

    public void setRightIconMarginRight(int rightIconMarginRight) {
        this.rightIconMarginRight = rightIconMarginRight;
        mTextView.invalidate();
    }

    public String getKey() {
        return key;
    }

    public int getKeySize() {
        return keySize;
    }

    public int getKeyColor() {
        return keyColor;
    }

    public int getKeyWidth() {
        return keyWidth;
    }

    public int getKeyMarginLeft() {
        return keyMarginLeft;
    }

    public int getDividerMode() {
        return dividerMode;
    }

    public int getDividerHeight() {
        return dividerHeight;
    }

    public int getDividerNormalColor() {
        return dividerNormalColor;
    }

    public int getDividerFocusedColor() {
        return dividerFocusedColor;
    }

    public int getDividerTop_marginTop() {
        return dividerTop_marginTop;
    }

    public int getDividerTop_marginLeft() {
        return dividerTop_marginLeft;
    }

    public int getDividerTop_marginRight() {
        return dividerTop_marginRight;
    }

    public int getDividerBottom_marginLeft() {
        return dividerBottom_marginLeft;
    }

    public int getDividerBottom_marginRight() {
        return dividerBottom_marginRight;
    }

    public int getDividerBottom_marginBottom() {
        return dividerBottom_marginBottom;
    }

    public Drawable getLeftIcon() {
        return leftIcon;
    }

    public int getLeftIconSize() {
        return leftIconSize;
    }

    public int getLeftIconMarginLeft() {
        return leftIconMarginLeft;
    }

    public Drawable getRightIcon() {
        return rightIcon;
    }

    public int getRightIconSize() {
        return rightIconSize;
    }

    public int getRightIconMarginRight() {
        return rightIconMarginRight;
    }

}




