package com.wuguangxin.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * 带有上分割线、下分割线的 TextView
 * <p>Created by wuguangxin on 19/12/20 </p>
 */
public class ItemTextView extends AppCompatTextView {

    private final ItemBuilder<AppCompatTextView> itemBuilder;

    public ItemTextView(Context context) {
        this(context, null);
    }

    public ItemTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public ItemTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        itemBuilder = new ItemBuilder<>(this, attrs, defStyleAttr);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        itemBuilder.draw(canvas);
    }

    public ItemBuilder<AppCompatTextView> getItemBuilder() {
        return itemBuilder;
    }
}






